package socialnetwork.domain;

import socialnetwork.utils.Constants;

import java.time.LocalDateTime;
import java.util.List;

public class Message extends Entity<Long> {
    public Long sender;
    public Long receiver;
    public String message;
    public LocalDateTime date;

    public Message(Long sender, Long receiver, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.date = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "[" + date.format(Constants.DATE_TIME_FORMATTER) + "] " + sender.toString() + ": " + message + "\n";
    }

    public Long getSender() {
        return sender;
    }

    public void setSender(Long sender) {
        this.sender = sender;
    }

    public Long getReceiver() {
        return receiver;
    }

    public void setReceiver(Long receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
