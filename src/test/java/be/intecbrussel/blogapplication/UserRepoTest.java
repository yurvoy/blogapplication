package be.intecbrussel.blogapplication;

import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;


@SpringBootTest
public class UserRepoTest {

    @Autowired
    UserRepository repo;

    @Test
    public void testUser() {
        User user = new User();
        user.setEmail("foo@gmail.com");
        user.setPassword("foofoo");
        user.setFirstName("foO");
        user.setLastName("oofoof");
        user.setGender("male");

        User savedUser = repo.save(user);

        System.out.println(repo.findByEmail("foo@gmail.com"));
    }


}