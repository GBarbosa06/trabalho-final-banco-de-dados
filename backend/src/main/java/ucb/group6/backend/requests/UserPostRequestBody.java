package ucb.group6.backend.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class UserPostRequestBody {
    @NotEmpty(message = "The name field cannot be empty")
    private String name;

    @Email(message = "The email is not valid")
    @NotEmpty(message = "The email field cannot be empty")
    private String email;

    @NotEmpty(message = "The password field cannot be empty")
    @Size(min = 6, message = "Password must have at least 6 characters")
    private String password;

    @URL(message = "The url is not valid")
    private String url;
}
