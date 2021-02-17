package be.intecbrussel.blogapplication.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    void saveImageFile(Long userId, MultipartFile file);
}
