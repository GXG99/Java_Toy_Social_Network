package socialnetwork.dto;

import socialnetwork.utils.Constants;

import java.time.LocalDateTime;

public class MessageDTO {
    public String sender;
    public String message;
    public LocalDateTime date;

    public MessageDTO(String sender, String message, LocalDateTime date) {
        this.sender = sender;
        this.message = message;
        this.date = date;
    }

    @Override
    public String toString() {
        return "[" + date.format(Constants.DATE_TIME_FORMATTER) + "] " + sender + ": " + message + "\n";
    }
}
