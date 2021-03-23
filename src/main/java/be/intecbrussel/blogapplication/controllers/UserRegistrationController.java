package be.intecbrussel.blogapplication.controllers;

import be.intecbrussel.blogapplication.web_security_config.UserRegistrationDto;
import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.Validator;
import java.util.Date;

@Controller
@RequestMapping("/registration")
public class UserRegistrationController{

    private final UserService userService;

    public UserRegistrationController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
        return "registration";
    }

    @PostMapping
    public String registerUserAccount(@ModelAttribute("user") @Valid UserRegistrationDto userDto,
                                      BindingResult result) {

        User existing = userService.findByEmail(userDto.getEmail());

        if (existing != null) {
            //result.rejectValue("email","existingMail", "There is already an account registered with that email");
            return "registration";
        }

        if (result.hasErrors()) {
            result.rejectValue("email","existingMail", "There is already an account registered with that email");
            return "registration";
        }

        userService.save(userDto);
        return "redirect:/registration?success";
    }
}