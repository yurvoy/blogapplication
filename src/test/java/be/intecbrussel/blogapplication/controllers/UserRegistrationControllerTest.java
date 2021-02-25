package be.intecbrussel.blogapplication.controllers;


import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.services.UserService;
import be.intecbrussel.blogapplication.web.UserRegistrationDto;
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
import org.springframework.web.servlet.tags.form.ErrorsTag;

import javax.servlet.ServletException;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
class UserRegistrationControllerTest {

    @MockBean
    private BindingResult mockBindingResult;

    @Mock
    private UserService userService;


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
        assertThat(registered, is("registration"));

    }

    @Test
    void shouldStayOnRegistrationPageIfBindingErrors() throws Exception {
        when(mockBindingResult.hasErrors()).thenReturn(true);

        String registered = userRegistrationController.registerUserAccount(user, mockBindingResult);

        assertThat(registered, is("registration"));
    }

}