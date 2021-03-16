package be.intecbrussel.blogapplication.controllers;

import be.intecbrussel.blogapplication.model.Post;
import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.services.PostService;
import be.intecbrussel.blogapplication.services.UserService;
import be.intecbrussel.blogapplication.web_security_config.WebConfig;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.time.LocalDateTime;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class PostControllerTest {

    @InjectMocks
    WebConfig webConfig;

    @Mock
    UserService userService;

    @Mock
    PostService postService;

    @InjectMocks
    PostController postController;

    MockMvc mockMvc;

    User user;

    Post post;

    Principal mockPrincipal;

    @BeforeEach
    public void setUp() throws Exception {
        mockPrincipal = mock(Principal.class);
        when(mockPrincipal.getName()).thenReturn("abc@gmail.com");

        MockitoAnnotations.openMocks(this);
        postController = new PostController(userService, postService);
        mockMvc = MockMvcBuilders
                .standaloneSetup(postController)
                .setViewResolvers(webConfig.viewResolver())
                .build();

        post = new Post();
        user = new User();
        user.setId(1L);
        post.setId(1L);
        post.setUser(user);
    }


    @Test
    public void createNewPost() throws Exception{
        when(userService.findByEmail(any())).thenReturn(user);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/user/" + user.getId() + "/createPost")
                .principal(mockPrincipal);

        mockMvc.perform(requestBuilder)
                .andExpect(view().name("user/frontpage"));
    }


    @Test
    public void editPost() throws Exception {
        when(postService.findById(anyLong())).thenReturn(post);

        mockMvc.perform(get("/editPost/" + post.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("user/updatePost"));;
    }

    @Test
    public void processUpdatePost() {
    }

    @Test
    public void likePost() {
    }

    @Test
    public void likeOwnPost() {
    }

    @Test
    public void deletePost() {
    }

    @Test
    public void processDeletePost() {
    }

    @Test
    public void postSearch() {
    }
}