package be.intecbrussel.blogapplication.controllers;

import be.intecbrussel.blogapplication.model.Post;
import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.services.CommentService;
import be.intecbrussel.blogapplication.services.PostService;
import be.intecbrussel.blogapplication.services.UserService;
import be.intecbrussel.blogapplication.web_security_config.CreateCommentDto;
import be.intecbrussel.blogapplication.web_security_config.WebConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class CommentControllerTest {

    @MockBean
    private BindingResult mockBindingResult;

    @Mock
    private CommentService commentService;

    @Mock
    private UserService userService;
    @Mock
    private PostService postService;

    @InjectMocks
    private CommentController commentController;

    @InjectMocks
    private WebConfig webConfig;

    CreateCommentDto newComment;

    Post post;

    User user;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        CommentController commentController = new CommentController(userService, postService, commentService);
        mockMvc = MockMvcBuilders
                .standaloneSetup(commentController)
                .setViewResolvers(webConfig.viewResolver())
                .build();

        post = new Post();
        user = new User();
        post.setId(1L);
        post.setUser(user);

        mockBindingResult = mock(BindingResult.class);
    }

    @Test
    void simpleComment() throws Exception {
        when(mockBindingResult.hasErrors()).thenReturn(false);
        when(postService.findById(anyLong())).thenReturn(post);
        String registered = commentController.createNewPost(post.getId(), user.getId(), newComment, mockBindingResult);
        assertThat(registered, is("redirect:/"));
    }

    @Test
    void shouldStayOnRegistrationPageIfBindingErrors() throws Exception {
        when(mockBindingResult.hasErrors()).thenReturn(true);

        String registered = commentController.createNewPost(post.getId(), user.getId(), newComment, mockBindingResult);

        assertThat(registered, is("user/frontpage"));
    }
}