package socialnetwork.utils.events;

import socialnetwork.domain.User;

import javax.swing.event.ChangeEvent;

public class UserListChangeEvent {
    private ChangeEventType type;



    private User data, oldData;

    public UserListChangeEvent(ChangeEventType type, User data) {
        this.type = type;
        this.data = data;
    }

    public UserListChangeEvent(ChangeEventType type, User data, User oldData) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }
    public ChangeEventType getType() {
        return type;
    }

    public User getData() {
        return data;
    }

    public User getOldData() {
        return oldData;
    }
}
