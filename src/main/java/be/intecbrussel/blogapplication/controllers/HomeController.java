package be.intecbrussel.blogapplication.controllers;

import be.intecbrussel.blogapplication.model.Post;
import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.services.PostService;
import be.intecbrussel.blogapplication.services.UserService;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final UserService userService;
    private final PostService postService;

    public HomeController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @RequestMapping({"","/", "/index"})
    public String root(Principal principal, Model model) {

        List<Post> topTenPosts = postService.findAll();
        Collections.reverse(topTenPosts);
        topTenPosts = topTenPosts.stream().limit(10).collect(Collectors.toList());
        model.addAttribute("posts", topTenPosts);
        if (principal == null){
            return "/user/frontpage";
        }
        User user = userService.findByEmail(principal.getName());

        model.addAttribute("user", user);

        return "/user/frontpage";
    }

    @GetMapping({"/user/{userId}/frontpage"})
    public String showLoggedinUserFrontPage(@PathVariable Long userId, Model model) {

        //model.addAttribute("view", "user/frontpage");
        model.addAttribute("user", userService.findById(userId));
        return "/user/frontpage";
    }


    @GetMapping({"/login"})
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


}
