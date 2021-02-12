package be.intecbrussel.blogapplication;

import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserRepoTest {

    @Autowired
    UserRepository userRepository;

    @Test
    public void test() {
        User user = new User(123456L, "yvo@gmail.com", "F4k3Pass");
        userRepository.save(user);
    }


}
