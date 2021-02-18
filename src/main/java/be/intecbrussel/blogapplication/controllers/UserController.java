package be.intecbrussel.blogapplication.controllers;

import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.repositories.UserRepository;
import be.intecbrussel.blogapplication.services.UserService;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;


@Controller
public class UserController implements ErrorController {


    private final UserRepository userRepository;
    private final UserService userService;

    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @RequestMapping({"","/","/index"})
    public String root(Principal principal, Model model) {
        if (principal == null){
            return "login";
        }
        User user = userRepository.findByEmail(principal.getName());

        model.addAttribute("user", user);

        return "index";
    }

    @GetMapping({"/login"})
    public String login(Model model) {
        return "login";
    }

    @GetMapping("/user")
    public String userIndex() {
        return "user/index";
    }

    @GetMapping({"users/{userId}/edit"})
    public String updateProfile(@PathVariable Long userId, Model model){

        model.addAttribute("user", userService.findById(userId));
        return "users/updateProfile.html";
    }

    @PostMapping({"users/{userId}/edit"})
    public String processUpdateProfile(@PathVariable Long userId, User user){

        userService.updateBio(userId, user.getUserBio());
        userRepository.save(user);
        return "redirect:/users/" + userId +  "/profile.html";
    }

    @RequestMapping("/error")
    public String handleError() {
        return "redirect:/";
    }

    @Override
    public String getErrorPath() {
        return null;
    }


}
