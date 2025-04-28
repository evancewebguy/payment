# Payment Microservice

This is a Java-based Spring Boot microservice designed to handle Business-to-Consumer (B2C) transactions via mobile
money platforms (M-Pesa, Airtel Money) in Kenya. The microservice initiates B2C payments and sends SMS notifications to
the recipient upon success or failure. It also provides endpoints for querying the status of payments and retrieving
transaction history. The service is built with security in mind, using OAuth2 for authentication.

## Features

- **Initiate B2C Payment**: Allows clients to initiate payments to recipients using mobile money platforms.
- **Get Payment Status**: Provides the status of a previously initiated payment.
- **Query User's Transactions**: Allows clients to retrieve all payments made to a specific phone number.
- **Retrieve All Payments**: Fetches all transactions made through the system.
- **Security**: OAuth2 authentication for securing the API.
- **Notifications**: Sends SMS notifications using a mock SMS gateway.
- **Logging and Error Handling**: Logs transactions and errors for observability and debugging.
- **Containerization**: Docker support for easy deployment.
- **Persistence**: Uses an in-memory database (H2) for data persistence.
- **Testing**: Unit and integration tests for ensuring quality.

## Endpoints

### 1. **Initiate Payment**

- **URL**: `/api/v1/payment`
- **Method**: `POST`
- **Request Body**:
  ```json
  {
    "messageId": "u4heruij2bqeuirjb2i",
    "data": {
      "amount": "1000",
      "phoneNumber": "0712345678",
      "senderName": "John Doe"
    }
  }
  ```
- **Response**:
    - 202 Accepted: The payment initiation was successful.
- **Description**: Initiates a B2C payment to the recipient.

### 2. **Get Payment Status**

- **URL**: `/api/v1/payment/status`
- **Method**: `POST`
- **Request Body**:
  ```json
  {
  "messageId": "",
    "data": {
      "endToEndId": "12345",
  "phone": "020202020"
    }
  }
  ```
- **Response**:
    - 200 OK: Returns the payment status (Success/Failure).
- **Description**: Fetches the status of a previously initiated payment.

### 3. **Get User's Payments**

- **URL**: `/api/v1/payment?phone={phone}`
- **Method**: `GET`
- **Request Params**:
    - `phone`: The phone number of the user.
- **Response**:
    - 200 OK: A list of payments made to the specified phone number.
- **Description**: Fetches all transactions made to a specific phone number.

### 4. **Get All Payments**

- **URL**: `/api/v1/payment`
- **Method**: `GET`
- **Response**:
    - 200 OK: A list of all payments.
- **Description**: Fetches all transactions in the system.

## Authentication

This service uses OAuth2 authentication, which is implemented with Google as the authorization provider. To
authenticate, navigate to:

```
http://localhost:8080/login/oauth2/code/google
```

The client application should authenticate through Google OAuth2 to get an access token for using the API.

## Design Patterns and Structure

- **Controller-Service Pattern**: The payment logic is abstracted into services, which are called by the controller to
  handle requests and responses.
- **DTO (Data Transfer Object)**: Used to encapsulate the request and response payloads.
- **Facade Pattern**: The payment service abstracts the logic for interacting with the mobile money APIs and the SMS
  gateway.

## Dependencies

- **Spring Boot**: The main framework for building the microservice.
- **Spring Security**: To handle OAuth2 authentication and authorization.
- **Spring Web**: To build RESTful endpoints.
- **Lombok**: To reduce boilerplate code (e.g., getters, setters, constructors).
- **H2 Database**: In-memory database for persistence.
- **Docker**: For containerizing the microservice.

## Assumed Workflow

1. A user sends a request to initiate a payment via the `/api/v1/payment` endpoint.
2. The service mocks the process of communicating with a mobile money platform (e.g., M-Pesa, Airtel Money).
3. Upon successful payment, the service sends an SMS notification to the recipient using a mock SMS gateway.
4. The service allows clients to check the status of the payment and retrieve transaction history.

## How to Run

### 1. Clone the repository

   ```bash
   git clone 
   cd payment
   ```

### 2. Run the Application

You can run the application locally with Maven:

   ```bash
   ./mvnw spring-boot:run
   ```

Or use Docker to containerize and run the service:

   ```bash
   docker-compose up
   ```

### 3. Access the Application

- API Base URL: `http://localhost:8080`
- OAuth2 Login URL: `http://localhost:8080/login/oauth2/code/google`

### 4. Test the API

Use Postman or any HTTP client to test the API endpoints.

## Edge Cases Considered

- Invalid transaction IDs when querying payment status.
- Invalid phone numbers when querying user transactions.
- Handling of network failures during mock mobile money or SMS gateway interaction.
- OAuth2 login failures (e.g., expired tokens, unauthorized access).

## Testing

- Unit tests for each service and controller method.
- Integration tests to validate the interaction between services.
- Mocked responses for mobile money API and SMS gateway.