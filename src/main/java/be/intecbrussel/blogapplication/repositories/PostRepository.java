package be.intecbrussel.blogapplication.repositories;

import be.intecbrussel.blogapplication.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {


}
