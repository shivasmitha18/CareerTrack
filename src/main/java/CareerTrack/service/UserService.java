package CareerTrack.service;

import CareerTrack.entity.User;
import CareerTrack.repository.UserRepository;
import CareerTrack.exception.InvalidCredentialsException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Get current logged-in user only
    public List<User> getAllUsers() {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User currentUser =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"));

        return List.of(currentUser);
    }

    // Get user by ID - only current user is allowed
    public User getUserById(Long id) {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User currentUser =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"));

        if (!currentUser.getId().equals(id)) {

            throw new RuntimeException(
                    "You are not allowed to access this user");
        }

        return currentUser;
    }

    // Create user
    public User createUser(User user) {

        user.setPassword(
                passwordEncoder.encode(
                        user.getPassword()
                )
        );

        return userRepository.save(user);
    }

    // Get user by email
    public User getUserByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"));
    }

    // Login
    public User login(
            String email,
            String password) {

        User user = getUserByEmail(email);

        if (!passwordEncoder.matches(
                password,
                user.getPassword())) {

            throw new InvalidCredentialsException(
                    "Invalid email or password");
        }

        return user;
    }

    // Update user
    public User updateUser(
            Long id,
            User userDetails) {

        User user = getUserById(id);

        user.setName(
                userDetails.getName()
        );

        user.setEmail(
                userDetails.getEmail()
        );

        user.setPassword(
                passwordEncoder.encode(
                        userDetails.getPassword()
                )
        );

        return userRepository.save(user);
    }

    // Delete user
    public void deleteUser(Long id) {

        User user = getUserById(id);

        userRepository.delete(user);
    }
}