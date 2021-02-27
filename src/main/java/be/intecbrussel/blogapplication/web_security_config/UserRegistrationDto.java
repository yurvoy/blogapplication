package be.intecbrussel.blogapplication.web_security_config;

import lombok.Data;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@FieldMatch.List({
        @FieldMatch(first = "password", second = "confirmPassword", message = "The password fields must match")
})
@Data
public class UserRegistrationDto {

    private String firstName;

    private String lastName;

    private String password;

    private String confirmPassword;

    private String birthday;

    private String gender;

    @Email
    private String email;

    @AssertTrue
    private Boolean terms;

}
