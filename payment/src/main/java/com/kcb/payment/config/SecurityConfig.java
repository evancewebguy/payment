package com.kcb.payment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OAuth2AuthorizedClientService clientService;

    public SecurityConfig(OAuth2AuthorizedClientService clientService) {
        this.clientService = clientService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/oauth2/authorization/google").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(customAuthenticationSuccessHandler())
                ).oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.decoder(jwtDecoder())) // Configure JWT decoder
                );
        ;
        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            if (authentication instanceof OAuth2AuthenticationToken token) {
                if (token.isAuthenticated()) {
                    OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(
                            token.getAuthorizedClientRegistrationId(),
                            token.getName());

                    if (client != null && client.getAccessToken() != null) {
                        Map<String, Object> tokenInfo = new HashMap<>();
                        tokenInfo.put("access_token", client.getAccessToken().getTokenValue());
                        tokenInfo.put("token_type", client.getAccessToken().getTokenType().getValue());
                        tokenInfo.put("expires_at", client.getAccessToken().getExpiresAt() != null ?
                                client.getAccessToken().getExpiresAt().getEpochSecond() : null);
                        tokenInfo.put("principal", token.getPrincipal().getAttributes());

                        response.setContentType("application/json");
                        objectMapper.writeValue(response.getWriter(), tokenInfo);
                    }
                }
            }
        };
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return JwtDecoders.fromIssuerLocation("https://accounts.google.com");
    }
}
