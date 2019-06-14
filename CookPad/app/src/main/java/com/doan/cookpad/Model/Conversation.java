package com.doan.cookpad.Model;

public class Conversation {
    private String ID,MESSAGE;
    private long SEND_TIME;

    public Conversation(String ID, String MESSAGE, long SEND_TIME) {
        this.ID = ID;
        this.MESSAGE = MESSAGE;
        this.SEND_TIME = SEND_TIME;
    }

    public Conversation() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getMESSAGE() {
        return MESSAGE;
    }

    public void setMESSAGE(String MESSAGE) {
        this.MESSAGE = MESSAGE;
    }

    public long getSEND_TIME() {
        return SEND_TIME;
    }

    public void setSEND_TIME(long SEND_TIME) {
        this.SEND_TIME = SEND_TIME;
    }
}
