Here's a simple `README.md` file for your Spring Boot project with user registration and password management features:

```markdown
# User Registration and Password Management System

This Spring Boot project provides user registration, email verification, password reset, and password change functionalities. The controller handles various user-related actions, including generating verification tokens, sending password reset emails, and handling password changes.

## Features

- **User Registration**: Register new users with email verification.
- **Email Verification**: Send verification tokens via email to confirm user registration.
- **Password Reset**: Allow users to reset their passwords by sending a reset token via email.
- **Change Password**: Change the user's password after validating the old password.
- **Token Expiration**: The verification and password reset tokens have an expiration time.

## Technologies Used

- **Spring Boot**: The core framework used for developing the application.
- **Spring Data JPA**: For interacting with the database.
- **Jakarta Servlet**: For handling HTTP requests.
- **Lombok**: Reduces boilerplate code for model classes.
- **MySQL**: The database used to store user data and tokens.

## Endpoints

### User Registration

- **POST** `/registration`
  - Registers a new user and sends a verification email.
  - Request Body: `UserModel` (JSON)
  - Response: `200 OK` on success.

### Verify Registration

- **GET** `/verifiyRegistrstion`
  - Verifies a user using a token sent via email.
  - Query Parameter: `token`
  - Response: `User Verified` if successful, `Invalid Token` otherwise.

### Resend Verification Token

- **GET** `/resendVarificationToken`
  - Resends the verification token for email confirmation.
  - Query Parameter: `token` (old token)
  - Response: `Varification Link sent` if successful.

### Password Reset

- **POST** `/resendPassword`
  - Sends a password reset link via email to the user.
  - Request Body: `PasswordModel` (contains email).
  - Response: Password reset URL on success.

- **POST** `/savePassword`
  - Resets the user's password using the token.
  - Query Parameter: `token`
  - Request Body: `PasswordModel` (contains new password).
  - Response: `Password Reset Successfully` on success.

### Change Password

- **POST** `/changePassword`
  - Changes the user's password after validating the old password.
  - Request Body: `PasswordModel` (contains email, oldPassword, newPassword).
  - Response: `Password Changed Successfully` if successful, or `Invalid Old Password` otherwise.

## Database Structure

- **UserEntity**: Stores user data like email, password, and verification status.
- **Varification**: Holds verification tokens linked to users.
- **PasswordResetTokenEntity**: Stores password reset tokens with expiration times.

## Running the Project

1. Clone the repository.
2. Configure the database in `application.properties`.
3. Build the project with Maven:
   ```bash
   mvn clean install
   ```
4. Run the Spring Boot application:
   ```bash
   mvn spring-boot:run
   ```
