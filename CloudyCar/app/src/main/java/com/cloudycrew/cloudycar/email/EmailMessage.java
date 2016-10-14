package com.cloudycrew.cloudycar.email;

/**
 * Created by George on 2016-10-13.
 */

public class EmailMessage {
    private Email to;
    private Email from;
    private String subject;
    private String message;

    public Email getTo() {
        return to;
    }

    public void setTo(Email to) {
        this.to = to;
    }

    public Email getFrom() {
        return from;
    }

    public void setFrom(Email from) {
        this.from = from;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
