package be.intecbrussel.blogapplication.web_security_config;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.*;
import java.time.LocalDate;



@Data
public class UserRegistrationDto {

    private String firstName;

    private String lastName;

    private String password;

    private String confirmPassword;

    @NotNull(message = "Please enter birth date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

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
