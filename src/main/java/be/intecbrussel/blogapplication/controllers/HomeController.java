package be.intecbrussel.blogapplication.controllers;

import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.repositories.UserRepository;
import be.intecbrussel.blogapplication.services.UserService;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
public class HomeController implements ErrorController {

    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping({"","/","/index"})
    public String root(Principal principal, Model model) {
        if (principal == null){
            return "home";
        }
        User user = userService.findByEmail(principal.getName());

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

    @RequestMapping("/error")
    public String handleError() {
        return "redirect:/";
    }

    @Override
    public String getErrorPath() {
        return null;
    }


}
