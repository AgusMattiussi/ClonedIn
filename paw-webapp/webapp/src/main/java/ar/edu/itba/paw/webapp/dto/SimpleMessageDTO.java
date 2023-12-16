package ar.edu.itba.paw.webapp.dto;

public class SimpleMessageDTO {

    private String message;

    public SimpleMessageDTO() {
    }

    public SimpleMessageDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message=message;
    }
}
