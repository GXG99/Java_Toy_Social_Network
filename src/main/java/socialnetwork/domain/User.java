package socialnetwork.domain;

import java.util.List;
import java.util.Objects;

public class User extends Entity<Long> {
    private String firstName;
    private String lastName;
    private String email;
    private List<User> friends;

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<User> getFriends() {
        return friends;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    @Override
    public String toString() {
        if (friends != null) {
            final String[] friendString = {""};
            friends.forEach(user -> {
                friendString[0] += user.firstName + " " + user.lastName + "; ";
            });
            return "User {" +
                    "email= '" + email + '\'' +
                    ", firstName= '" + firstName + '\'' +
                    ", lastName= '" + lastName + '\'' +
                    ", friends= " + friendString[0] +
                    ", id= " + this.getId() +
                    '}';
        } else {
            return "User {" +
                    "email= '" + email + '\'' +
                    ", firstName= '" + firstName + '\'' +
                    ", lastName= '" + lastName + '\'' +
                    ", friends= " +
                    ", id=" + this.getId() +
                    '}';
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User that = (User) o;
        return getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName()) &&
                getFriends().equals(that.getFriends());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getFriends());
    }
}