package be.intecbrussel.blogapplication.repositories;

import be.intecbrussel.blogapplication.model.Post;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan(basePackageClasses = be.intecbrussel.blogapplication.repositories.PostRepository.class)
class PostRepositoryTest {


    @Autowired
    private PostRepository repository;


    @Test
    void search() {

        Post post = new Post();
        Post post1 = new Post();
        post.setPostTitle("this post");
        post1.setPostTitle("another post");

        List<Post> posts = new ArrayList<>();
        posts.add(post);
        posts.add(post1);
        repository.saveAll(posts);
        List<Post> postOptional = repository.search("this post");


        assertEquals(postOptional.get(0).getPostTitle(), post.getPostTitle());
        assertEquals(posts.size(), repository.count());
        assertEquals(posts.size(), 2);
        assertEquals(repository.count(), 2);

    }
}
