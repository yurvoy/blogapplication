package be.intecbrussel.blogapplication.repositories;

import be.intecbrussel.blogapplication.model.SecurityToken;
import be.intecbrussel.blogapplication.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
public class SecurityTokenRepositoryTest {

    @Mock
    private SecurityTokenRepository securityTokenRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void findByToken() {
        SecurityToken securityToken = new SecurityToken();
        securityToken.setId(1L);
        securityToken.setToken("ThisTokenIsATest");
        securityTokenRepository.save(securityToken);

        when(securityTokenRepository.findByToken(anyString())).thenReturn(securityToken);
        SecurityToken result = securityTokenRepository.findByToken("ThisTokenIsATest");
        Assert.assertNotNull("The object you enter return null", result);

        verify(securityTokenRepository, times(1)).findByToken("ThisTokenIsATest");
        verify(securityTokenRepository, never()).findAll();

        securityTokenRepository.delete(result);
    }
}
