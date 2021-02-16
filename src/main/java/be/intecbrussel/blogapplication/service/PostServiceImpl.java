package be.intecbrussel.blogapplication.service;

import be.intecbrussel.blogapplication.model.Post;
import be.intecbrussel.blogapplication.repositories.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class PostServiceImpl implements PostService{

    private PostRepository postRepository;

    @Override
    public Set<Post> findAll() {

        Set<Post> posts = new HashSet<>();
        postRepository.findAll().iterator().forEachRemaining(posts::add);

        return posts;
    }

    @Override
    public Post findById(Long aLong) {

        Optional<Post> postOptional = postRepository.findById(aLong);

        if(!postOptional.isPresent()){
            throw new RuntimeException("Post not found");
        }
        return postOptional.get();
    }

    @Override
    public Post save(Post object) {

        object.setTitle(object.getTitle());
        object.setPostDate(object.getPostDate());

        return postRepository.save(object);
    }

    @Override
    public void delete(Post object) {

        postRepository.delete(object);

    }

    @Override
    public void deleteById(Long aLong) {

        postRepository.deleteById(aLong);

    }
}
