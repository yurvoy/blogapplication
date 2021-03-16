package be.intecbrussel.blogapplication.controllers;

import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.services.PostService;
import be.intecbrussel.blogapplication.services.UserService;
import be.intecbrussel.blogapplication.web_security_config.WebConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
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

    Principal mockPrincipal;

    @BeforeEach
    public void setUp() throws Exception {
        mockPrincipal = mock(Principal.class);
        when(mockPrincipal.getName()).thenReturn("abc@gmail.com");

        MockitoAnnotations.openMocks(this);
        user = User.builder().id(1L).email("abc@gmail.com").password("abcdef").build();
        postController = new PostController(userService, postService);
        mockMvc = MockMvcBuilders
                .standaloneSetup(postController)
                .setViewResolvers(webConfig.viewResolver())
                .build();
    }


    @Test
    public void createNewPost() {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/user/" + user.getId() +"/createPost")
                .principal(mockPrincipal);

    }

    @Test
    public void testCreateNewPost() {
    }

    @Test
    public void editPost() {
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