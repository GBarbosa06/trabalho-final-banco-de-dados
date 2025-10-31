# TODO: Improve Login System Security

## Steps to Complete

- [x] Read current pom.xml to understand existing dependencies
- [x] Update pom.xml to add Spring Security, BCrypt, and JWT dependencies
- [x] Create JwtUtil.java for JWT token handling
- [x] Create SecurityConfig.java for Spring Security configuration
- [x] Update UserService.java: Implement password hashing with BCrypt, strengthen password policies, improve error handling with generic messages
- [x] Update UserController.java: Modify login endpoint to return JWT token instead of user object
- [x] Update application.properties: Add security-related configurations
- [x] Run Maven to install new dependencies
- [x] Test endpoints: Register, login, verify token-based authentication
- [x] Verify password hashing in database and error responses for security

## Progress Tracking
- Started: [Date/Time]
- Completed: None yet
