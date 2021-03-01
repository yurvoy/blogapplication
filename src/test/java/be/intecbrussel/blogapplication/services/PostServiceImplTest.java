package be.intecbrussel.blogapplication.services;

import be.intecbrussel.blogapplication.model.Post;
import be.intecbrussel.blogapplication.repositories.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class PostServiceImplTest {

    PostService postService;

    @Mock
    UserService userService;

    @Mock
    PostRepository postRepository;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        postService = new PostServiceImpl(userService, postRepository);

    }

    @Test
    List<Post> getTopPosts() {
        List<Post> topTenPosts = postRepository.findAll();
        topTenPosts = topTenPosts.stream().limit(10).collect(Collectors.toList());
        assertEquals(topTenPosts.size(), 10);
        return topTenPosts;
    }

}