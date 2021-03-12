package be.intecbrussel.blogapplication.controllers;

import be.intecbrussel.blogapplication.model.Post;
import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.services.PostService;
import be.intecbrussel.blogapplication.services.UserService;
import be.intecbrussel.blogapplication.web_security_config.CreatePostDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Slf4j
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

    @PostMapping("user/{userId}/createPost/{pageIndicator}")
    public String createNewPost(@PathVariable Long userId, @PathVariable String pageIndicator, @ModelAttribute("post") @Valid CreatePostDto post,
                                BindingResult result) {

        if (result.hasErrors()) {
            return "user/createPost";
        }
        postService.savePost(userId, post);

        System.out.println(pageIndicator);
        if (pageIndicator.equals("profile")){
            return "redirect:/user/" + userId + "/profile";
        } else {
            return "redirect:/";
        }

    }

    @PostMapping("user/{userId}/createPost")
    public String createNewPost(@PathVariable Long userId, @ModelAttribute("post") @Valid CreatePostDto post,
                                BindingResult result) {

        if (result.hasErrors()) {
            return "user/createPost";
        }

        postService.savePost(userId, post);
        return "redirect:/user/" + userId + "/profile";
    }

    @GetMapping("editPost/{id}")
    public String editPost(@PathVariable Long id, Model model, Principal principal){

        Post post = this.postService.findById(id);

        User postUser = post.getUser();
        User visitor =  userService.findByEmail(principal.getName());
        if (postUser != visitor) {
            return "redirect:/index";
        }

        String user = "aUser";

        if(principal != null){
            user = principal.getName();
        }

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


    @GetMapping("likePost/{id}")
    public String likePost(@PathVariable Long id, Principal principal){

        if(principal != null){
            postService.likePost(id, principal);
        }
        return "redirect:/index";
    }

    @GetMapping("likeOwnPost/{id}")
    public String likeOwnPost(@PathVariable Long id, Principal principal){
        Post post = postService.findById(id);
        Long userId = post.getUser().getId();

        if(principal != null){
            postService.likePost(id, principal);
        }
        return "redirect:/user/" + userId + "/profile";
    }

    @GetMapping("deletePost/{id}")
    public String deletePost(@PathVariable Long id, Model model, Principal principal) {

        Post post = this.postService.findById(id);

        User postUser = post.getUser();
        User visitor =  userService.findByEmail(principal.getName());
        if (postUser != visitor) {
            return "redirect:/index";
        }

        String user = "aUser";

        if (principal != null) {
            user = principal.getName();
        }

        if (post != null) {

            if (user.equals(post.getUser().getEmail())) {
                model.addAttribute("post", post);
                return "user/deletePost";
            } else {
                return "403";
            }
        } else {
            return "error";
        }
    }

    @PostMapping("deletePost/{id}")
    public String processDeletePost(@PathVariable Long id) {

        User user = new User();
        user.setId(postService.findById(id).getUser().getId());

        postService.deleteById(id);
        return "redirect:/user/" + user.getId() + "/profile";
    }


    @GetMapping("/search")
    public String postSearch(Model model, @Param("text") String text, Principal principal){

        List<Post> postList = postService.findAll(text);
        model.addAttribute("posts", postList);
        model.addAttribute("postList", postList);
        model.addAttribute("text", text);

        if(principal != null) {
            User user = userService.findByEmail(principal.getName());
            model.addAttribute("user", userService.findById(user.getId()));
        }

        if(postList.isEmpty()){
            return "searchNotFound";
        }
        return "user/frontpage";
    }

}
