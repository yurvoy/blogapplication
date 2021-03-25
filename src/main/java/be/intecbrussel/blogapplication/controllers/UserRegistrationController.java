package be.intecbrussel.blogapplication.controllers;

import be.intecbrussel.blogapplication.model.SecurityToken;
import be.intecbrussel.blogapplication.services.ITemplateEngine;
import be.intecbrussel.blogapplication.services.SecurityTokenService;
import be.intecbrussel.blogapplication.web_security_config.UserRegistrationDto;
import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.services.UserService;
import be.intecbrussel.blogapplication.web_security_config.Utility;
import net.bytebuddy.utility.RandomString;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.Validator;
import java.util.Date;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

import org.thymeleaf.context.Context;

@Controller
public class UserRegistrationController {

    private final UserService userService;

    private final SecurityTokenService securityTokenService;

    private final JavaMailSender mailSender;

    private final ITemplateEngine templateEngine;

    public UserRegistrationController(UserService userService, SecurityTokenService securityTokenService, JavaMailSender mailSender, ITemplateEngine templateEngine) {
        this.userService = userService;
        this.securityTokenService = securityTokenService;
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
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
            return "registration";
        } else if (result.hasErrors()) {
            return "registration";
        }

        if (result.hasErrors()) {
            result.rejectValue("email", "existingMail", "There is already an account registered with that email");
            return "registration";
        }
            String email = userDto.getEmail();

            SecurityToken verificationToken = new SecurityToken(RandomString.make(30));

            try {
                String verifyAccountLink = Utility.getSiteURL(request) + "/verifyAccount?token=" + verificationToken.getToken();
                sendVerificationEmail(email, verifyAccountLink);
                model.addAttribute("message", "Email sent ! Please check your mail box.");

                User savedUser = userService.save(userDto);
                securityTokenService.save(verificationToken, savedUser);

            } catch (UnsupportedEncodingException | MessagingException e) {
                System.out.println("error sending mail");
                model.addAttribute("error", "Error while sending email");
            }


            return "redirect:/registration?success";

        }


        public void sendVerificationEmail (String recipientEmail, String link) throws
        MessagingException, UnsupportedEncodingException {

            Context context = new Context();
            context.setVariable("link", link);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setFrom("team2.intec.iot@gmail.com", "Blog application Support");
            helper.setTo(recipientEmail);

            String subject = "Blogify!: Verify your account!";

            String html = templateEngine.process("user/email/verificationEmail", context);

            String content = "<p>Hello,</p>"
                    + "<p>You have created a new account in our app.</p>"
                    + "<p>Click the link below to validate your account:</p>"
                    + "<p><a href=\"" + link + "\">Validate my account</a></p>"
                    + "<br>"
                    + "<p>Ignore this email if you haven't made an account with us";

            if (html == null) {
                helper.setText(content, true);
            } else {
                helper.setText(html, true);
            }

            mailSender.send(message);
        }


        @GetMapping("/verifyAccount")
        public String showVerifiedAccountPage (@Param(value = "token") String token, Model model){

            SecurityToken verificationToken = securityTokenService.getSecurityTokenByToken(token);
            if (verificationToken == null) {
                model.addAttribute("failed", "invalidToken");
                return "/verifyAccount";
            }
            System.out.println("is null");
            if (verificationToken.getExpireAt().isBefore(LocalDateTime.now())) {
                model.addAttribute("failed", "invalidToken");
                return "/verifyAccount";
            }

            User user = userService.findById(verificationToken.getUser().getId());

            if (user == null) {
                model.addAttribute("UserNotFound", "userNotFound");
                return "/verifyAccount";
            }

            if (user.getAccountVerified()) {
                model.addAttribute("alreadyVerified", "AlreadyVerified");
                return "/verifyAccount";
            }

            model.addAttribute("token", token);
            model.addAttribute("captcha", "captchaAvailable");

            return "verifyAccount";
        }

        @PostMapping("/verifyAccount/{token}")
        public String showResponseFromCaptcha (@PathVariable String token, Model model){
            SecurityToken verificationToken = securityTokenService.getSecurityTokenByToken(token);
            User user = userService.findById(verificationToken.getUser().getId());

            user.setAccountVerified(true);
            user.getSecurityTokens().remove(verificationToken);
            model.addAttribute("validated", "validated");
            userService.save(user);

            return "verifyAccount";
        }

    }

