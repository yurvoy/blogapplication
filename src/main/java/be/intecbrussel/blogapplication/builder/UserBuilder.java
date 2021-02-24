package be.intecbrussel.blogapplication.builder;

import be.intecbrussel.blogapplication.model.User;

import java.time.LocalDate;

public class UserBuilder {

    private Long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private LocalDate birthday;
    private String gender;

    public UserBuilder() {
    }

    public UserBuilder(Long id) {
        this.id = id;
    }

    public UserBuilder(Long id, String email, String password, String firstName, String lastName, LocalDate birthday,
                       String gender, String userBio, Byte[] profileImage) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.gender = gender;

    }

    public UserBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public UserBuilder setEmail(String email) {
        this.email = email;
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

    public UserBuilder setBirthday(LocalDate birthday) {
        this.birthday = birthday;
        return this;
    }

    public UserBuilder setGender(String gender) {
        this.gender = gender;
        return this;
    }



    public User build(){
        return new User(id, email, password, firstName, lastName, birthday, gender);
    }
}
