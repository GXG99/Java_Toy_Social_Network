package socialnetwork.dto;
import socialnetwork.utils.Constants;

import java.time.LocalDateTime;

public class FriendDTO {
    public String firstName;
    public String lastName;


    public Long id;
    public LocalDateTime date;

    public FriendDTO(String firstName, String lastName, Long id, LocalDateTime date) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.date = date;
    }

    @Override
    public String toString() {
        return firstName + " | " + lastName + " | " + date.format(Constants.DATE_TIME_FORMATTER);
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDateTime getDate() {
        return date;
    }

}
