package be.intecbrussel.blogapplication.controller;

import be.intecbrussel.blogapplication.service.PostService;
import be.intecbrussel.blogapplication.service.UserService;
import org.springframework.stereotype.Controller;

@Controller
public class PostController {

    private final PostService postService;
    private final UserService userService;


    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }
}
