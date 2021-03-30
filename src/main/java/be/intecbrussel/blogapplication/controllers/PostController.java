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
import javax.servlet.http.HttpServletRequest;
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
            return "redirect:/";
        }
        postService.savePost(userId, post);

        System.out.println(pageIndicator);
        if (pageIndicator.equals("profile")){
            return "redirect:/user/" + userId + "/profile";
        } else {
            return "redirect:/";
        }

    }

    @GetMapping("/user/{postId}")
    public String singlePost(@PathVariable Long postId, Model model){

        Post post= postService.findById(postId);
        User user = this.userService.getLoggedInUser();
        User postOwner = post.getUser();

        model.addAttribute("postOwner", postOwner);
        model.addAttribute("post", post);
        model.addAttribute("user", user);


        return "user/post";
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
    public String likePost(@PathVariable Long id, Principal principal, HttpServletRequest request){

        if(principal != null){
            postService.likePost(id, principal);
        }
        return "redirect:" + request.getHeader("Referer");
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
                return "user/deletePreview";
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

    @GetMapping("/user/{userId}/reviewPosts")
    public String reviewPosts(@PathVariable Long userId, Model model, Principal principal){

        User existing = userService.findById(userId);
        User visitor = userService.findByEmail(principal.getName());
        if (existing == null || existing != visitor) {
            return "redirect:/index";
        }

        model.addAttribute("user", userService.findById(userId));
        return "user/configuration/reviewPosts";
    }

    @GetMapping("/search/{tag}")
    public String tagSearch(@PathVariable String tag, Model model, Principal principal){

        List<Post> tagList = postService.findTags(tag);
        model.addAttribute("tags", tagList);
        model.addAttribute("tagList", tagList);
        model.addAttribute("tag", tag);


        if(principal != null) {
            User user = userService.findByEmail(principal.getName());
            model.addAttribute("user", userService.findById(user.getId()));
        }

        if(tagList == null){
            return "searchNotFound";
        }
        return "user/configuration/searchTag";
    }

}
