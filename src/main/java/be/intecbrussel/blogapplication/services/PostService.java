package be.intecbrussel.blogapplication.services;

import be.intecbrussel.blogapplication.model.Post;
import be.intecbrussel.blogapplication.web_security_config.CreatePostDto;

import java.util.List;

public interface PostService {

    Post findById(Long id);

    Post savePost(Long userId, CreatePostDto newPost);

    List<Post> findAll();
}

