package br.com.professorisidro.isilanguage.api;

import java.util.List;

public class Response {

    private List<Message> result;
    private String error;

    public Response(List<Message> result) {
        this.result = result;
    }

    public Response(String error) {
        this.error = error;
    }

    public List<Message> getResult() {
        return result;
    }

    public void setResult(List<Message> result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
