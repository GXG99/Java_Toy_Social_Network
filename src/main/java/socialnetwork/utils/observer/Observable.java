package socialnetwork.utils.observer;

import socialnetwork.utils.events.Event;

public interface Observable {
    void addObserver(Observer e);
    void removeObserver(Observer e);
    void notifyObservers();
}
