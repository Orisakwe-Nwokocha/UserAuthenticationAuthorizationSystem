# JWT Project - User Authentication and Authorization System

## Overview
This project implements a secure authentication system using JWT (JSON Web Token) and RSA encryption for a Spring Boot 
application. It leverages Spring Security for security configurations and integrates custom authentication and 
authorization mechanisms. The detailed implementation of each class can be found in the source code.

## Table of Contents
1. Setup Instructions
2. API Documentation
   - Authentication Endpoints
   - User Endpoints
3. Classes Overview
   - Configuration Classes
   - Filter Classes
   - Manager and Provider Classes
   - Service Classes
   - Model Classes
   - Testing

## Setup Instructions
### Prerequisites
- Java Development Kit (JDK) 21 or later
- Maven
- Spring Boot
- Configure RSA Keys: Generate RSA keys for signing JWT tokens and place them in your application's configuration file

## API Documentation
### Authentication Endpoints
#### Register
- URL: `/api/v1/auth/register` 
- Method: POST 
- Description: Registers a new user. 
- Request Body:
  {
  "username": "string",
  "password": "string",
  "roles": ["USER"]
  }

- Response:
  - 201 Created

#### Login
- URL: `/api/v1/auth/login`
- Method: POST 
- Description: Authenticates a user and returns a JWT token. 
- Request Body:
    {
    "username": "string",
    "password": "string"
    }
- Response:
  - 200 OK
  - Body:
    {
    "success": true,
    "data": {
    "token": "string",
    "message": "Authentication succeeded"
     }
    }

#### Logout
- URL: `/api/v1/auth/logout`
- Method: POST 
- Description: Blacklists a user's JWT token and returns no content. 
- Headers:
    - `Authorization: Bearer <token>`
- Response:
    - 204 No Content


### User Endpoints
#### Get User Information
- URL: `/api/v1/user`
- Method: GET 
- Description: Returns a greeting message. 
- Headers:
  - `Authorization: Bearer <token>`
- Response:
  - 200 OK

### Admin Endpoint
- URL: `/api/v1/user/admin`
- Method: GET 
- Description: Returns a greeting message for admin users. 
- Headers:
  - `Authorization: Bearer <token>`
- Response:
  - 200 OK 
  - 403 Forbidden (for non-admin users)

## Classes Overview
### Configuration Classes

#### `BeanConfig`
Provides the configuration for password encoding.

#### `RsaKeyProperties`
Holds the RSA key properties used for signing JWT tokens.

#### `SecurityConfig`
Configures the security filter chain and defines the security policies.

### Filter Classes
#### `CustomUsernamePasswordAuthenticationFilter`
Handles user authentication by validating username and password.

#### `CustomAuthorizationFilter`
Handles user authorization by validating user's access token.

### Manager and Provider Classes
#### `CustomAuthenticationManager`
Manages authentication by delegating to the appropriate provider.

#### `CustomAuthenticationProvider`
Authenticates users by comparing provided credentials with stored user details.

### Service Classes
#### `CustomUserDetailsService`
Loads user-specific data.

### Model Classes
#### `UserDetailsImpl`
Represents user details for authentication.

### Testing
The project includes unit and integration tests for the authentication and user management functionalities.