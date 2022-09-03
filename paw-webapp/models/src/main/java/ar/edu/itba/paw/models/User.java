package ar.edu.itba.paw.models;

public class User {

    private final long id;
    private final String email;
    private final String password;
    private final String name;
    private final String location;
    //TODO: FK o nombre?
    private final long categoryId_fk;
    private final String currentPosition;
    private final String description;
    private final String education;


    //TODO: Aniadir imagen


    public User(long id, String email, String password, String name, String location, long categoryId_fk, String currentPosition, String description, String education) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.location = location;
        this.categoryId_fk = categoryId_fk;
        this.currentPosition = currentPosition;
        this.description = description;
        this.education = education;
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

    public String getLocation() {
        return location;
    }

    public long getCategoryId_fk() {
        return categoryId_fk;
    }

    public String getCurrentPosition() {
        return currentPosition;
    }

    public String getDescription() {
        return description;
    }

    public String getEducation() {
        return education;
    }

    public String getName() {
        return name;
    }


}
