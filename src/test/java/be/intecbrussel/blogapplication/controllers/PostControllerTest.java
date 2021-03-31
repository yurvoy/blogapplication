package be.intecbrussel.blogapplication.controllers;

import be.intecbrussel.blogapplication.model.Post;
import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.services.PostService;
import be.intecbrussel.blogapplication.services.UserService;
import be.intecbrussel.blogapplication.web_security_config.WebConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.repository.query.Param;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


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
//        when(mockPrincipal.getName()).thenReturn("abc@gmail.com");

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
    public void createNewPostFromProfile() throws Exception{

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/user/" + user.getId() + "/createPost/profile")
                .principal(mockPrincipal);

        mockMvc.perform(requestBuilder)
                .andExpect(view().name("redirect:/user/" + user.getId() + "/profile"));
    }

    @Test
    public void createNewPostFromFrontPage() throws Exception{

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/user/" + user.getId() + "/createPost/frontpage")
                .principal(mockPrincipal);

        mockMvc.perform(requestBuilder)
                .andExpect(view().name("redirect:/"));
    }


    @Test
    public void editPost() throws Exception {
        when(postService.findById(anyLong())).thenReturn(post);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/editPost/" + post.getId())
                .principal(mockPrincipal);

        mockMvc.perform(requestBuilder)
                .andExpect(view().name("redirect:/index"));
    }

    @Test
    public void singlePost() throws Exception {
        when(postService.findById(anyLong())).thenReturn(post);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/user/" + post.getId());

        mockMvc.perform(requestBuilder)
                .andExpect( view().name("user/post") );


    }

    @Test
    public void processUpdatePost() throws Exception {
        when(postService.findById(anyLong())).thenReturn(post);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/editPost/" + post.getId())
                .principal(mockPrincipal);

        mockMvc.perform(requestBuilder)
                .andExpect(view().name("redirect:/user/" + user.getId() + "/profile"));
    }

    @Test
    public void likePost() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/likePost/" + post.getId())
                .principal(mockPrincipal);

        mockMvc.perform(requestBuilder)
                .andExpect(view().name("redirect:" + request.getHeader("Referer")));
    }

    @Test
    public void deletePost() throws Exception {
        when(postService.findById(anyLong())).thenReturn(post);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/deletePost/" + post.getId())
                .principal(mockPrincipal);

        mockMvc.perform(requestBuilder)
                .andExpect(view().name("redirect:/index"));
    }

    @Test
    public void processDeletePost() throws Exception {
        when(postService.findById(anyLong())).thenReturn(post);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/deletePost/" + post.getId())
                .principal(mockPrincipal);

        mockMvc.perform(requestBuilder)
                .andExpect(view().name("redirect:/index"));
    }

    @Test
    public void postSearch() throws Exception {
        when(userService.findById(1L)).thenReturn(user);
        when(userService.findByEmail(any())).thenReturn(user);

        RequestBuilder requestBuilder = MockMvcRequestBuilders

                .get("/search")

                .principal(mockPrincipal);

        mockMvc.perform(requestBuilder)
                .andExpect(view().name("searchNotFound"));
    }

    @Test
    public void reviewPosts() throws Exception{
        when(userService.findById(1L)).thenReturn(user);
        when(userService.findByEmail(any())).thenReturn(user);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/user/" + user.getId() + "/reviewPosts")
                .principal(mockPrincipal);

        mockMvc.perform(requestBuilder)
                .andExpect(view().name("user/configuration/reviewPosts"));
    }
}
