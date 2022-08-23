package br.com.professorisidro.isilanguage.api;

public class Message {

    public static final String ERROR = "error";
    public static final String WARNING = "warning";
    public static final String INFO = "info";

    private String type;
    private String message;

    public Message(String type, String message) {
        this.type = type;
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
