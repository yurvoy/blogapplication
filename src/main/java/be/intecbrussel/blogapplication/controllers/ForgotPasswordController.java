package be.intecbrussel.blogapplication.controllers;

import be.intecbrussel.blogapplication.model.ConfirmationToken;
import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.repositories.ConfirmationTokenRepository;
import be.intecbrussel.blogapplication.services.EmailSenderService;
import be.intecbrussel.blogapplication.services.UserService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ForgotPasswordController {

    private final UserService userService;

    private final ConfirmationTokenRepository confirmationTokenRepository;

    private final EmailSenderService emailSenderService;

    public ForgotPasswordController(UserService userService, ConfirmationTokenRepository confirmationTokenRepository, EmailSenderService emailSenderService) {
        this.userService = userService;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.emailSenderService = emailSenderService;
    }

    @GetMapping(value = "/forgotPassword")
    public ModelAndView displayForgotPasswordPage() {
        return new ModelAndView("forgotPassword");
    }

    // Process form submission from forgotPassword page
    @PostMapping(value = "/forgotPassword")
    public ModelAndView processForgotPasswordForm(ModelAndView modelAndView, @RequestParam("email") String userEmail, HttpServletRequest request) {

        // Lookup user in database by e-mail
        User existing = userService.findByEmail(userEmail);

        if (existing != null) {
            modelAndView.addObject("errorMessage", "We didn't find an account for that e-mail address.");
        } else {
            // Generate random 36-character string token for reset password
            ConfirmationToken confirmationToken = new ConfirmationToken(existing);

            // Save token to database
            confirmationTokenRepository.save(confirmationToken);

            String appUrl = request.getScheme() + "://" + request.getServerName();

            // Email message
            SimpleMailMessage passwordResetEmail = new SimpleMailMessage();
            passwordResetEmail.setFrom("support@demo.com");
            passwordResetEmail.setTo(existing.getEmail());
            passwordResetEmail.setSubject("Password Reset Request");
            passwordResetEmail.setText("To reset your password, click the link below:\n" + appUrl
                    + "/reset?token=" + confirmationToken.getConfirmationToken());

            emailSenderService.sendEmail(passwordResetEmail);

            // Add success message to view
            modelAndView.addObject("successMessage", "A password reset link has been sent to " + userEmail);
        }

        modelAndView.setViewName("forgotPassword");
        return modelAndView;
    }


    @GetMapping(value = "/reset")
    public ModelAndView displayResetPasswordPage() {
        return new ModelAndView("resetPassword");
    }
}
