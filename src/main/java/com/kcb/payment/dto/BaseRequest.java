package com.kcb.payment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BaseRequest<T> {
    private String messageId;
    private T data;
}
