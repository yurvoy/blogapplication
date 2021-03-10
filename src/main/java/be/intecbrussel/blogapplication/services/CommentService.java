package be.intecbrussel.blogapplication.services;

import be.intecbrussel.blogapplication.model.Comment;
import be.intecbrussel.blogapplication.web_security_config.CreateCommentDto;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CommentService {

    Comment findById(Long id);

    Comment saveComment(Long postId, Long userId, CreateCommentDto newComment);

    List<Comment> findAll();
}