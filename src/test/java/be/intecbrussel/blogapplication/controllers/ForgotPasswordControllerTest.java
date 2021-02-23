package be.intecbrussel.blogapplication.controllers;

import be.intecbrussel.blogapplication.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
public class ForgotPasswordControllerTest {

    @Mock
    UserService userService;
    @Mock
    JavaMailSender mailSender;

    MockMvc mockMvc;

    @InjectMocks
    ForgotPasswordController forgotPasswordController;

    @BeforeEach
    public void setUp() {
        forgotPasswordController = new ForgotPasswordController(mailSender, userService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(forgotPasswordController)
                .build();
    }

    @Test
    public void showForgotPasswordForm() throws Exception {

        mockMvc.perform(get("/forgotPassword"))
                .andExpect(status().isOk())
                .andExpect(view().name("forgotPassword"));
    }

}