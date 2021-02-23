package be.intecbrussel.blogapplication.controllers;

import be.intecbrussel.blogapplication.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@ExtendWith(MockitoExtension.class)
public class ForgotPasswordControllerTest {

    @Mock
    UserService userService;
    @Mock
    JavaMailSender mailSender;

    MockMvc mockMvc;

    @InjectMocks
    ForgotPasswordController forgotPasswordController;

    private ViewResolver viewResolver()
    {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();

        viewResolver.setPrefix("classpath:templates/");
        viewResolver.setSuffix(".html");

        return viewResolver;
    }

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(forgotPasswordController)
                .setViewResolvers(viewResolver())
                .build();
    }

    @Test
    public void showForgotPasswordForm() throws Exception {

        mockMvc.perform(get("/forgotPassword"))
                .andExpect(status().isOk())
                .andExpect(view().name("forgotPassword"));;
    }

    @Test
    public void processForgotPassword() throws Exception {

        mockMvc.perform(post("/forgotPassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("forgotPassword"));;
    }

}