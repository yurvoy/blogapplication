package be.intecbrussel.blogapplication.services;

import be.intecbrussel.blogapplication.model.Post;
import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.web_security_config.CreatePostDto;

import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface PostService {

    Post save(Post post);

    Post findById(Long id);

    Post savePost(Long userId, CreatePostDto newPost);

    List<Post> findAll();

    List<Post> findAll(String text);

    void updatePost(Long postId, Principal principal, CreatePostDto postForm);

    void likePost(Long postId, Principal principal);

    List<User> findLikes(Long postId);

    void deleteById(Long along);

    List<String> findTags(Long postId);

    String addVideo(String videoURL);

    List<Post> findTags(String tag);

    List<String> findTopTenTags();
}

