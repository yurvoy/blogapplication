package be.intecbrussel.blogapplication.controllers;

import be.intecbrussel.blogapplication.model.Post;
import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.services.CommentService;
import be.intecbrussel.blogapplication.services.PostService;
import be.intecbrussel.blogapplication.services.UserService;
import be.intecbrussel.blogapplication.web_security_config.WebConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@ExtendWith(MockitoExtension.class)
public class CommentControllerTest {

    @Mock
    private CommentController commentController;

    @Mock
    private UserService userService;
    @Mock
    private PostService postService;
    @Mock
    private CommentService commentService;

    @InjectMocks
    private WebConfig webConfig;

    Post post;

    User user;

    MockMvc mockMvc;

    Principal mockPrincipal;

    @BeforeEach
    void setUp() {
        mockPrincipal = mock(Principal.class);
        when(mockPrincipal.getName()).thenReturn("abc@gmail.com");

        commentController = new CommentController(userService, postService, commentService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(commentController)
                .setViewResolvers(webConfig.viewResolver())
                .build();

        post = new Post();
        user = new User();
        user.setId(1L);
        post.setId(1L);
        post.setUser(user);
    }



    @Test
    void simpleFrontPageComment() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(postService.findById(anyLong())).thenReturn(post);
        when(userService.findById(anyLong())).thenReturn(user);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/user/" + post.getId() + "/" + user.getId() + "/createComment")
                .principal(mockPrincipal);

        mockMvc.perform(requestBuilder)
                .andExpect(view().name("redirect:" + request.getHeader("Referer")));
    }




}