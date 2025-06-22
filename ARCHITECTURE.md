# PacMan Game - MVC Architecture

This document describes the Model-View-Controller (MVC) architecture implemented in the PacMan Game project.

## Architecture Overview

The project follows a clean MVC pattern with additional service layers for better separation of concerns:

```
com.pacman.game/
├── controller/          # Controllers - Handle user input and coordinate between view and service
├── model/              # Models - Data entities
├── view/               # Views - User interface components
├── service/            # Services - Business logic layer
├── dao/                # Data Access Objects - Database operations
├── database/           # Database connection management
└── test/               # Test classes
```

## Layer Responsibilities

### 1. Controllers (`controller/`)
- **Purpose**: Handle user input and coordinate between view and service layers
- **Responsibilities**:
  - Receive user input from views
  - Validate input (basic validation)
  - Call appropriate service methods
  - Handle service responses
  - Update views with results
  - Show user messages (success/error)

**Classes**:
- `RegistrationController`: Handles user registration
- `LoginController`: Handles user authentication

### 2. Services (`service/`)
- **Purpose**: Contain business logic and coordinate between controllers and DAOs
- **Responsibilities**:
  - Implement business rules
  - Coordinate data operations
  - Handle complex validation
  - Manage transactions
  - Provide clean interfaces to controllers

**Classes**:
- `UserService`: User-related business logic
- `ValidationService`: Input validation logic
- `DatabaseService`: Database connection management

### 3. Models (`model/`)
- **Purpose**: Represent data entities
- **Responsibilities**:
  - Define data structure
  - Provide getters/setters
  - Handle data validation at entity level

**Classes**:
- `User`: User entity with properties and methods

### 4. Views (`view/`)
- **Purpose**: User interface components
- **Responsibilities**:
  - Display data to users
  - Capture user input
  - Handle UI events
  - Call controller methods
  - Update UI based on controller responses

**Classes**:
- `RegistrationView`: User registration form
- `PacmanDialog`: Dialog components

### 5. DAOs (`dao/`)
- **Purpose**: Data Access Objects for database operations
- **Responsibilities**:
  - Execute database queries
  - Map database results to objects
  - Handle database-specific operations
  - Provide data persistence methods

**Classes**:
- `UserDAO`: User data access operations

### 6. Database (`database/`)
- **Purpose**: Database connection and configuration
- **Responsibilities**:
  - Manage database connections
  - Handle connection pooling
  - Provide database configuration
  - Handle database errors

**Classes**:
- `DbConnection`: Base database connection class
- `MySqlConnection`: MySQL-specific connection implementation

## Data Flow

1. **User Input**: User interacts with a view (e.g., clicks register button)
2. **View**: Captures input and calls controller method
3. **Controller**: Receives input, calls service methods
4. **Service**: Implements business logic, calls DAO methods
5. **DAO**: Executes database operations
6. **Response**: Results flow back through the layers to the view

## Benefits of This Architecture

1. **Separation of Concerns**: Each layer has a specific responsibility
2. **Maintainability**: Changes in one layer don't affect others
3. **Testability**: Each layer can be tested independently
4. **Reusability**: Services can be reused across different controllers
5. **Scalability**: Easy to add new features without affecting existing code

## Example Usage

```java
// In a view
RegistrationController controller = new RegistrationController();
boolean success = controller.registerUser(username, email, password, confirmPassword);

// Controller delegates to service
UserService.RegistrationResult result = userService.registerUser(username, email, password, confirmPassword);

// Service handles business logic and calls DAO
User user = new User();
user.setUsername(username);
userDAO.saveUser(user);
```

## Testing

The architecture supports comprehensive testing:
- **Unit Tests**: Test individual classes
- **Integration Tests**: Test layer interactions
- **End-to-End Tests**: Test complete user workflows

Use the `ArchitectureTest` class to verify the architecture is working correctly.

## Database Setup

Before running the application:
1. Ensure MySQL server is running
2. Create the database using `database_setup.sql`
3. Update database credentials in `DbConnection.java` if needed
4. Add MySQL JDBC driver to the classpath

## Future Enhancements

1. **Configuration Management**: Move database credentials to configuration files
2. **Logging**: Add comprehensive logging throughout the application
3. **Error Handling**: Implement centralized error handling
4. **Security**: Add password hashing and other security measures
5. **Caching**: Implement caching for frequently accessed data 