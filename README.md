# 🚀 Spring Boot Security Authentication System

A **robust authentication and authorization system** for Spring Boot, leveraging modern security best practices and seamless integration with Google OAuth2.  
Empower your Java app with scalable, stateless security! 🛡️

## ✨ Features

- 🔒 **JWT Authentication**
  - Supports both *access* and *refresh* tokens for a secure flow.
  - Stateless session management using JWT.
- 🌐 **OAuth2 Integration**
  - Social sign-in with **Google** for frictionless login.
- 🏷️ **Role-Based Access Control (RBAC)**
  - Assign users to roles with specific permissions.
- 🎯 **Granular Authority**
  - Fine-grained permission system, beyond simple roles.
- 🛡️ **Security Methods**
  - Utilizes Spring Security’s powerful authentication & authorization framework.
  - Supports both declarative and programmatic access control.
- 🚦 **Route Protection**
  - Restrict endpoints based on user roles and authorities.

## 🏁 Getting Started

### 🛠️ Prerequisites

- Java **17+** ☕
- **Maven** or **Gradle** 🛠️
- Google OAuth2 Credentials *(Client ID & Secret)* 🔑

### 💻 Installation

1. **Clone the Repository**

   ```sh
   git clone https://github.com/tejasvi001/SpringSecurityAuthentication.git
   cd SpringSecurityAuthentication
   ```

2. **Configure Application Properties**

   Set the following variables in `application.yml` or `application.properties`:
   ```yaml
   spring:
     security:
       oauth2:
         client:
           registration:
             google:
               client-id: 
               client-secret: 
   jwt:
     secret: 
     access-token-expiration: 900000
     refresh-token-expiration: 2592000000
   ```

3. **Build and Run**

   ```sh
   ./mvnw spring-boot:run
   ```
   or
   ```sh
   ./gradlew bootRun
   ```

## 🔑 Authentication Overview

### 🪙 JWT Flow

- On login, user receives:
  - **Access Token:** Short-lived, used for API access.
  - **Refresh Token:** Long-lived, used to obtain new access tokens.
- All state is managed via tokens — *stateless session management*.

### 🟢 OAuth2 with Google

- Sign in with your Google account.
- Receive JWT tokens post-authentication.

## 🛡️ Authorization & RBAC

- Users are assigned to roles (e.g., **USER**, **ADMIN**).
- Each role consists of **authorities** (fine-grained permissions).
- Access is controlled at both the HTTP route and method levels:
  - Annotations like `@PreAuthorize`, `@Secured`, etc.
  - Flexible route protection via configuration.

## 📊 Example: Role & Permission Mapping

| Role  | Permissions               | Endpoints Access           |
|-------|---------------------------|----------------------------|
| USER  | `READ_PROFILE`, `EDIT_SELF` | `/user/**`, `/profile`     |
| ADMIN | all user permissions + `MANAGE_USERS` | `/admin/**`        |

_Note: Update roles and permissions in your database or configuration as needed._

## 📝 Security Annotations Sample

```java
// Only users with ADMIN role
@PreAuthorize("hasRole('ADMIN')")
// Requires MANAGE_USERS authority
@PreAuthorize("hasAuthority('MANAGE_USERS')")
```

## 🔐 Route Protection Example

```java
http
  .authorizeHttpRequests()
    .antMatchers("/admin/**").hasRole("ADMIN")
    .antMatchers("/user/**").hasAnyRole("USER", "ADMIN")
    .anyRequest().authenticated();
```

## 🔄 Refresh Token Endpoint Workflow

1. Send the **refresh token** with the request (as a cookie or in the Authorization header).
2. Get a new access token if the refresh token is valid.

## ❤️ Contributing

Contributions are welcome! Please fork the repository and submit a pull request.  
Let's make security better together! 💡

## 📄 License

This project is MIT licensed.

## 📬 Contact

For support or questions, open an issue or contact the maintainer.

> ⭐️ **Don’t forget to star the repo:**  
>  all configurations and code samples according to your actual project needs.*
