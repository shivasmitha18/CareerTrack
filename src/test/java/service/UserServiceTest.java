package CareerTrack.service;

import CareerTrack.entity.User;
import CareerTrack.repository.UserRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService userService;


    @BeforeEach
    void setUp() {

        userService =
                new UserService(
                        userRepository,
                        passwordEncoder
                );
    }


    @AfterEach
    void tearDown() {

        SecurityContextHolder.clearContext();
    }


    private void authenticateUser(String email) {

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        List.of()
                );

        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);
    }


    @Test
    void createUser_shouldCreateUserSuccessfully() {

        User user = new User();

        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("password123");


        when(userRepository.findByEmail(
                "test@example.com"
        )).thenReturn(Optional.empty());


        when(passwordEncoder.encode(
                "password123"
        )).thenReturn("encoded-password");


        when(userRepository.save(
                any(User.class)
        )).thenAnswer(invocation ->
                invocation.getArgument(0)
        );


        User result =
                userService.createUser(user);


        assertNotNull(result);

        assertEquals(
                "Test User",
                result.getName()
        );

        assertEquals(
                "test@example.com",
                result.getEmail()
        );

        assertEquals(
                "encoded-password",
                result.getPassword()
        );


        verify(userRepository)
                .findByEmail("test@example.com");

        verify(passwordEncoder)
                .encode("password123");

        verify(userRepository)
                .save(user);
    }


    @Test
    void createUser_shouldRejectDuplicateEmail() {

        User existingUser = new User();

        existingUser.setId(1L);
        existingUser.setEmail(
                "test@example.com"
        );


        User newUser = new User();

        newUser.setName("New User");
        newUser.setEmail(
                "test@example.com"
        );
        newUser.setPassword("password123");


        when(userRepository.findByEmail(
                "test@example.com"
        )).thenReturn(
                Optional.of(existingUser)
        );


        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> userService.createUser(newUser)
                );


        assertEquals(
                "Email already registered. Please use another email.",
                exception.getMessage()
        );


        verify(userRepository)
                .findByEmail("test@example.com");
    }


    @Test
    void getUserById_shouldReturnUser() {

        User user = new User();

        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");


        authenticateUser(
                "test@example.com"
        );


        when(userRepository.findByEmail(
                "test@example.com"
        )).thenReturn(
                Optional.of(user)
        );


        User result =
                userService.getUserById(1L);


        assertNotNull(result);

        assertEquals(
                1L,
                result.getId()
        );

        assertEquals(
                "Test User",
                result.getName()
        );

        assertEquals(
                "test@example.com",
                result.getEmail()
        );


        verify(userRepository)
                .findByEmail(
                        "test@example.com"
                );
    }


    @Test
    void getUserByEmail_shouldReturnUser() {

        User user = new User();

        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");


        when(userRepository.findByEmail(
                "test@example.com"
        )).thenReturn(
                Optional.of(user)
        );


        User result =
                userService.getUserByEmail(
                        "test@example.com"
                );


        assertNotNull(result);

        assertEquals(
                1L,
                result.getId()
        );

        assertEquals(
                "Test User",
                result.getName()
        );

        assertEquals(
                "test@example.com",
                result.getEmail()
        );


        verify(userRepository)
                .findByEmail(
                        "test@example.com"
                );
    }


    @Test
    void login_shouldReturnUserForValidCredentials() {

        User user = new User();

        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("encoded-password");


        when(userRepository.findByEmail(
                "test@example.com"
        )).thenReturn(
                Optional.of(user)
        );


        when(passwordEncoder.matches(
                "password123",
                "encoded-password"
        )).thenReturn(true);


        User result =
                userService.login(
                        "test@example.com",
                        "password123"
                );


        assertNotNull(result);

        assertEquals(
                "test@example.com",
                result.getEmail()
        );


        verify(userRepository)
                .findByEmail(
                        "test@example.com"
                );

        verify(passwordEncoder)
                .matches(
                        "password123",
                        "encoded-password"
                );
    }


    @Test
    void login_shouldRejectInvalidPassword() {

        User user = new User();

        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("encoded-password");


        when(userRepository.findByEmail(
                "test@example.com"
        )).thenReturn(
                Optional.of(user)
        );


        when(passwordEncoder.matches(
                "wrong-password",
                "encoded-password"
        )).thenReturn(false);


        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> userService.login(
                                "test@example.com",
                                "wrong-password"
                        )
                );


        assertNotNull(
                exception.getMessage()
        );


        verify(userRepository)
                .findByEmail(
                        "test@example.com"
                );

        verify(passwordEncoder)
                .matches(
                        "wrong-password",
                        "encoded-password"
                );
    }


    @Test
    void login_shouldRejectUnknownEmail() {

        when(userRepository.findByEmail(
                "unknown@example.com"
        )).thenReturn(
                Optional.empty()
        );


        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> userService.login(
                                "unknown@example.com",
                                "password123"
                        )
                );


        assertNotNull(
                exception.getMessage()
        );


        verify(userRepository)
                .findByEmail(
                        "unknown@example.com"
                );
    }


    @Test
    void getAllUsers_shouldReturnUsers() {

        User user = new User();

        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");


        authenticateUser(
                "test@example.com"
        );


        when(userRepository.findByEmail(
                "test@example.com"
        )).thenReturn(
                Optional.of(user)
        );


        List<User> result =
                userService.getAllUsers();


        assertNotNull(result);

        assertEquals(
                1,
                result.size()
        );

        assertEquals(
                "Test User",
                result.get(0).getName()
        );


        verify(userRepository)
                .findByEmail(
                        "test@example.com"
                );
    }
}