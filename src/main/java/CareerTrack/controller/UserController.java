package CareerTrack.controller;

import CareerTrack.dto.LoginRequest;
import CareerTrack.dto.LoginResponse;
import CareerTrack.entity.User;
import CareerTrack.service.JwtService;
import CareerTrack.service.UserService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    public UserController(
            UserService userService,
            JwtService jwtService) {

        this.userService = userService;
        this.jwtService = jwtService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {

        return ResponseEntity.ok(
                userService.getAllUsers()
        );
    }

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser() {

        return ResponseEntity.ok(
                userService.getUserByEmail(
                        org.springframework.security.core.context.SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getName()
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                userService.getUserById(id)
        );
    }

    @SecurityRequirements
    @PostMapping
    public ResponseEntity<User> createUser(
            @Valid @RequestBody User user) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.createUser(user));
    }

    @SecurityRequirements
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest loginRequest) {

        User user = userService.login(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );

        String token = jwtService.generateToken(
                user.getId(),
                user.getEmail()
        );

        LoginResponse response = new LoginResponse(
                "Login successful",
                user.getId(),
                user.getName(),
                user.getEmail(),
                token
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody User userDetails) {

        return ResponseEntity.ok(
                userService.updateUser(id, userDetails)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id) {

        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }
}