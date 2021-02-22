package be.intecbrussel.blogapplication.controllers;

import be.intecbrussel.blogapplication.services.UserService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ForgotPasswordControllerTest {

    @Mock
    UserService userService;
    @Mock
    JavaMailSender mailSender;

    ForgotPasswordController forgotPasswordController;

    @Before
    public void setUp() throws Exception {
        forgotPasswordController = new ForgotPasswordController(mailSender, userService);
    }

    @Test
    public void showForgotPasswordForm() throws Exception {

        String viewName = forgotPasswordController.showForgotPasswordForm();
        assertEquals("forgotPassword", viewName);
    }
}