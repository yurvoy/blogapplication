package be.intecbrussel.blogapplication.controllers;

import be.intecbrussel.blogapplication.web_security_config.UserRegistrationDto;
import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.services.UserService;
import be.intecbrussel.blogapplication.web_security_config.Utility;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import org.thymeleaf.context.Context;

@Controller
public class UserRegistrationController {

    private final UserService userService;

    private final JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;


    public UserRegistrationController(UserService userService, JavaMailSender mailSender) {
        this.userService = userService;
        this.mailSender = mailSender;
    }

    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        return "registration";
    }

    @PostMapping("/registration")
    public String registerUserAccount(@ModelAttribute("user") @Valid UserRegistrationDto userDto, HttpServletRequest request,
                                      Model model, BindingResult result) {

        User existing = userService.findByEmail(userDto.getEmail());
        if (existing != null) {
            result.rejectValue("email","existingMail", "There is already an account registered with that email");
            return "registration";
        } else if (result.hasErrors()) {
            return "registration";
        }

        String email = userDto.getEmail();
        String token = RandomString.make(30);

        try {
            String verifyAccountLink = Utility.getSiteURL(request) + "/verifyAccount?token=" + token;
            System.out.println(verifyAccountLink);
            sendVerificationEmail(email, verifyAccountLink);
            model.addAttribute("message", "Email sent ! Please check your mail box.");
            userService.save(userDto, token);
        } catch (UnsupportedEncodingException | MessagingException e) {
            System.out.println("error sending mail");
            model.addAttribute("error", "Error while sending email");
        }

        return "redirect:/registration?success";
    }

    public void sendVerificationEmail(String recipientEmail, String link) throws MessagingException, UnsupportedEncodingException {

        Context context = new Context();
        context.setVariable("link",link);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        helper.setFrom("team2.intec.iot@gmail.com", "Blog application Support");
        helper.setTo(recipientEmail);
        helper.addAttachment("email.png", new ClassPathResource("static/email.png"));

        String subject = "Blogify!: Verify your account!";

        String html = templateEngine.process("user/email/verificationEmail", context);

        helper.setSubject(subject);

        helper.setText(html, true);

        mailSender.send(message);
    }


    @GetMapping("/verifyAccount")
    public String showVerifiedAccountPage(@Param(value = "token") String token, Model model) {

        User user = userService.getByVerifyAccountToken(token);

        if (user == null) {
            System.out.println("User not found");
            model.addAttribute("failed","invalidToken");
            return "/verifyAccount";
        }

        user.setAccountVerified(true);
        model.addAttribute("token", token);
        userService.save(user);
        return "verifyAccount";
    }

}