# LinkShrinky Backend

This is the Spring Boot backend API for the LinkShrinky URL shortener service. It handles URL redirection, click analytics generation, and user authentication. 

The application is structured as a **modular monolith**. 

## Tech Stack
*   **Language:** Java
*   **Framework:** Spring Boot
*   **Security:** Spring Security with JWT
*   **Hosting:** Azure App Service

## Core Features
*   **URL Shortening:** Generates 8-character aliases for long URLs.
*   **Click Analytics:** Tracks usage statistics filtered by date ranges.
*   **Authentication:** Secures endpoints using stateless JWT tokens.
*   **CORS Management:** Restricts API access to authorized frontend domains.

## Environment Variables
Create an `application.properties` or `.env` file in your local environment.

```properties
# Server
server.port=8080

# CORS Firewall (Set to exact frontend domain in production)
FRONTEND_URL=http://localhost:5173

# Database (Update with your credentials)
spring.datasource.url=jdbc:mysql://localhost:3306/linkshrinky
spring.datasource.username=root
spring.datasource.password=yourpassword

# JWT
jwt.secret=generate-a-secure-base64-secret-key-here
jwt.expiration=86400000
```

## API Reference

### Auth
*   `POST /api/auth/public/login`
    *   Body: `{ "username": "user", "password": "password" }`
    *   Returns: JWT Token.

### URLs
*   `POST /api/urls/shorten`
    *   Headers: `Authorization: Bearer <token>`
    *   Body: `{ "originalUrl": "[https://example.com](https://example.com)" }`
    *   Returns: Shortened URL object.

*   `GET /api/urls/stats/{shortUrl}?startDate={isoDate}&endDate={isoDate}`
    *   Headers: `Authorization: Bearer <token>`
    *   Returns: Array of click event objects.

## Local Development Setup

1.  Clone the repository.
    ```bash
    git clone https://github.com/FaizanAhmed2707/URLShortener-Demo.git
    cd URLShortener-Demo
    ```
2.  Start your local database.
3.  Build and run the Spring Boot application using Maven.
    ```bash
    mvn clean install
    mvn spring-boot:run
    ```
4.  The server will start on `http://localhost:8080`.

## Production Deployment (Azure)

This API is configured for deployment on Azure App Service. 

1. Push your code to the `main` branch. GitHub Actions will handle the build and deployment to Azure.
2. Ensure the `FRONTEND_URL` environment variable in the Azure Portal configuration exactly matches your custom frontend domain (e.g., `[https://lshrink.fzdev.in](https://lshrink.fzdev.in)`) to prevent strict CORS blocks.
3. Keep the Azure CORS firewall settings synchronized with the `FRONTEND_URL` variable.

---
**Author:** Syed Faizan Ahmed

Ask for the frontend README when you are ready.
```
