package be.intecbrussel.blogapplication.service;

import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Set<User> findAll() {
        Set<User> userSet = new HashSet<>();
        userRepository.findAll().iterator().forEachRemaining(userSet::add);
        return userSet;
    }

    @Override
    public User findById(Long aLong) {
        Optional<User> userOptional = userRepository.findById(aLong);
        if(!userOptional.isPresent()){
            throw new RuntimeException("user not found");
        }
        return userOptional.get();
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void delete(User user) {

    }

    @Override
    public void deleteById(Long userId) {
        userRepository.findUserById(userId).ifPresent(user->{
            userRepository.deleteById(userId);
        });

    }
    /*
    public void updatePassword(User user, String newPassword){
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    } */

    public void updateUser(Long id, String userEmail, String password, Byte[] profileImage, String userBio, LocalDateTime birthdate){
        userRepository.findUserById(id).ifPresent(user -> {
            user.setUserEmail(userEmail);
            user.setPassword(password);
            user.setProfileImage(profileImage);
            user.setUserBio(userBio);
            user.setBirthDate(birthdate);
        });
    }
}
