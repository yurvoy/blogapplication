package be.intecbrussel.blogapplication.web_security_config;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import javax.validation.constraints.*;


@Data
public class UserRegistrationDto {

    private String firstName;

    private String lastName;

    private String password;

    private String confirmPassword;

    @NotNull(message = "Please enter birth date")
    private String birthday;

    private String gender;


    @Email
    private String email;

    @AssertTrue
    private Boolean terms;


    public void setFirstName(String firstName) {
        this.firstName = StringUtils.capitalize(firstName);
    }

    public void setLastName(String lastName) {
        this.lastName = StringUtils.capitalize(lastName);
    }

}
