package be.intecbrussel.blogapplication.controllers;

import be.intecbrussel.blogapplication.model.Post;
import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.repositories.UserRepository;
import be.intecbrussel.blogapplication.services.PostService;
import be.intecbrussel.blogapplication.services.UserService;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController implements ErrorController {

    private final UserService userService;
    private final PostService postService;

    public HomeController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @RequestMapping({"","/", "/home", "/index"})
    public String root(Principal principal, Model model) {
        List<Post> posts = postService.findAll();

        if (principal == null){

            model.addAttribute("posts", posts);
            return "/user/frontpage";
        }

        User user = userService.findByEmail(principal.getName());
        model.addAttribute("user", user);

        return "index";
    }

    @GetMapping({"/user/{userId}/frontpage"})
    public String showLoggedinUserFrontPage(@PathVariable Long userId, Model model) {

        List<Post> posts = postService.findAll();
        System.out.println("User ID is " + userId);

        model.addAttribute("user", userService.findById(userId))
                .addAttribute("posts",posts);

        return "/user/frontpage";
    }


    @GetMapping({"/login", "/frontpage/login"})
    public String login(Model model) {
        return "login";
    }

    @GetMapping({"/logout"})
    public String logout(Model model) {
        return "home";
    }

    @RequestMapping("/404")
    public String notFoundError(){

        return "404";
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}
