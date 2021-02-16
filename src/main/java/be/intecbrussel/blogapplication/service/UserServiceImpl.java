package be.intecbrussel.blogapplication.service;

import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;


    @Override
    public Set<User> findAll() {
        log.debug("We are in user service");

        Set<User> userSet = new HashSet<>();
        userRepository.findAll().iterator().forEachRemaining(userSet::add);

        return userSet;
    }

    @Override
    public User findById(Long aLong) {

        Optional<User> userOptional = userRepository.findById(aLong);

        if(!userOptional.isPresent()){
            throw new RuntimeException("User Not Found");
        }

        return userOptional.get();
    }

    @Override
    @Transactional
    public User save(User user) {

        user.setPassword(user.getPassword());
        user.setUserEmail(user.getUserEmail());
        return userRepository.save(user);
    }

    @Override
    public void delete(User object) {

    }

    @Override
    public void deleteById(Long aLong) {

    }

    public void updatePassword(User user, String newPassword){
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }




}
