package be.intecbrussel.blogapplication.services;

import be.intecbrussel.blogapplication.model.AuthProvider;
import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.web_security_config.CustomOAuth2User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


@Service
public class OAuth2Service extends DefaultOAuth2UserService {

    private final UserService userService;

    public OAuth2Service(UserService userService) {
        this.userService = userService;
    }


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        CustomOAuth2User customOAuth2User = new CustomOAuth2User(oAuth2User);
        String email = customOAuth2User.getEmail();
        String name = customOAuth2User.getName();
        AuthProvider provider = AuthProvider.GITHUB;
        System.out.println("success oauth handler test: " + email);

        User user = userService.findByEmail(email);

        if(user == null) {
            userService.createNewOAuth2User(email, name, provider);
        } else {
            userService.updateOAuth2User(user, name, provider);
        }
        return customOAuth2User;
    }
}
