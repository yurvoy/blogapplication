package be.intecbrussel.blogapplication.controllers;

import be.intecbrussel.blogapplication.model.Post;
import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.services.OAuth2Service;
import be.intecbrussel.blogapplication.services.PostService;
import be.intecbrussel.blogapplication.services.UserService;
import be.intecbrussel.blogapplication.web_security_config.CreateCommentDto;
import be.intecbrussel.blogapplication.web_security_config.CreatePostDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.text.Collator;
import java.util.*;
import java.util.function.Function;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Collections.reverseOrder;
import static java.util.Map.Entry.comparingByKey;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;

@Controller
public class HomeController {

    private final UserService userService;
    private final PostService postService;

    public HomeController(UserService userService, PostService postService, OAuth2Service oAuth2Service) {
        this.userService = userService;
        this.postService = postService;
    }

    @RequestMapping({"","/", "/index"})
    public String root(Principal principal, Model model) {

        List<Post> lastPosts = postService.findPosts();
        model.addAttribute("posts", lastPosts);

        List<Post> popularPosts = postService.findPopularPosts();
        model.addAttribute("popular", popularPosts);

        List<String> topTenTags = postService.findTopTenTags();
        model.addAttribute("topTags", topTenTags);

        if (principal == null){
            return "user/frontpage";
        }

        User user = userService.findByEmail(principal.getName());

        model.addAttribute("user", user);

        return "user/frontpage";
    }

    @GetMapping({"/user/{userId}/frontpage"})
    public String showLoggedinUserFrontPage(@PathVariable Long userId, Model model) {

        model.addAttribute("user", userService.findById(userId));
        return "user/frontpage";
    }


    @GetMapping({"/login"})
    public String login(Model model) {
        return "login";
    }


    @ModelAttribute("comment")
    public CreateCommentDto CreateComemntDto() {
        return new CreateCommentDto();
    }

    @ModelAttribute("post")
    public CreatePostDto CreatePostDto() {
        return new CreatePostDto();
    }

    @RequestMapping("/404")
    public String notFoundError(){

        return "404";
    }


}
