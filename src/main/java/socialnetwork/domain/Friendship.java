package socialnetwork.domain;

import socialnetwork.utils.Constants;

import java.time.LocalDateTime;


public class Friendship extends Entity<Tuple<Long,Long>> {

    private String status;
    LocalDateTime date;

    public Friendship() {
        status = "PENDING";
        date = LocalDateTime.now();
    }

    /**
     *
     * @return the date when the friendship was created
     */
    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimestamp() {
        return date.format(Constants.DATE_TIME_FORMATTER);
    }
}
