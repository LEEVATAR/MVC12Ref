package com.model2.mvc.service.chatbot;

public class ChatbotRequest {
    private String message;

    public ChatbotRequest() {
    }

    public ChatbotRequest(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}