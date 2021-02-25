package be.intecbrussel.blogapplication.controllers;


import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.services.UserService;
import be.intecbrussel.blogapplication.web_security_config.UserRegistrationDto;
import be.intecbrussel.blogapplication.web_security_config.WebConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class UserRegistrationControllerTest {

    @MockBean
    private BindingResult mockBindingResult;

    @Mock
    private UserService userService;

    @InjectMocks
    private WebConfig webConfig;



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
                .setViewResolvers(webConfig.viewResolver())
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
    public void showRegistrationFormTest() throws Exception {

        mockMvc.perform(get("/registration"))
                .andExpect(status().isOk());
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

        assertThat(registered, is("registration"));


    }

    @Test
    void shouldStayOnRegistrationPageIfBindingErrors() throws Exception {
        when(mockBindingResult.hasErrors()).thenReturn(true);

        String registered = userRegistrationController.registerUserAccount(user, mockBindingResult);

        assertThat(registered, is("registration"));
    }

}