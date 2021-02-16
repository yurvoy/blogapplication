package be.intecbrussel.blogapplication.controller;

import be.intecbrussel.blogapplication.model.Post;
import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.service.PostService;
import be.intecbrussel.blogapplication.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RequestMapping("/{user_id}/posts")
@Slf4j
@Controller
public class PostController {

    private final String CREATE_POST = "posts/createPost.html";
    private final String UPDATE_POST = "posts/updatePost.html";
    private final PostService postService;


    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/newpost")
    public String newPost(Model model){

        model.addAttribute("post", Post.builder().build());

        return "{user_id}/" +CREATE_POST;
    }



    @PostMapping("/new")
    public String processNewForm(@Valid Post post, BindingResult result){

        if(result.hasErrors()){
            return CREATE_POST;
        }else{
            Post savedPost = postService.save(post);
            return UPDATE_POST;
        }
    }

    @GetMapping("/{userId}/post/edit")
    public String updatePost(@PathVariable Long postId, Model model, Principal principal) {

        model.addAttribute("postUpdate", postService.findById(postId));
        if(principal != null) {
            return UPDATE_POST;
        }
        return CREATE_POST;
    }

    @PostMapping("/{userId}/post/edit")
    public String processUpdatePost(@Valid @ModelAttribute("post") Post post, BindingResult result, @PathVariable User userId){


        if(result.hasErrors()){
            return CREATE_POST;
        }else{
            post.setUser(userId);
            Post savedPost = postService.save(post);
            return "redirect:/users/post" + savedPost.getUser();
        }
    }





}
