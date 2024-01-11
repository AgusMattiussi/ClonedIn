package ar.edu.itba.paw.webapp.dto;

public class ErrorDTO {

    private String errorClass;
    private String message;
    private String detail;

    public ErrorDTO(Class<?> errorClass, String message, String detail) {
        this.errorClass = errorClass.getSimpleName();
        this.message = message;
        this.detail = detail;
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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
