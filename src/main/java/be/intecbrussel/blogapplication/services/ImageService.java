package be.intecbrussel.blogapplication.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface ImageService {
    void saveImageFile(Long userId, MultipartFile file);
}
