package be.intecbrussel.blogapplication.controllers;


import be.intecbrussel.blogapplication.model.SecurityToken;
import be.intecbrussel.blogapplication.services.ITemplateEngine;
import be.intecbrussel.blogapplication.services.SecurityTokenService;
import be.intecbrussel.blogapplication.services.UserService;
import be.intecbrussel.blogapplication.web_security_config.UserRegistrationDto;
import be.intecbrussel.blogapplication.web_security_config.WebConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
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

    @Mock
    private SecurityTokenService securityTokenService;

    @InjectMocks
    private WebConfig webConfig;

    @Mock
    private JavaMailSender mailSender;

    @MockBean
    private HttpServletRequest request;

    @Mock
    private ITemplateEngine templateEngine;

    @InjectMocks
    private UserRegistrationController userRegistrationController;

    UserRegistrationDto user;

    SecurityToken securityToken;

    MimeMessage mockMessage;

    MimeMessageHelper mockHelper;

    MockMvc mockMvc;

    Model model;

    @BeforeEach
    void setUp() {

        userRegistrationController = new UserRegistrationController(userService, securityTokenService, mailSender, templateEngine);
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

        securityToken = new SecurityToken();
        securityToken.setId(1L);
        securityToken.setToken("ThisIsATokenTest");

        mockBindingResult = mock(BindingResult.class);
        request = mock(HttpServletRequest.class);
        mockHelper = mock(MimeMessageHelper.class);
        mockMessage = mock(MimeMessage.class);
        templateEngine = mock(ITemplateEngine.class);
        model = mock(Model.class);
       }

    @Test
    public void showRegistrationFormTest() throws Exception {

        mockMvc.perform(get("/registration"))
                .andExpect(status().isOk());
    }

    @Test
    void simpleRegistration() throws Exception {


        when(request.getRequestURL()).thenReturn(new StringBuffer("ThisIsMockingURL"));
        when(request.getServletPath()).thenReturn("ThisIsAMockingPath");
        when(mailSender.createMimeMessage()).thenReturn(mockMessage);

        String registered = userRegistrationController.registerUserAccount(user, request, model, mockBindingResult);
        assertThat(registered, is("redirect:/registration?success"));
    }

    @Test
    void shouldStayOnRegistrationPageIfBindingErrors() throws Exception {
        when(mockBindingResult.hasErrors()).thenReturn(true);

        String registered = userRegistrationController.registerUserAccount(user, request, model, mockBindingResult);

        assertThat(registered, is("registration"));
    }

}