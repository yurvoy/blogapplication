package be.intecbrussel.blogapplication.services;


import be.intecbrussel.blogapplication.exceptions.UserNotFoundException;
import be.intecbrussel.blogapplication.model.AuthProvider;
import be.intecbrussel.blogapplication.model.SecurityToken;
import be.intecbrussel.blogapplication.repositories.SecurityTokenRepository;
import be.intecbrussel.blogapplication.web_security_config.UserRegistrationDto;
import be.intecbrussel.blogapplication.model.Role;
import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final SecurityTokenRepository securityTokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, SecurityTokenRepository securityTokenRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.securityTokenRepository = securityTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findById(Long aLong) {
        Optional<User> userOptional = userRepository.findById(aLong);
        if (!userOptional.isPresent()) {
            throw new RuntimeException("user not found");
        }
        return userOptional.get();
    }

    public User save(UserRegistrationDto registration)  {
        User user = new User();
        user.setFirstName(registration.getFirstName());
        user.setLastName(registration.getLastName());
        user.setEmail(registration.getEmail());
        user.setPassword(passwordEncoder.encode(registration.getPassword()));
        user.setBirthday(registration.getBirthday());
        user.setGender(registration.getGender());
        user.setRoles(Arrays.asList(new Role("ROLE_USER")));
        user.setAccountVerified(false);
        user.setAuthProvider(AuthProvider.LOCAL);
        userRepository.save(user);
        return userRepository.findByEmail(registration.getEmail());
    }

    @Override
    public void updateProfile(Long userId, Principal principal, User userForm) {
        User user = userRepository.findByEmail(principal.getName());

        if(userForm.getFirstName() != null) {
            user.setFirstName(userForm.getFirstName());
        }
        if(userForm.getLastName() != null) {
            user.setLastName(userForm.getLastName());
        }
        if(userForm.getUserBio() != null) {
            user.setUserBio(userForm.getUserBio());
        }
        if(userForm.getProfileImage() != null) {
            user.setProfileImage(userForm.getProfileImage());
        }
        if(userForm.getBirthday() != null) {
            user.setBirthday(userForm.getBirthday());
        }
        if(userForm.getGender() != null) {
            user.setGender(userForm.getGender());
        }
        userRepository.save(user);
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

    @Override
    public void updateResetPasswordToken(String token, String email) throws UserNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setResetPasswordToken(token);
            userRepository.save(user);
        } else {
            throw new UserNotFoundException("Could not find any customer with the email " + email);
        }
    }

    @Override
    public User getByResetPasswordToken(String token) {
        return userRepository.findByResetPasswordToken(token);
    }

    @Override
    public User getByVerifyAccountToken(String token) {
        SecurityToken verificationToken = securityTokenRepository.findByToken(token);
        return verificationToken.getUser();
    }

    @Override
    public void updatePassword(User user, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);

        user.setResetPasswordToken(null);
        userRepository.save(user);
    }

    private Collection < ? extends GrantedAuthority > mapRolesToAuthorities(Collection < Role > roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public User getLoggedInUser() {
        String loggedInUseremail = SecurityContextHolder.
                getContext().getAuthentication().getName();

        return findByEmail(loggedInUseremail);
    }


    public List<User> findAll(){
        return userRepository.findAll();
    }

    @Override
    public void createNewOAuth2User(String email, String name, Byte[] picture, AuthProvider provider) {
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setFirstName(name);
        newUser.setLastName("");
        newUser.setProfileImage(picture);
        newUser.setAuthProvider(provider);
        newUser.setAccountVerified(true);


        userRepository.save(newUser);
    }

    @Override
    public void updateOAuth2User(User user, Byte[] picture, AuthProvider provider) {
        user.setAuthProvider(provider);
        user.setProfileImage(picture);
        user.setAccountVerified(true);


        userRepository.save(user);
    }


}
