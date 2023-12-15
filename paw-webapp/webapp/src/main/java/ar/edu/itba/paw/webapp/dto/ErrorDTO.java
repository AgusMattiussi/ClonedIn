package ar.edu.itba.paw.webapp.dto;

public class ErrorDTO {

    private String errorClass;
    private String message;
    private String details;

    public ErrorDTO(Class<?> errorClass, String message, String details) {
        this.errorClass = errorClass.getSimpleName();
        this.message = message;
        this.details = details;
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

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
