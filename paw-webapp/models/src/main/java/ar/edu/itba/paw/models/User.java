package ar.edu.itba.paw.models;

public class User {

    private long id;
    private String email;
    private String password;

    public User(long id, String email, String password) {
        super();
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
