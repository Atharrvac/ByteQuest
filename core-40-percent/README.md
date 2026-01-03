# Core 40% - Secure Online Voting System

This directory contains the core 40% of the Secure Online Voting System, focusing on the essential functionality required for the voting process, user authentication, and blockchain integration.

## Core Components

### 1. Application Entry Point
- `SpringBootFormApplication.java`: Main application class that initializes the Spring Boot application and sets up the admin user.

### 2. Security Configuration
- `mySecurityConfig.java`: Configures Spring Security, authentication, and authorization rules.
- `CustomUserDetailsService.java`: Custom user details service for authentication.

### 3. Core Models
- `User.java`: Represents a user in the system.
- `Block.java`: Implements the blockchain block structure.
- `Votedata.java`: Stores voting data in the blockchain.

### 4. Repositories
- `UserRepo.java`: Data access layer for User entities.
- `VoteRepo.java`: Handles voting data and blockchain operations.

### 5. Controllers
- `HomeController.java`: Handles home page and basic navigation.
- `VoteController.java`: Manages the voting process and blockchain operations.

### 6. Utilities
- `SHA256.java`: Provides hashing functionality for the blockchain.

## Getting Started

### Prerequisites
- Java 8 or later
- Maven 3.6.0 or later
- H2 Database (embedded)

### Running the Application

1. Navigate to the project root directory:
   ```bash
   cd core-40-percent
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:4000`.

## Default Admin Credentials
- **Username**: admin
- **Password**: 1234

## Database Access
- H2 Console: `http://localhost:4000/h2-console`
- JDBC URL: `jdbc:h2:mem:collegeproject`
- Username: `sa`
- Password: (leave empty)

## Notes
- This is a simplified version containing approximately 40% of the original codebase.
- Focuses on core voting functionality and blockchain integration.
- Additional features like face recognition and advanced reporting are not included in this core version.
