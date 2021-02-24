package be.intecbrussel.blogapplication.controllers;

import be.intecbrussel.blogapplication.model.Post;
import be.intecbrussel.blogapplication.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PostController {

    private final UserService userService;

    public PostController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("post")
    public Post userRegistrationDto() {
        return new Post();
    }

    @GetMapping("user/{userId}/createPost")
    public String showUploadForm(@PathVariable String userId, Model model){

        model.addAttribute("user", userService.findById(Long.valueOf(userId)));

        return "user/createPost";
    }

}
