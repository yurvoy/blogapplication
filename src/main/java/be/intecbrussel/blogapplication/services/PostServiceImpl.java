package be.intecbrussel.blogapplication.services;

import be.intecbrussel.blogapplication.model.Post;
import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.repositories.PostRepository;
import be.intecbrussel.blogapplication.repositories.UserRepository;
import be.intecbrussel.blogapplication.web_security_config.CreatePostDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.reverseOrder;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;

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
        User user = userService.findById(userId);

        Post post = new Post();
        post.setPostTitle(newPost.getPostTitle());
        post.setPostText(newPost.getPostText());
        post.setPostTimeStamp(LocalDateTime.now(Clock.systemUTC()));
        if (newPost.getEmbedURL() != null
                && !newPost.getEmbedURL().contains("/embed/")
                && !newPost.getEmbedURL().contains("Add picture")) {
            if (newPost.getEmbedURL().contains("youtube.com/") || newPost.getEmbedURL().contains("://youtu")){
                post.setVideoURL(addVideo(newPost.getEmbedURL()));
            } else {
                post.setPictureURL(newPost.getEmbedURL());
            }
        }
        post.setTags(newPost.getTags());
        post.setUser(user);

        List<Post> posts = user.getPosts();
        posts.add(0,post);
        user.setPosts(posts);

        return postRepository.save(post);
    }

    @Override
    public Post save(Post post){
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
    public List<Post> findAll(String text) {

        if(text != null){

            return postRepository.search(text);
        }
        return postRepository.findAll();
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
        if(postForm.getTags() != null){
            post.get().setTags(postForm.getTags());
        }
        if (postForm.getEmbedURL() != null
                && !postForm.getEmbedURL().contains("/embed/")
                && !postForm.getEmbedURL().contains("Add picture")) {
            if (postForm.getEmbedURL().contains("youtube.com/") || postForm.getEmbedURL().contains("://youtu")){
                post.get().setVideoURL(addVideo(postForm.getEmbedURL()));
                post.get().setPictureURL(null);
            } else {
                post.get().setPictureURL(postForm.getEmbedURL());
                post.get().setVideoURL(null);
            }
        }

        postRepository.save(post.get());
    }

    @Override
    public void likePost(Long postId, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        Optional<Post> post = postRepository.findById(postId);
        List<User> likes = post.get().getLikes();

        boolean alreadyLiked = likes.contains(user);
        if (alreadyLiked) {
            likes.remove(user);
        } else {
            likes.add(user);
        }
        post.get().setLikes(likes);
        postRepository.save(post.get());
    }

    @Override
    public List<User> findLikes(Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        return post.get().getLikes();
    }

    @Override
    public List<Post> findTags(String tag) {

        List<Post> postOptional = null;

        if(tag != null){

            postOptional = postRepository.findAll()
                    .stream()
                    .filter(post -> post.getTags().contains(tag))
                    .collect(toList());
        }
        return postOptional;
    }

    @Override
    public List<String> findTopTenTags() {
        List<Post> postsWithTags = postRepository.findAll()
                .stream()
                .filter(post -> !post.getTags().isEmpty())
                .collect(toList());
        List<String> tags = new ArrayList<>();
        for (Post post : postsWithTags) {
            tags.addAll(post.getTags());
        }
        Set<String> uniqueTags = new HashSet<>(tags);
        TreeMap<String, Integer> tagsMap = new TreeMap<>();
        for (String tag : uniqueTags) {
            int count = Collections.frequency(tags, tag);
            tagsMap.put(tag, count);
        }
        List<String> topTenTags = tagsMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .limit(10)
                .collect(Collectors.toList());
        return topTenTags;
    }


    @Override
    public List<String> findTags(Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        return post.get().getTags();
    }

    @Override
    public String addVideo(String videoURL) {
        String embedURL = "https://www.youtube.com/embed/";
        if (videoURL.contains("youtube.com/")) {
            embedURL = embedURL + videoURL.substring(videoURL.indexOf("=")+1);
        } else {
            embedURL = embedURL + videoURL.substring(videoURL.lastIndexOf("/")+1);
        }
        return embedURL;
    }

    @Override
    public List<Post> findPosts() {
        List<Post> lastPosts = findAll();

        Collections.reverse(lastPosts);

        lastPosts = lastPosts.stream().limit(30).collect(Collectors.toList());

        return lastPosts;
    }

    @Override
    public List<Post> findPopularPosts() {
        List<Post> popularPosts = findAll();

        Collections.sort(popularPosts,
                Comparator.comparingInt((Post p) -> p.getLikes().size()));
        Collections.reverse(popularPosts);

        popularPosts = popularPosts.stream().limit(10).collect(Collectors.toList());

        return popularPosts;
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

                user.getPosts().remove(postToDelete);
                postRepository.delete(postToDelete);



                userRepository.save(user);
            }
        } else {
            log.debug("User Id Not found. Id:" + userId);
        }

    }

}