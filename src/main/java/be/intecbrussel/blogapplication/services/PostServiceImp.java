package be.intecbrussel.blogapplication.services;

import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.repositories.PostRepository;
import be.intecbrussel.blogapplication.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class PostServiceImp implements PostService{

    private final PostRepository postRepository;
    private final UserService userService;

    public PostServiceImp(UserService userService, PostRepository postRepository) {
        this.userService = userService;
        this.postRepository = postRepository;
    }

    @Override
    public void savePost(Long userId, String postTitle, String postText) {
        User user = userService.findById(userId);
    }
}
