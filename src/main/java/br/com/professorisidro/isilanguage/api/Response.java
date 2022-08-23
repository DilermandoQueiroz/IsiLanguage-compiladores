package br.com.professorisidro.isilanguage.api;

import java.util.List;

public class Response {

    private List<String> result;
    private String error;

    public Response(List<String> result) {
        this.result = result;
    }

    public Response(String error) {
        this.error = error;
    }

    public List<String> getResult() {
        return result;
    }

    public void setResult(List<String> result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
