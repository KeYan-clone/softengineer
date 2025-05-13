# Error Handling Refactoring Report

## Changes Implemented

### 1. Moved User Exceptions to Common Module
- Created `UserException` in common module
- Updated `UserServiceImpl` to use common exception

### 2. Created Common Experiment Exception
- Created `ExperimentException` in common module with standard error codes
- Added factory methods for creating specific experiment exceptions

### 3. Enhanced GlobalExceptionHandler
- Added specific handlers for `UserException` and `ExperimentException`
- Maintained hierarchical structure with `BaseException` handling as fallback

## Architecture Improvements

### 1. Introduced Domain Service Pattern
- Created `UserDomainService` interface in core module
- Implemented `UserDomainServiceImpl` in core module
- Modified `UserServiceImpl` in user-service to use `UserDomainService`

### 2. Clear Responsibility Separation
- Core domain services (`UserDomainService`) handle entity operations and persistence
- Business services (`UserService`) handle business logic and transactions
- Common exceptions provide standardized error handling across services

## Implementation Details

### UserDomainService Interface
```java
public interface UserDomainService {
    User createUser(User user);
    Optional<User> findById(String id);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    User updateUser(User user);
    void deleteUser(String id);
    List<User> findAll();
    Page<User> findAll(Pageable pageable);
    List<User> findByRole(UserRole role);
}
```

### Common Exception Example
```java
public class UserException extends BaseException {
    public static final int USER_NOT_FOUND = 100001;
    public static final int USERNAME_ALREADY_EXISTS = 100002;
    
    public static UserException userNotFound() {
        return new UserException(USER_NOT_FOUND, "用户不存在");
    }
}
```

## Recommendations for Additional Changes

1. **Standardize Exception Handling in All Services**:
   - Replace any remaining custom exception classes with common exceptions
   - Update all service implementations to use the appropriate common exceptions

2. **Update ExperimentService Implementation**:
   - Replace usage of `ExperimentNotFoundException` with `ExperimentException.experimentNotFound()`
   - Replace usage of `UnauthorizedAccessException` with `ExperimentException.unauthorizedAccess()`

3. **Apply Similar Pattern to Other Modules**:
   - Create domain service interfaces for other core entities
   - Implement corresponding domain service implementations
   - Refactor business services to use domain services

4. **Add Documentation**:
   - Update project documentation to reflect the new architecture
   - Add javadoc to all new interfaces and classes

5. **Testing Strategy**:
   - Unit test domain services separately from business services
   - Add integration tests for the complete flow
