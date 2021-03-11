package be.intecbrussel.blogapplication.controllers;

import be.intecbrussel.blogapplication.model.Comment;
import be.intecbrussel.blogapplication.model.Post;
import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.services.CommentService;
import be.intecbrussel.blogapplication.services.PostService;
import be.intecbrussel.blogapplication.services.UserService;
import be.intecbrussel.blogapplication.web_security_config.CreateCommentDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

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
            CreateCommentDto comment, BindingResult result) {

        Post post = postService.findById(postId);
        User user = userService.findById(userId);
        if (result.hasErrors() || user == null || post == null) {
            return "user/frontpage";
        }

        commentService.saveComment(post.getId(), user.getId(), comment);

        return "redirect:/index";
    }

    @PostMapping("user/{postId}/{userId}/profileComment")
    public String createNewComment(@PathVariable Long postId, @PathVariable Long userId, @Valid @ModelAttribute("comment")
            CreateCommentDto comment, BindingResult result) {

        Post post = postService.findById(postId);
        User user = userService.findById(userId);
        if (result.hasErrors() || user == null || post == null) {
            return "user/profile";
        }

        commentService.saveComment(post.getId(), user.getId(), comment);

        return "redirect:/user/" + userId + "/profile";
    }

    @PostMapping("/error")
    public String error() {
        return "redirect:/";
    }
}