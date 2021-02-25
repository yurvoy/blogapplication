package be.intecbrussel.blogapplication.controllers;

import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.services.UserService;
import be.intecbrussel.blogapplication.web_security_config.UserRegistrationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.springframework.validation.*;


import static org.hamcrest.Matchers.is;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserRegistrationControllerTest {

    @MockBean
    private BindingResult mockBindingResult;

    @Mock
    private UserService userService;

    @Mock
    private JdbcTemplate jdbcTemplateMock;

    @InjectMocks
    private UserRegistrationController userRegistrationController;

    UserRegistrationDto user;


    User user1;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        userRegistrationController = new UserRegistrationController(userService);
        mockMvc = MockMvcBuilders
                .standaloneSetup(userRegistrationController)
                .build();

        user = new UserRegistrationDto();
        user.setEmail("foofoo@gmail.com");
        user.setBirthday("20/07/1995");
        user.setPassword("foooo2");
        user.setConfirmPassword("foooo2");
        user.setFirstName("foo");
        user.setLastName("foo");
        user.setGender("male");
        user.setTerms(true);

        mockBindingResult = mock(BindingResult.class);
    }

    @Test
    void simpleRegistration() throws Exception {
        when(mockBindingResult.hasErrors()).thenReturn(false);
        String registered = userRegistrationController.registerUserAccount(user, mockBindingResult);
        assertThat(registered, is("redirect:/registration?success"));
    }


    @Test
    void shouldNotRegisterExistingUser() throws Exception {
        user1 = User.builder().id(1L).email("foofoo@gmail.com").build();

        when(userService.findByEmail("foofoo@gmail.com")).thenReturn(user1);

        String registered = userRegistrationController.registerUserAccount(user, mockBindingResult);
        assertThat(registered, is("redirect:/registration?success"));

    }

    @Test
    void shouldStayOnRegistrationPageIfBindingErrors() throws Exception {
        when(mockBindingResult.hasErrors()).thenReturn(true);

        String registered = userRegistrationController.registerUserAccount(user, mockBindingResult);

        assertThat(registered, is("registration"));
    }

}