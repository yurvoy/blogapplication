package be.intecbrussel.blogapplication.controllers;

import be.intecbrussel.blogapplication.model.Comment;
import be.intecbrussel.blogapplication.model.Post;
import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.services.CommentService;
import be.intecbrussel.blogapplication.services.PostService;
import be.intecbrussel.blogapplication.services.UserService;
import be.intecbrussel.blogapplication.web_security_config.CreateCommentDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

@Controller
public class CommentController {

    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;

    public CommentController(UserService userService, PostService postService, CommentService commentService) {
        this.userService = userService;
        this.postService = postService;
        this.commentService = commentService;
    }

    @PostMapping("user/{postId}/{userId}/createComment")
    public String createNewPost(@PathVariable Long postId, @PathVariable Long userId, @Valid @ModelAttribute("comment")
            CreateCommentDto comment, BindingResult result, HttpServletRequest request, Model model) {

        Post post = postService.findById(postId);
        User user = userService.findById(userId);
        if (result.hasErrors() || user == null || post == null) {
            return "redirect:" + request.getHeader("Referer");
        }

        commentService.saveComment(post.getId(), user.getId(), comment);



        return "redirect:" + request.getHeader("Referer");
    }

    @PostMapping("/error")
    public String error() {
        return "redirect:/";
    }


    @GetMapping("deleteComment/{id}/{pageIndicator}")
    public String deletePost(@PathVariable Long id, @PathVariable String pageIndicator, Model model, Principal principal) {

        model.addAttribute(pageIndicator);

        Comment comment = this.commentService.findById(id);

        User commentUser = comment.getUser();
        User visitor =  userService.findByEmail(principal.getName());

        if (commentUser != visitor) {
            return "redirect:/index";
        }

        String user = "aUser";

        if (principal != null) {
            user = principal.getName();
        }

        if (comment != null) {

            if (user.equals(comment.getUser().getEmail())) {
                model.addAttribute("comment", comment);
                return "user/deletePreview";
            } else {
                return "403";
            }
        } else {
            return "error";
        }
    }

    @PostMapping("deleteComment/{id}/{pageIndicator}")
    public String processDeleteComment(@PathVariable Long id, @PathVariable String pageIndicator) {

        User user = new User();
        user.setId(commentService.findById(id).getUser().getId());

        commentService.deleteById(id);

        if (pageIndicator.equals("profile")){
            return "redirect:/user/" + user.getId() + "/profile";
        } else {
            return "redirect:/";
        }

    }

}