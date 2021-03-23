package be.intecbrussel.blogapplication.web_security_config;

import be.intecbrussel.blogapplication.model.AuthProvider;
import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserService userService;

    public OAuth2LoginSuccessHandler(UserService userService) {
        this.userService = userService;
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                        Authentication authentication) throws IOException, ServletException {

        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getEmail();
        String name = oAuth2User.getName();
        AuthProvider provider = AuthProvider.GOOGLE;
        System.out.println("success oauth handler test: " + email);

        User user = userService.findByEmail(email);

        if(user == null) {
            userService.createNewOAuth2User(email, name, provider);
        } else {
            userService.updateOAuth2User(user, name, provider);
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
