package be.intecbrussel.blogapplication.services;

import be.intecbrussel.blogapplication.model.SecurityToken;
import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.repositories.SecurityTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class SecurityTokenServiceImplTest {

    SecurityTokenServiceImpl securityTokenService;

    @Mock
    SecurityTokenRepository securityTokenRepository;

    @Mock
    PostService postService;

    @Mock
    UserService userService;

    SecurityToken securityToken;
    User user;

    @BeforeEach
    void setUp(){

        MockitoAnnotations.openMocks(this);

        securityTokenService = new SecurityTokenServiceImpl(securityTokenRepository, userService);

        user = new User();
        user.setId(1L);
        user.setEmail("mock@gmail.com");
        user.setPassword("newpassword");

        securityToken = new SecurityToken();
        securityToken.setId(1L);
        securityToken.setToken("ThisIsATokenTest");
    }

    @Test
    void save(){
        when(securityTokenRepository.findByToken(anyString())).thenReturn(securityToken);
        when(securityTokenRepository.save(any())).thenReturn(securityToken);

        SecurityToken savedSecurityToken = securityTokenService.save(securityToken);

        assertEquals("ThisIsATokenTest",savedSecurityToken.getToken());
        assertNotNull(savedSecurityToken, "security Token not found");
    }

    @Test
    void saveWithUser(){

        when(securityTokenRepository.findByToken(anyString())).thenReturn(securityToken);
        when(securityTokenRepository.save(any())).thenReturn(securityToken);

        when(userService.findById(anyLong())).thenReturn(user);

        SecurityToken savedSecurityToken = securityTokenService.save(securityToken, user);

        assertEquals("ThisIsATokenTest",savedSecurityToken.getToken());
        assertNotNull(savedSecurityToken, "security Token not found");
        assertEquals(userService.findById(1L).getSecurityTokens().size(),1);
        assertEquals(savedSecurityToken.getUser().getId(), user.getId());

    }


    @Test
    void getSecurityTokenByToken() {

        when(securityTokenRepository.findByToken(anyString())).thenReturn(securityToken);

        SecurityToken savedSecurityToken = securityTokenService.getSecurityTokenByToken("ThisIsATokenTest");

        assertEquals("ThisIsATokenTest",savedSecurityToken.getToken());
        assertNotNull(savedSecurityToken, "security Token not found");

    }
}
