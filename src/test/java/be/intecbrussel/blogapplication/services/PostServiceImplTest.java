package be.intecbrussel.blogapplication.services;

import be.intecbrussel.blogapplication.model.Post;
import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.repositories.PostRepository;
import be.intecbrussel.blogapplication.repositories.UserRepository;
import be.intecbrussel.blogapplication.web_security_config.CreatePostDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class PostServiceImplTest {

    PostServiceImpl postService;

    UserServiceImpl userService;

    @Mock
    PostRepository postRepository;

    @Mock
    UserRepository userRepository;


    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        postService = new PostServiceImpl(userService, postRepository, userRepository);
    }

    @Test
    void savePost() {

        User user = new User();
        user.setId(1L);

        Post post = new Post();

        CreatePostDto postDto = new CreatePostDto();
        postDto.setPostTitle("this title");
        post.setPostTitle(postDto.getPostTitle());
        post.setUser(user);

        List<Post> listPost = new ArrayList<>();
        listPost.add(post);

        user.setPosts(listPost);

        Optional<Post> postOptional = Optional.of(post);

        when(postRepository.findById(anyLong())).thenReturn(postOptional);
        when(postRepository.save(any())).thenReturn(postOptional);


        assertEquals("this title",post.getPostTitle());
        assertNotNull(postOptional, "post is not null");
        assertEquals(listPost.size(),1);
        assertEquals(post.getUser().getId(), user.getId());
        verify(postRepository, never()).findAll();

    }

    @Test
    void findById() {

        Post post = new Post();
        post.setId(1L);
        Optional<Post> postOptional = Optional.of(post);

        when(postRepository.findById(anyLong())).thenReturn(postOptional);

        Post postReturned = postService.findById(1L);

        assertNotNull(postReturned, "post is not null");
        verify(postRepository, times(1)).findById(1L);
        verify(postRepository, never()).findAll();

    }

    @Test
    void findAll() {

        List<Post> postList = new ArrayList<>();
        Post post = new Post();
        Post post1 = new Post();
        postList.add(post);
        postList.add(post1);

        when(postRepository.findAll()).thenReturn(postList);

        List<Post> postList1 = postService.findAll();

        assertEquals(postList1.size(),2);
        verify(postRepository, times(1)).findAll();
    }

    @Test
    void updatePost(){

        Post post = new Post();
        post.setId(1L);
        post.setPostTitle("TitlebeforeUpdate");
        Optional<Post> postOptional = Optional.of(post);

        CreatePostDto postForm = new CreatePostDto();
        postForm.setPostTitle("TitleafterUpdate");

        when(postRepository.findById(anyLong())).thenReturn(postOptional);

        post.setPostTitle(postForm.getPostTitle());

        assertEquals(post.getPostTitle(), "TitleafterUpdate");
        verify(postRepository, never()).findAll();
    }

    @Test
    void deleteById(){

        User user = new User();
        user.setId(1L);

        Post post = new Post();
        Post post1 = new Post();
        Post post2 = new Post();

        List<Post> listPost = new ArrayList<>();
        listPost.add(post);
        listPost.add(post1);
        listPost.add(post2);

        user.setPosts(listPost);

        Optional<Post> postOptional = Optional.of(post);

        when(postRepository.findById(anyLong())).thenReturn(postOptional);

        listPost.remove(post);

        assertNotNull(postOptional, "post is not null");
        assertEquals(listPost.size(),2);
        assertEquals(listPost.get(1), post1);
        verify(postRepository, never()).findAll();

    }

    @Test
    void likePost(){

        User user = new User();
        user.setEmail("jef@gmail.com");

        Post post = new Post();
        post.setId(1L);

        List<User> likes = new ArrayList<>();

        Optional<Post> postOptional = Optional.of(post);

        if (!likes.contains(user)) {

            likes.add(user);
            post.setLikes(likes);

        } else {

            likes.remove(user);
            post.setLikes(likes);
        }

        when(postRepository.findById(anyLong())).thenReturn(postOptional);
        when(postRepository.save(any())).thenReturn(postOptional);


        List<User> likesList = postOptional.get().getLikes();


        assertEquals(post.getLikes(),likesList);
        assertNotNull(postOptional, "post is not null");
        verify(postRepository, never()).findAll();

    }

    @Test
    void findLikes(){

        Post post = new Post();
        post.setId(1L);

        List<User> userList = new ArrayList<>();
        User user = new User();
        user.setId(1L);
        User user1 = new User();
        userList.add(user1);
        userList.add(user);
        post.setLikes(userList);

        Optional<Post> postOptional = Optional.of(post);

        when(postRepository.findById(anyLong())).thenReturn(postOptional);

        List<User> likeList = postOptional.get().getLikes();


        assertEquals(likeList.size(), userList.size());
        assertEquals(userList.size(), 2);
        assertEquals(likeList.get(1).getId(), 1);

    }


}