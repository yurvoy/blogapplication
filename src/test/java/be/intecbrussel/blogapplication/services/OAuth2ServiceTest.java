package be.intecbrussel.blogapplication.services;

import be.intecbrussel.blogapplication.web_security_config.GithubOAuth2User;
import be.intecbrussel.blogapplication.web_security_config.GoogleOAuth2User;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class OAuth2ServiceTest {

    OAuth2Service oAuth2Service = mock(OAuth2Service.class);;

    @Test
    public void loadUser() throws OAuth2AuthenticationException {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("id", "123456");
        attributes.put("first-name", "foo");
        attributes.put("last-name", "FOO");
        attributes.put("email", "foofoo@gmail.com");

        GrantedAuthority authority = new OAuth2UserAuthority(attributes);
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(authority);


        DefaultOAuth2User user = new DefaultOAuth2User(authorities, attributes, "email");

        //Test Custom class
        GoogleOAuth2User googleOAuth2User = new GoogleOAuth2User(user);
        GithubOAuth2User githubOAuth2User = new GithubOAuth2User(user);

        when(oAuth2Service.loadUser(any())).thenReturn(googleOAuth2User);
        when(oAuth2Service.loadUser(any())).thenReturn(githubOAuth2User);

        assertNotNull(googleOAuth2User, "Google OAuth failed");
        assertNotNull(githubOAuth2User, "Github OAuth failed");

    }
}