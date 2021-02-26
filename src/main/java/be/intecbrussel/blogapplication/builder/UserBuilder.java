package be.intecbrussel.blogapplication.builder;

import be.intecbrussel.blogapplication.model.Role;
import be.intecbrussel.blogapplication.model.User;
import lombok.Data;

import java.time.LocalDate;
import java.util.Collection;

@Data
public class UserBuilder {

    private Long id;
    private String userEmail;
    private String password;
    private String firstName;
    private String lastName;
    private LocalDate birthday;
    private String gender;
    private Collection<Role> roles;
    private Byte[] profileImage;
    private String userBio;

    public UserBuilder(){}

    public UserBuilder(Long id){
        this.id = id;
    }

    public UserBuilder(String userEmail){
        this.userEmail = userEmail;
    }

    public UserBuilder(Long id, String userEmail, String password, String firstName, String lastName, Byte[] profileImage, String userBio) {
        this.id = id;
        this.userEmail = userEmail;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileImage = profileImage;
        this.userBio = userBio;
    }

    public UserBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public UserBuilder setUserEmail(String userEmail) {
        this.userEmail = userEmail;
        return this;
    }

    public UserBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserBuilder setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserBuilder setProfileImage(Byte[] profileImage) {
        this.profileImage = profileImage;
        return this;
    }

    public UserBuilder setUserBio(String userBio) {
        this.userBio = userBio;
        return this;
    }

    public User build(){
        return new User(userEmail,password,firstName,lastName, birthday, gender, roles);
    }
}
