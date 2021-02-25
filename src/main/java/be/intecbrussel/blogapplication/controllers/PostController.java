package be.intecbrussel.blogapplication.controllers;

import be.intecbrussel.blogapplication.model.Post;
import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.services.PostService;
import be.intecbrussel.blogapplication.services.UserService;
import be.intecbrussel.blogapplication.web_security_config.CreatePostDto;
import be.intecbrussel.blogapplication.web_security_config.UserRegistrationDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class PostController {

    private final UserService userService;
    private final PostService postService;

    public PostController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @ModelAttribute("post")
    public CreatePostDto userRegistrationDto() {
        return new CreatePostDto();
    }

    @GetMapping("user/{userId}/createPost")
    public String showUploadForm(@PathVariable String userId, Model model){

        User existing = userService.findById(Long.parseLong(userId));
        if (existing == null) {
            return "user/index";
        }

        model.addAttribute("user", userService.findById(Long.valueOf(userId)));

        return "user/createPost";
    }

    @PostMapping("user/{userId}/createPost")
    public String createNewPost(@PathVariable String userId, @ModelAttribute("post") @Valid CreatePostDto post,
                                BindingResult result) {

        if (result.hasErrors()) {
            return "user/createPost";
        }

        postService.savePost(Long.parseLong(userId), post);
        return "redirect:/user/" + userId + "/profile";
    }
}