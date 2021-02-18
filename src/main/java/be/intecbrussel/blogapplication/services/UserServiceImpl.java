package be.intecbrussel.blogapplication.services;

import be.intecbrussel.blogapplication.web.UserRegistrationDto;
import be.intecbrussel.blogapplication.model.Role;
import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findById(Long aLong) {
        Optional<User> userOptional = userRepository.findById(aLong);
        if(!userOptional.isPresent()){
            throw new RuntimeException("user not found");
        }
        return userOptional.get();
    }

    public User save(UserRegistrationDto registration) {
        User user = new User();
        user.setFirstName(registration.getFirstName());
        user.setLastName(registration.getLastName());
        user.setEmail(registration.getEmail());
        user.setPassword(passwordEncoder.encode(registration.getPassword()));
        user.setBirthday(registration.getBirthday());
        user.setGender(registration.getGender());
        user.setRoles(Arrays.asList(new Role("ROLE_USER")));
        return userRepository.save(user);
    }

    @Override
    public String updateBio(Long id, String userBio){
        userRepository.findById(id).ifPresent(user -> {
            user.setUserBio(userBio);
        });
        return userBio;
    }

    /*public void updateUser(Long id, String userEmail, String password, Byte[] profileImage, String userBio, LocalDateTime birthdate){
        userRepository.findUserById(id).ifPresent(user -> {
            user.setUserEmail(userEmail);
            user.setPassword(password);
            user.setProfileImage(profileImage);
            user.setUserBio(userBio);
            user.setBirthDate(birthdate);
        });
    }

     */
    @Override
    public void updateUser(Long id, String userEmail, String password, Byte[] profileImage, String userBio, LocalDateTime birthday){
    userRepository.findById(id).ifPresent(user -> {
        user.setEmail(userEmail);
        user.setPassword(password);
        user.setProfileImage(profileImage);
        user.setUserBio(userBio);
        user.setBirthday(birthday);
        userRepository.save(user);
    });

}


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection < ? extends GrantedAuthority > mapRolesToAuthorities(Collection < Role > roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

}
