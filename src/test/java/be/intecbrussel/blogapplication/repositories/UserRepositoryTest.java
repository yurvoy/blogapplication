package be.intecbrussel.blogapplication.repositories;

import be.intecbrussel.blogapplication.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void findByEmail() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@gmail.com");
        userRepository.save(user);

        User result = userRepository.findByEmail("test@gmail.com");
        Assert.assertNotNull("The object you enter return null", result);

        verify(userRepository, times(1)).findByEmail("test@gmail.com");
        verify(userRepository, never()).findAll();

        userRepository.delete(result);
    }


    @Test
    public void findByResetPasswordToken() {
        User user = new User();
        user.setId(1L);
        user.setResetPasswordToken("fake-token");
        userRepository.save(user);

        User result = userRepository.findByResetPasswordToken("fake-token");
        Assert.assertNotNull("The object you enter return null", result);

        verify(userRepository, times(1)).findByResetPasswordToken("fake-token");
        verify(userRepository, never()).findAll();

        userRepository.delete(result);
    }
}