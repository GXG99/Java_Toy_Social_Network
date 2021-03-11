package socialnetwork.service;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.Message;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.User;
import socialnetwork.dto.FriendDTO;
import socialnetwork.dto.FriendRequestDTO;
import socialnetwork.dto.MessageDTO;
import socialnetwork.repository.Repository;
import socialnetwork.repository.exceptions.RepositoryException;
import socialnetwork.repository.memory.InMemoryRepository;
import socialnetwork.utils.observer.Observable;
import socialnetwork.utils.observer.Observer;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class UserService implements Observable {
    private Repository<Long, User> userRepository;
    private Repository<Tuple<Long, Long>, Friendship> friendshipRepository;
    private Repository<Long, Message> messageRepository;
    private List<Message> messageInMemoryRepository;
    private User loggedUser;

    public UserService(Repository<Long, User> userRepository, Repository<Tuple<Long, Long>, Friendship> friendshipRepository, Repository<Long, Message> messageRepository) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
        this.messageRepository = messageRepository;
        loggedUser = null;
    }

    // Register manager

    public void addUser(User user) {
        userRepository.save(user);
    }

    // Friendship manager

    public void removeFriend(Long ID1, Long ID2) {
        Optional<User> temp1 = userRepository.findOne(ID1);
        Optional<User> temp2 = userRepository.findOne(ID2);
        if (temp1.isPresent() && temp2.isPresent()) {
            Tuple<Long, Long> tuple;
            tuple = new Tuple<>(ID1, ID2);
            friendshipRepository.delete(tuple);
        }
        notifyObservers();
    }

    public void addFriend(Long ID1, Long ID2) {
        Optional<User> temp1 = userRepository.findOne(ID1);
        Optional<User> temp2 = userRepository.findOne(ID2);
        if (temp1.isPresent() && temp2.isPresent()) {
            Friendship friendship = new Friendship();
            Tuple<Long, Long> tuple;
            tuple = new Tuple<>(ID1, ID2);
            friendship.setId(tuple);
            friendshipRepository.save(friendship);
        }
        notifyObservers();
    }

    public List<FriendRequestDTO> getSentRequests() {
        List<FriendRequestDTO> friendRequests = new LinkedList<>();
        Iterable<Friendship> friendships = friendshipRepository.findAll();
        List<Friendship> filteredFriendships = StreamSupport.stream(friendships.spliterator(), false)
                .filter(friendship -> (friendship.getId().getLeft().equals(loggedUser.getId()))
                        || (friendship.getId().getRight().equals(loggedUser.getId()))).collect(Collectors.toList());
        for (Friendship friendship : filteredFriendships) {
            if (friendship.getId().getLeft().equals(loggedUser.getId())) {
                String firstName;
                String lastName;
                LocalDateTime date;
                Long id;
                String status;
                if (userRepository.findOne(friendship.getId().getRight()).isPresent()) {
                    firstName = userRepository.findOne(friendship.getId().getRight()).get().getFirstName();
                    lastName = userRepository.findOne(friendship.getId().getRight()).get().getLastName();
                    date = friendship.getDate();
                    id = friendship.getId().getRight();
                    status = friendship.getStatus();
                    if (status.equals("PENDING"))
                        friendRequests.add(new FriendRequestDTO(firstName, lastName, id, date, status));
                }
            }
        }
        return friendRequests;
    }

    public List<FriendRequestDTO> getFriendRequests() {
        List<FriendRequestDTO> friendRequests = new LinkedList<>();
        Iterable<Friendship> friendships = friendshipRepository.findAll();
        List<Friendship> filteredFriendships = StreamSupport.stream(friendships.spliterator(), false)
                .filter(friendship -> (friendship.getId().getLeft().equals(loggedUser.getId()))
                        || (friendship.getId().getRight().equals(loggedUser.getId()))).collect(Collectors.toList());
        for (Friendship friendship : filteredFriendships) {
            if (friendship.getId().getRight().equals(loggedUser.getId())) {
                String firstName;
                String lastName;
                LocalDateTime date;
                Long id;
                String status;
                if (userRepository.findOne(friendship.getId().getLeft()).isPresent()) {
                    firstName = userRepository.findOne(friendship.getId().getLeft()).get().getFirstName();
                    lastName = userRepository.findOne(friendship.getId().getLeft()).get().getLastName();
                    date = friendship.getDate();
                    id = friendship.getId().getLeft();
                    status = friendship.getStatus();
                    if (status.equals("PENDING"))
                        friendRequests.add(new FriendRequestDTO(firstName, lastName, id, date, status));
                }
            }
        }
        return friendRequests;
    }

    public List<FriendDTO> getFriendsByDate(int month) {
        List<FriendDTO> friends = new LinkedList<>();
        Iterable<Friendship> friendships = friendshipRepository.findAll();
        List<Friendship> filteredFriendships = StreamSupport.stream(friendships.spliterator(), false)
                .filter(friendship -> (friendship.getId().getLeft().equals(loggedUser.getId()))
                        || (friendship.getId().getRight().equals(loggedUser.getId()))).collect(Collectors.toList());
        for (Friendship friendship : filteredFriendships) {
            if (!friendship.getId().getLeft().equals(loggedUser.getId())) {
                String firstName;
                String lastName;
                LocalDateTime date;
                Long id;
                if (userRepository.findOne(friendship.getId().getLeft()).isPresent()) {
                    firstName = userRepository.findOne(friendship.getId().getLeft()).get().getFirstName();
                    lastName = userRepository.findOne(friendship.getId().getLeft()).get().getLastName();
                    date = friendship.getDate();
                    id = friendship.getId().getLeft();
                    friends.add(new FriendDTO(firstName, lastName, id, date));
                }
            } else if (!friendship.getId().getRight().equals(loggedUser.getId())) {
                String firstName;
                String lastName;
                LocalDateTime date;
                Long id;
                if (userRepository.findOne(friendship.getId().getRight()).isPresent()) {
                    firstName = userRepository.findOne(friendship.getId().getRight()).get().getFirstName();
                    lastName = userRepository.findOne(friendship.getId().getRight()).get().getLastName();
                    date = friendship.getDate();
                    id = friendship.getId().getRight();
                    friends.add(new FriendDTO(firstName, lastName, id, date));
                }
            }
        }
        return StreamSupport.stream(friends.spliterator(), false)
                .filter(friendDTO -> friendDTO.date.getMonthValue() == month).collect(Collectors.toList());
    }

    public List<User> getNonFriends() {
        Iterable<User> userIterable = userRepository.findAll();
        Iterable<Friendship> friendshipIterable = friendshipRepository.findAll();
        List<User> users = StreamSupport.stream(userIterable.spliterator(), false)
                .collect(Collectors.toList());

        List<Friendship> filteredFriendships = StreamSupport.stream(friendshipIterable.spliterator(), false)
                .filter(friendship -> (friendship.getId().getLeft().equals(loggedUser.getId()))
                        || (friendship.getId().getRight().equals(loggedUser.getId()))).collect(Collectors.toList());

        List<User> filteredList = users.stream().filter(user -> filteredFriendships.stream().allMatch(friendship -> {
                    if (friendship.getId().getLeft().equals(user.getId()))
                        return false;
                    if (friendship.getId().getRight().equals(user.getId()))
                        return false;
                    return true;
                }
        )).collect(Collectors.toList());
        return filteredList;
    }

    public List<FriendDTO> getFriends() {
        List<FriendDTO> friends = new LinkedList<>();
        Iterable<Friendship> friendships = friendshipRepository.findAll();
        List<Friendship> filteredFriendships = StreamSupport.stream(friendships.spliterator(), false)
                .filter(friendship -> (friendship.getId().getLeft().equals(loggedUser.getId()))
                        || (friendship.getId().getRight().equals(loggedUser.getId()))).collect(Collectors.toList());
        for (Friendship friendship : filteredFriendships) {
            if (!friendship.getId().getLeft().equals(loggedUser.getId())) {
                String firstName;
                String lastName;
                LocalDateTime date;
                Long id;
                if (userRepository.findOne(friendship.getId().getLeft()).isPresent()) {
                    firstName = userRepository.findOne(friendship.getId().getLeft()).get().getFirstName();
                    lastName = userRepository.findOne(friendship.getId().getLeft()).get().getLastName();
                    id = friendship.getId().getLeft();
                    date = friendship.getDate();
                    if (friendship.getStatus().equals("ACCEPTED"))
                        friends.add(new FriendDTO(firstName, lastName, id, date));
                }
            } else if (!friendship.getId().getRight().equals(loggedUser.getId())) {
                String firstName;
                String lastName;
                LocalDateTime date;
                Long id;
                if (userRepository.findOne(friendship.getId().getRight()).isPresent()) {
                    firstName = userRepository.findOne(friendship.getId().getRight()).get().getFirstName();
                    lastName = userRepository.findOne(friendship.getId().getRight()).get().getLastName();
                    date = friendship.getDate();
                    id = friendship.getId().getRight();
                    if (friendship.getStatus().equals("ACCEPTED"))
                        friends.add(new FriendDTO(firstName, lastName, id, date));
                }
            }
        }
        return friends;
    }


    /**
     * Method that updates the logged user
     */
    public void logIn(String email) {
        Iterable<User> userList = userRepository.findAll();
        for (User user : userList) {
            if (user.getEmail().equals(email)) {
                loggedUser = user;
                break;
            }
        }
        if (loggedUser == null)
            throw new RepositoryException("Email does not exist!");
    }

    /**
     * Method that return the logged user
     *
     * @return - User
     */
    public User getLoggedUser() {
        return loggedUser;
    }

    // Friend request manager

    public void acceptRequest(Long id1, Long id2) {
        Tuple<Long, Long> requestTuple = new Tuple<>(id1, id2);
        Friendship friendship = friendshipRepository.findOne(requestTuple).get();
        friendship.setStatus("ACCEPTED");
        friendshipRepository.update(friendship);
        notifyObservers();
    }

    public void declineRequest(Long id1, Long id2) {
        Tuple<Long, Long> requestTuple = new Tuple<>(id1, id2);
        Friendship friendship = friendshipRepository.findOne(requestTuple).get();
        friendship.setStatus("REJECTED");
        friendshipRepository.update(friendship);
        notifyObservers();
    }

    public void removeRequest(Long id1, Long id2) {
        Tuple<Long, Long> requestTuple = new Tuple<>(id1, id2);
        friendshipRepository.delete(requestTuple);
        notifyObservers();
    }

    //Observer

    private List<Observer> observers = new ArrayList<>();

    @Override
    public void addObserver(Observer e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers() {
        observers.stream().forEach(x -> x.update());
    }

    // Message manager

    public void sendMessage(Message message) {
        try {
            messageRepository.save(message);
            messageInMemoryRepository.add(message);
        } catch (RepositoryException repositoryException) {
            repositoryException.printStackTrace();
        }
    }

    public void loadAllMessages() {
        Iterable<Message> messages = messageRepository.findAll();
        messageInMemoryRepository = StreamSupport.stream(messages.spliterator(), false).filter(message ->
                (message.getSender().equals(getLoggedUser().getId())) ||
                        (message.getReceiver().equals(getLoggedUser().getId()))).collect(Collectors.toList());
    }

    public List<MessageDTO> loadMessages(Long friend_uid) {
        List<MessageDTO> messageDTOS = new LinkedList<>();
        List<Message> messageList = messageInMemoryRepository.stream().filter(message ->
                message.getSender().equals(friend_uid) || message.getReceiver().equals(friend_uid))
                .sorted((Comparator.comparing(Message::getDate))).collect(Collectors.toList());
        messageList.forEach(message -> {
            User sender = userRepository.findOne(message.getSender()).get();
            MessageDTO messageDTO = new MessageDTO(sender.getFirstName() + " " + sender.getLastName(),
                    message.getMessage(),
                    message.getDate());
            messageDTOS.add(messageDTO);
        });
        return messageDTOS;
    }
}
