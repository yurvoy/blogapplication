package be.intecbrussel.blogapplication.services;

import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.repositories.UserRepository;
import be.intecbrussel.blogapplication.web_security_config.CustomOAuth2User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


@Service
public class OAuth2Service extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public OAuth2Service(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);
        return new CustomOAuth2User(user);
    }
}
