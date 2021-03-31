package be.intecbrussel.blogapplication.services;

import be.intecbrussel.blogapplication.model.AuthProvider;
import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.web_security_config.GithubOAuth2User;
import be.intecbrussel.blogapplication.web_security_config.GoogleOAuth2User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class OAuth2Service extends DefaultOAuth2UserService {

    private final UserService userService;

    public OAuth2Service(UserService userService) {
        this.userService = userService;
    }


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        GithubOAuth2User githubOAuth2User = new GithubOAuth2User(oAuth2User);
        GoogleOAuth2User googleOAuth2User = new GoogleOAuth2User(oAuth2User);

        String email;
        String name;
        AuthProvider provider;
        Byte[] picture = null;

        if (oAuth2User.getAttribute("name") == null) {
            email = githubOAuth2User.getName();
            name = githubOAuth2User.getFullName();
            try {
                picture = githubOAuth2User.getPicture();
            } catch (IOException e) {
                e.printStackTrace();
            }
            provider = AuthProvider.GITHUB;
        } else {
            email = googleOAuth2User.getName();
            name = googleOAuth2User.getFullName();
            try {
                picture = googleOAuth2User.getPicture();
            } catch (IOException e) {
                e.printStackTrace();
            }
            provider = AuthProvider.GOOGLE;
        }

        User user = userService.findByEmail(email);

        if(user == null) {
            userService.createNewOAuth2User(email, name, picture, provider);
        } else {
            userService.updateOAuth2User(user, picture, provider);
        }

        if (oAuth2User.getAttribute("name") == null) {
            return githubOAuth2User;
        }
        return googleOAuth2User;
    }

}
