package ar.edu.itba.paw.webapp.dto;

public class ErrorDTO {

    private String errorClass;
    private String message;
    private String description;

    public ErrorDTO(Class<?> errorClass, String message, String description) {
        this.errorClass = errorClass.getSimpleName();
        this.message = message;
        this.description = description;
    }

    public ErrorDTO() {
    }

    public String getErrorClass() {
        return errorClass;
    }

    public void setErrorClass(String errorClass) {
        this.errorClass = errorClass;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
