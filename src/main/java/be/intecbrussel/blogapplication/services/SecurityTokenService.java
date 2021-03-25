package be.intecbrussel.blogapplication.services;

import be.intecbrussel.blogapplication.model.SecurityToken;
import be.intecbrussel.blogapplication.model.User;
import org.springframework.stereotype.Service;

public interface SecurityTokenService {

    SecurityToken save(SecurityToken securityToken, User user);

    SecurityToken save(SecurityToken securityToken);

    SecurityToken getSecurityTokenByToken(String token);
}
