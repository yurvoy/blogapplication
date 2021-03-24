package be.intecbrussel.blogapplication.web_security_config;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Map;

public class GithubOAuth2User implements OAuth2User {

    private OAuth2User oAuth2User;

    public GithubOAuth2User(OAuth2User oAuth2User) {
        this.oAuth2User = oAuth2User;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oAuth2User.getAuthorities();
    }

    // principal.getName() must return email address, else return node_id
    @Override
    public String getName() {
        if (oAuth2User.getAttribute("email") != null) {
            return oAuth2User.getAttribute("email");
        }
        return oAuth2User.getAttribute("node_id");
    }

    public String getFullName(){
        return oAuth2User.getAttribute("login");
    }

    public Byte[] getPicture() throws IOException {
        String picture = oAuth2User.getAttribute("avatar_url");

        byte[] bytes = IOUtils.toByteArray(new URL(picture));

        Byte[] pictureObject = ArrayUtils.toObject(bytes);

        return pictureObject;
    }

}
