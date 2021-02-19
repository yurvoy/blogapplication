package be.intecbrussel.blogapplication;

import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
public class UserRepoTest {


    @Autowired
    UserRepository userRepository;

    @Test
    public void repoTest() {
        User user = new User("Geek@gmail.com", "p455w00rd", "foo", "fooFOO", LocalDate.of(1999, 05, 01), "male");
        userRepository.save(user);
    }


}
