package be.intecbrussel.blogapplication.services;

import be.intecbrussel.blogapplication.model.SecurityToken;
import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.repositories.SecurityTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Slf4j
@Service
public class SecurityTokenServiceImp implements SecurityTokenService {

    private final SecurityTokenRepository securityTokenRepository;
    private final UserService userService;

    public SecurityTokenServiceImp(SecurityTokenRepository securityTokenRepository, UserService userService) {
        this.securityTokenRepository = securityTokenRepository;
        this.userService = userService;
    }

    @Override
    public SecurityToken save(SecurityToken securityToken, User user) {

        User existing = userService.findById(user.getId());
        securityToken.setCreatedAt(LocalDateTime.now());
        securityToken.setExpireAt(LocalDateTime.now().plusSeconds(604800L)); //7 days in seconds
        securityToken.setUser(existing);


        existing.getSecurityTokens().add(securityToken);

        return securityTokenRepository.save(securityToken);
    }

    @Override
    public SecurityToken save(SecurityToken securityToken) {

        securityToken.setCreatedAt(LocalDateTime.now());
        securityToken.setExpireAt(LocalDateTime.now().plusSeconds(604800L)); //7 days in seconds

        SecurityToken savedToken = securityTokenRepository.save(securityToken);

        return savedToken;
    }

    @Override
    public SecurityToken getSecurityTokenByToken(String token){
        return securityTokenRepository.findByToken(token);
    }
}
