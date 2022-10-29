package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.validators.ExistingEmail;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

public class LoginForm {

    @NotEmpty
    @Email
    @ExistingEmail
    @Size(max=100)
    private String email;

    @Size(min = 6, max = 20)
    private String password;

    private Boolean rememberMe;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(Boolean rememberMe) {
        this.rememberMe = rememberMe;
    }


}
