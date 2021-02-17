package be.intecbrussel.blogapplication.service;

import be.intecbrussel.blogapplication.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


public interface UserService extends CrudService<User, Long> {
    User findById(Long id);
    User save(User user);
    void updateUser(Long id, String userEmail, String password, Byte[] profileImage, String userBio, LocalDateTime birthdate);
    String updateBio(Long id, String userBio);
}
