package be.intecbrussel.blogapplication.controllers;

import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.repositories.UserRepository;
import be.intecbrussel.blogapplication.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;


@Controller
public class HomeController {

    @Autowired
    UserRepository repo;

    @RequestMapping(value = "/")
    public String root(Principal principal, Model model) {
        User user = repo.findByEmail(principal.getName());

        model.addAttribute("user", user);

        return "index";
    }

//    @GetMapping("/")
//    public String root() {
//        return "index";
//    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @GetMapping("/user")
    public String userIndex() {
        return "user/index";
    }

}
