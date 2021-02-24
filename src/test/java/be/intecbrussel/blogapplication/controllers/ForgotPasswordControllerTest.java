package be.intecbrussel.blogapplication.controllers;

import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.services.UserService;
import be.intecbrussel.blogapplication.web_secuity_config.WebConfig;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@ExtendWith(MockitoExtension.class)
public class ForgotPasswordControllerTest {

    @InjectMocks
    WebConfig webConfig;

    @Mock
    UserService userService;
    @Mock
    HttpServletRequest request;

    MockMvc mockMvc;

    @InjectMocks
    ForgotPasswordController forgotPasswordController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(forgotPasswordController)
                .setViewResolvers(webConfig.viewResolver())
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
        String email = request.getParameter("email");
        String token = RandomString.make(30);

        userService.updateResetPasswordToken(token, email);

    }


    @Test
    public void showResetPasswordForm() throws Exception {

        mockMvc.perform(get("/resetPassword"))
                .andExpect(status().isOk())
                .andExpect(view().name("message"));;
    }

    @Test
    public void processResetPassword() throws Exception {
        String token = request.getParameter("token");

        User user = userService.getByResetPasswordToken(token);

        mockMvc.perform(post("/resetPassword"))
                .andExpect(status().isOk())
                .andExpect(view().name("message"));;
    }

}