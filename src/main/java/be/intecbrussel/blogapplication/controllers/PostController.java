package be.intecbrussel.blogapplication.controllers;

import be.intecbrussel.blogapplication.model.Post;
import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.services.PostService;
import be.intecbrussel.blogapplication.services.UserService;
import be.intecbrussel.blogapplication.web_security_config.CreatePostDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;


@Controller
public class PostController {

    private final UserService userService;
    private final PostService postService;

    public PostController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @ModelAttribute("post")
    public CreatePostDto CreatePostDto() {
        return new CreatePostDto();
    }

    @GetMapping("user/{userId}/createPost")
    public String showUploadForm(@PathVariable String userId, Model model){

        User existing = userService.findById(Long.parseLong(userId));
        if (existing == null) {
            return "redirect:/index";
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

    @GetMapping("editPost/{id}")
    public String editPost(@PathVariable Long id, Model model, Principal principal){

        String user = "aUser";

        if(principal != null){
            user = principal.getName();
        }

        Post post = this.postService.findById(id);

        if(post != null){

            if(user.equals(post.getUser().getEmail())){
                model.addAttribute("post", post);
                return "user/updatePost";
            }else{
                return "403";
            }
        }else{
            return "error";
        }

    }

    @PostMapping("editPost/{id}")
    public String processUpdatePost(@PathVariable Long id, Principal principal, @ModelAttribute("post") CreatePostDto postForm){

        postService.updatePost(id, principal, postForm);
        return "redirect:/user/" + postService.findById(id).getUser().getId() + "/profile";
    }

}
