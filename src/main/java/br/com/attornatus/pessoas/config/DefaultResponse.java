package br.com.attornatus.pessoas.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DefaultResponse {

    protected String status = "SUCCESS";
    private List<String> messages = new ArrayList();

    public DefaultResponse() {
    }

    public DefaultResponse(String status, String message) {
        this.status = status;
        this.messages = Arrays.asList(message);
    }

    public DefaultResponse(String status, List<String> messages) {
        this.status = status;
        this.messages = messages;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getMessages() {
        return this.messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public void setMessage(String message) {
        this.messages = Arrays.asList(message);
    }

    public void add(String message) {
        this.messages.add(message);
    }
}
