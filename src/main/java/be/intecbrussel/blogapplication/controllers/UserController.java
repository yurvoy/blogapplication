package be.intecbrussel.blogapplication.controllers;

import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;


@Controller
public class UserController {


    @Autowired
    private UserRepository userRepository;

    @RequestMapping({"","/","/index"})
    public String root(Principal principal, Model model) {
        if (principal == null){
            return "loginUser";
        }
        User user = userRepository.findByEmail(principal.getName());

        model.addAttribute("user", user);

        return "index";
    }

    @GetMapping({"/loginUser"})
    public String login(Model model) {
        return "loginUser";
    }

    @GetMapping("/user")
    public String userIndex() {
        return "user/index";
    }


}
