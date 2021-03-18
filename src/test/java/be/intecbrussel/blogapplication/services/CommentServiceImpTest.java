package be.intecbrussel.blogapplication.services;

import be.intecbrussel.blogapplication.model.Comment;
import be.intecbrussel.blogapplication.model.Post;
import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.repositories.CommentRepository;
import be.intecbrussel.blogapplication.web_security_config.CreateCommentDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class CommentServiceImpTest {

    CommentServiceImpl commentService;

    @Mock
    CommentRepository commentRepository;

    @Mock
    PostService postService;

    @Mock
    UserService userService;

    Comment comment;
    User user;
    Post post;

    @BeforeEach
    void setUp(){

        MockitoAnnotations.openMocks(this);

        commentService = new CommentServiceImpl(commentRepository, postService, userService);

        user = new User();
        user.setId(1L);
        user.setEmail("mock@gmail.com");
        user.setPassword("newpassword");

        post = new Post();
        post.setId(1L);
        post.setUser(user);

        comment = new Comment();
        comment.setId(1L);
        comment.setCommentText("This is a comment test");
    }

    @Test
    void findById() {

        Optional<Comment> commentOptional = Optional.of(comment);

        when(commentRepository.findById(anyLong())).thenReturn(commentOptional);

        Comment commentReturned = commentService.findById(1L);

        assertNotNull(commentReturned, "user is not null");
        verify(commentRepository, times(1)).findById(1L);
        verify(commentRepository, never()).findAll();
    }

    @Test
    void saveComment(){
        CreateCommentDto newComment = new CreateCommentDto();
        newComment.setCommentText("This is a comment test");

        List<Comment> comments = new ArrayList<>();
        post.setComments(comments);
        comment.setUser(user);
        comment.setPost(post);

        Optional<Comment> optionalComment = Optional.of(comment);

        when(commentRepository.findById(anyLong())).thenReturn(optionalComment);
        when(commentRepository.save(any())).thenReturn(comment);

        when(postService.findById(anyLong())).thenReturn(post);
        when(userService.findById(anyLong())).thenReturn(user);

        Comment commentReturned = commentService.saveComment(1L,1L, newComment);

        assertEquals("This is a comment test",commentReturned.getCommentText());
        assertNotNull(commentReturned, "comment not found");
        assertEquals(commentReturned.getPost().getComments().size(),1);
        assertEquals(commentReturned.getUser().getId(), user.getId());
        verify(commentRepository, never()).findAll();

    }

    @Test
    void findAll(){
        List<Comment> comments = new ArrayList<>();
        comments.add(comment);

        when(commentRepository.findAll()).thenReturn(comments);

        List<Comment> returnedComments = commentService.findAll();

        assertEquals(comments.size(),1);
    }
}
