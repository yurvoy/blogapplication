package be.intecbrussel.blogapplication.services;

import org.springframework.web.multipart.MultipartFile;


public interface PostService {
    void savePost(Long userId, String postTitle, String postText);
}

