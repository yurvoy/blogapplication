package be.intecbrussel.blogapplication.services;

import be.intecbrussel.blogapplication.model.Post;
import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.web_security_config.CreatePostDto;

import java.security.Principal;
import java.util.List;

public interface PostService {

    Post findById(Long id);

    Post savePost(Long userId, CreatePostDto newPost);

    List<Post> findAll();

    void updatePost(Long postId, Principal principal, CreatePostDto postForm);

    void likePost(Long postId, Principal principal);

    List<User> findLikes (Long postId);
}

