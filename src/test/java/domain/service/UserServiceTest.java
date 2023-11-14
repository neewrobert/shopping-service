package domain.service;

import com.neewrobert.shopping.domain.exception.UserAlreadyExistsException;
import com.neewrobert.shopping.domain.port.UserRepository;
import com.neewrobert.shopping.domain.service.UserService;
import com.neewrobert.shopping.infrastructure.web.dto.UserDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static utils.CommonMethods.getUser;

@SpringBootTest(classes = {UserService.class})
class UserServiceTest {


    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private UserService userService;

    @BeforeEach
    public void setUp() {
        userService = new UserService(passwordEncoder, userRepository);
    }

    @Test
    void shouldRegisterNewUser() {

        when(userRepository.save(any())).thenReturn(getUser());
        when(userRepository.existsByEmail(any(String.class))).thenReturn(false);
        var userDTO = getUserDTO();
        var registeredUser = userService.registerNewUser(userDTO);
        Assertions.assertEquals(registeredUser.email(), userDTO.email());

    }

    @Test
    void shouldThrowUserAlreadyExistsException() {

        when(userRepository.existsByEmail(any(String.class))).thenReturn(true);
        var userDTO = getUserDTO();
        Assertions.assertThrows(UserAlreadyExistsException.class, () -> userService.registerNewUser(userDTO));
    }

    private UserDTO getUserDTO() {
        return new UserDTO(
                "John Doe",
                "johndoe@example.com",
                "secret",
                null,
                null);
    }
}
