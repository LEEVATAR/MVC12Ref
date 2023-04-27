package com.model2.mvc.service.chatbot;

public class ChatbotMessage {
    private String message;

    public ChatbotMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}