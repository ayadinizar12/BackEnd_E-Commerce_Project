package com.sid.demo.exceptions;

public class ErrorRespo {

	private String message;

    public ErrorRespo(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
