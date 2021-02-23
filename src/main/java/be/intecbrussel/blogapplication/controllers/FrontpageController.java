package be.intecbrussel.blogapplication.controllers;

import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.service.PostService;
import be.intecbrussel.blogapplication.services.UserService;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
public class FrontpageController implements ErrorController {

    private final UserService userService;
    private final PostService postService;


    public FrontpageController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @GetMapping("/user/frontpage")
    public String showFrontPage(@PathVariable Long userId, Model model) {

        User user = userService.findById(userId);
        model.addAttribute("view", "user/frontpage");
        model.addAttribute("user", user);
        return "/user/frontpage";
    }

    @RequestMapping("/frontpage")
    public String root(Principal principal, Model model) {

        if (principal == null) {
            return "login";
        }
        User user = userService.findByEmail(principal.getName());

        model.addAttribute("user", user);


        return "frontpage";
    }

    @GetMapping({"frontpage/login"})
    public String login(Model model) {
        return "login";
    }

    @GetMapping("frontpage/user")
    public String userIndex() {
        return "user/index";
    }

    @RequestMapping("frontpage/error")
    public String handleError() {
        return "redirect:/";
    }

    @Override
    public String getErrorPath() {
        return null;
    }


}
