package socialnetwork.dto;

import java.time.LocalDateTime;

public class FriendRequestDTO {
    private String firstName;
    private String lastName;
    private LocalDateTime date;
    private Long id;
    private String status;

    public FriendRequestDTO(String firstName, String lastName, Long id,  LocalDateTime date, String status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.date = date;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public Long getId() {
        return id;
    }
}
