package com.eshya.test.payload;

import org.springframework.http.ResponseEntity;

public class DefaultMessage {
    private String message;

    // Constructor that accepts the message directly
    public DefaultMessage(String message) {
        this.message = message;
    }

    // Getter and setter for the message property
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // Method to create and return ResponseEntity
    public ResponseEntity<Object> toResponseEntity() {
        return ResponseEntity.ok(new MessageResponse(message));
    }

    // Inner class for encapsulating the message
    static class MessageResponse {
        private String message;

        public MessageResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
