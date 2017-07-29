package com.training.android.undivided;


public class Message {

    private String number;
    private String message;

    public Message(String number, String message) {
        this.number = number;
        this.message = message;
    }

    public String message() {
        return this.message;
    }

    public String number() {
        return this.number;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setNumber(String number) {
        this.number = number;
    }

}
