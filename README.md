# User Management API

A Spring Boot user management application with a browser interface and REST API. It demonstrates Spring Data JPA, Hibernate, validation, BCrypt password hashing, and role-based authorization.

## Features

- User registration and sign-in
- Profile and password updates
- Admin user directory with create, update, and delete actions
- USER and ADMIN roles enforced by Spring Security
- MySQL persistence through Spring Data JPA and Hibernate
- Browser interface served by Spring Boot

## Technology

- Java 17+
- Spring Boot 3.5
- Spring Web
- Spring Data JPA and Hibernate
- Spring Security
- Bean Validation
- MySQL

## Run locally

1. Create the database in MySQL:

   ```sql
   CREATE DATABASE user_management_api_clean;
   ```

2. Open the project folder containing `pom.xml` in IntelliJ IDEA. Use JDK 17 or newer and wait for Maven to load.

3. In **Run → Edit Configurations**, add these environment variables to the `UserManagementApplication` run configuration:

   ```text
   DB_USERNAME=root
   DB_PASSWORD=your-local-MySQL-password
   ```

   The application reads these values from the environment; credentials are not stored in the project.

4. Run `UserManagementApplication`.

5. Open the browser interface at [http://localhost:8081/](http://localhost:8081/).

## Create an admin account

Registration creates a USER account. To grant ADMIN access to your own local account, run this in MySQL Workbench after registering:

```sql
UPDATE user_management_api_clean.users
SET user_role = 'ADMIN'
WHERE username = 'your-email@example.com';
```

Sign out and sign back in for the updated role to appear in the browser interface.

## API examples

Register an account from IntelliJ Terminal using PowerShell:

```powershell
$body = '{"firstName":"Demo","lastName":"User","username":"demo@example.com","password":"ChangeMe123!"}'
Invoke-RestMethod -Method Post -Uri "http://localhost:8081/api/auth/register" -ContentType "application/json" -Body $body
```

Test an authenticated admin request:

```powershell
$pair = "demo@example.com:ChangeMe123!"
$basic = [Convert]::ToBase64String([Text.Encoding]::ASCII.GetBytes($pair))
$headers = @{ Authorization = "Basic $basic" }
Invoke-RestMethod -Method Get -Uri "http://localhost:8081/api/users" -Headers $headers
```

## Access rules

| Endpoint | Access |
|---|---|
| `POST /api/auth/register` | Public |
| `POST /api/auth/login` | Authenticated |
| `GET /api/auth/me` | Authenticated |
| `GET/PUT /api/users/me` | Authenticated |
| `PUT /api/users/me/password` | Authenticated |
| `/api/users` and `/api/users/{id}` | ADMIN |

The application uses HTTP Basic authentication. The browser sends the signed-in user’s credentials with protected API requests. The login endpoint verifies the credentials; it does not issue a token.

Passwords are stored as BCrypt hashes and are not included in API responses.

## Password-reset demo warning

The current demo also has a username-only password-reset flow that does not verify account ownership. Anyone who knows a username could change that account’s password. **Remove or replace this flow with verified one-time reset tokens before publishing or deploying the application.**

This project is intended for local learning and interview demonstration. Use HTTPS and a properly verified password-reset flow for a deployed application.
