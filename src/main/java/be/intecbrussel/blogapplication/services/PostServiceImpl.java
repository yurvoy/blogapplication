package be.intecbrussel.blogapplication.services;

import be.intecbrussel.blogapplication.model.Post;
import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.repositories.PostRepository;
import be.intecbrussel.blogapplication.repositories.UserRepository;
import be.intecbrussel.blogapplication.web_security_config.CreatePostDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    public PostServiceImpl(UserService userService, PostRepository postRepository, UserRepository userRepository) {
        this.userService = userService;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Post savePost(Long userId, CreatePostDto newPost) {
        System.out.println("this is the user Id " + userId);
        User user = userService.findById(userId);

        Post post = new Post();
        post.setPostTitle(newPost.getPostTitle());
        post.setPostText(newPost.getPostText());
        post.setPostTimeStamp(LocalDate.now());
        post.setUser(user);

        List<Post> posts = user.getPosts();
        posts.add(0,post);
        user.setPosts(posts);

        return postRepository.save(post);
    }

    @Override
    public Post findById(Long id) {
        Optional<Post> postOptional = postRepository.findById(id);
        if (!postOptional.isPresent()) {
            throw new RuntimeException("post not found");
        }
        return postOptional.get();
    }

    @Override
    public List<Post> findAll() {

        List<Post> posts = postRepository.findAll();

        if (posts.size() == 0){
            System.out.println("There are no post yet");
        }
        return posts;
    }

    @Override
    public void updatePost(Long postId, Principal principal, CreatePostDto postForm) {
        Optional<Post> post = postRepository.findById(postId);

        if(postForm.getPostTitle()!= null) {
            post.get().setPostTitle(postForm.getPostTitle());
        }
        if(postForm.getPostText() != null) {
            post.get().setPostText(postForm.getPostText());
        }

        postRepository.save(post.get());
    }


    @Override
    public void deleteById(Long userId) {

        log.debug("Deleting post: " + userId);

        Optional<User> userOptional = Optional.ofNullable(postRepository.findById(userId).get().getUser());


        if(userOptional.isPresent()){
            User user = userOptional.get();
            log.debug("found user");

            Optional<Post> postOptional = user
                    .getPosts()
                    .stream()
                    .filter(post -> post.getId().equals(userId))
                    .findFirst();

            if(postOptional.isPresent()){
                log.debug("found Post");
                Post postToDelete = postOptional.get();
                postToDelete.setPostText(null);

                user.getPosts().remove(postOptional.get());
                userRepository.save(user);
            }
        } else {
            log.debug("User Id Not found. Id:" + userId);
        }

    }


}