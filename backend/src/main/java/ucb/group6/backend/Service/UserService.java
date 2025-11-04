package ucb.group6.backend.Service;

import ucb.group6.backend.exception.BadRequestException;
import ucb.group6.backend.mapper.UserMapper;
import ucb.group6.backend.model.User;
import ucb.group6.backend.repository.UserRepository;
import ucb.group6.backend.requests.UserLoginRequestBody;
import ucb.group6.backend.requests.UserPostRequestBody;
import ucb.group6.backend.requests.UserPutRequestBody;
import ucb.group6.backend.util.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService {
    private final UserRepository repository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository repository, UserMapper userMapper, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.repository = repository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public List<User> listAll(){
        return repository.findAll();
    }

    public User findByIdOrThrowBadRequestException(long id) throws BadRequestException {
        return repository.findById(id)
                .orElseThrow(() -> new BadRequestException("User not found"));
    }

    public Optional<User> findByEmail(String email){
        return repository.findByEmail(email);
    }


    @Transactional
    public User save(UserPostRequestBody userPostRequestBody){
        User user = userMapper.toUser(userPostRequestBody);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }

    public void delete(long id) throws BadRequestException {
        repository.delete(findByIdOrThrowBadRequestException(id));
    }

    public void update(UserPutRequestBody userPutRequestBody) throws BadRequestException {
        User savedUser = findByIdOrThrowBadRequestException(userPutRequestBody.getId());

        if (userPutRequestBody.getName() != null && !userPutRequestBody.getName().isBlank()) {
            savedUser.setName(userPutRequestBody.getName());
        }

        if (userPutRequestBody.getEmail() != null && !userPutRequestBody.getEmail().isBlank()) {
            savedUser.setEmail(userPutRequestBody.getEmail());
        }

        if (userPutRequestBody.getPassword() != null && !userPutRequestBody.getPassword().isBlank()) {
            savedUser.setPassword(passwordEncoder.encode(userPutRequestBody.getPassword()));
        }

        repository.save(savedUser);
    }

    public User register(UserPostRequestBody userPostRequestBody) throws BadRequestException {
        User user = new User();
        user.setName(userPostRequestBody.getName());
        user.setEmail(userPostRequestBody.getEmail());
        user.setPassword(userPostRequestBody.getPassword());

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email cannot be null.");
        }

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password cannot be null or empty");
        }

        Optional<User> existingUser = repository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new BadRequestException("Email already used");
        }

        Pattern passwordPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$");
        if (!passwordPattern.matcher(user.getPassword()).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, and one digit");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }


    public String login(UserLoginRequestBody userLoginRequestBody) throws BadRequestException {
        Optional<User> searchedUser = repository.findByEmail(userLoginRequestBody.getEmail());

        if (searchedUser.isEmpty() ||
                !passwordEncoder.matches(userLoginRequestBody.getPassword(), searchedUser.get().getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        return jwtUtil.generateToken(searchedUser.get().getEmail());
    }
}