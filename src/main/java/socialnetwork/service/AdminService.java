package socialnetwork.service;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.User;
import socialnetwork.graph.Graph;
import socialnetwork.repository.Repository;
import java.util.*;
import java.util.stream.Collectors;

public class AdminService {
    private Repository<Long, User> repo;
    private Repository<Tuple<Long, Long>, Friendship> friendshipRepo;
    private Graph userGraph;

    public AdminService(Repository<Long, User> repo, Repository<Tuple<Long, Long>, Friendship> friendshipRepo) {
        this.repo = repo;
        this.friendshipRepo = friendshipRepo;
        userGraph = new Graph();
    }

    /**
     * Method that loads data from friendship repository to the graph
     */
    private void loadGraph() {
        userGraph.setAdjacency(new HashMap<>());
        friendshipRepo.findAll().forEach(friendship -> userGraph.addEdge(friendship.getId().getLeft(), friendship.getId().getRight()));
    }

    /**
     * Method that loads data from friendship repository and returns the graph
     *
     * @return - User graph with loaded data
     */
    public Graph getUserGraph() {
        loadGraph();
        return userGraph;
    }

    /**
     * Method that computes and returns the number of connected components in the graph.
     *
     * @return - number of connected components in the network graph
     */
    public int getNumberOfConnectedComponents() {
        loadGraph();
        int singleComponent = 0;
        for (User user : repo.findAll()) {
            if (user.getFriends() == null || user.getFriends().isEmpty()) {
                singleComponent++;
            }
        }
        return userGraph.getNumberOfConnectedComponents() + singleComponent;
    }

    public List<Long> getStrongestConnectedComponent() {
        loadGraph();
        return userGraph.findStrongestConnectedComponent();
    }

    /**
     * Method that updates users friend list.
     */
    private void loadFriendships() {
        repo.findAll().forEach(user -> user.setFriends(null));
        friendshipRepo.findAll().forEach(friendship -> {
            Optional<User> temp1 = repo.findOne(friendship.getId().getLeft());
            Optional<User> temp2 = repo.findOne(friendship.getId().getRight());
            if (temp1.isPresent() && temp2.isPresent()) {
                User user1 = temp1.get();
                User user2 = temp2.get();
                addToFriendlist(user1, user2);
            }
        });
    }

    /**
     * Method that adds two friends and updates their friend list.
     *
     * @param user1 First user
     * @param user2 Second user
     */
    public void addToFriendlist(User user1, User user2) {
        List<User> user1Friends = user1.getFriends();
        if (user1Friends == null) user1Friends = new ArrayList<>();
        user1Friends.add(user2);
        user1.setFriends(user1Friends);
        repo.update(user1);

        List<User> user2Friends = user2.getFriends();
        if (user2Friends == null) user2Friends = new ArrayList<>();
        user2Friends.add(user1);
        user2.setFriends(user2Friends);
        repo.update(user2);
    }

    /**
     * Method that creates a friendship relationship and reloads the repository.
     *
     * @param ID1 - First user ID
     * @param ID2 - Second user ID
     */
    public void addFriend(Long ID1, Long ID2) {
        addFriendRelation(ID1, ID2);
    }

    /**
     * Method that adds two friends.
     *
     * @param ID1 ID of the first user
     * @param ID2 ID of the second user
     */
    public void addFriendRelation(Long ID1, Long ID2) {
        Optional<User> temp1 = repo.findOne(ID1);
        Optional<User> temp2 = repo.findOne(ID2);
        User user1;
        User user2;
        if (temp1.isPresent() && temp2.isPresent()) {
            user1 = temp1.get();
            user2 = temp2.get();
            Friendship friendship = new Friendship();
            Tuple<Long, Long> tuple;
            if (user1.getId() < user2.getId()) {
                tuple = new Tuple<>(user1.getId(), user2.getId());
            } else {
                tuple = new Tuple<>(user2.getId(), user1.getId());
            }
            friendship.setId(tuple);
            friendshipRepo.save(friendship);
        }
    }

    public void removeFriend(Long ID1, Long ID2) {
        removeFriendRelation(ID1, ID2);
        loadFriendships();
    }

    /**
     * Method that removes two friends.
     *
     * @param ID1 ID of the first user
     * @param ID2 ID of the second user
     */
    public void removeFriendRelation(Long ID1, Long ID2) {
        Optional<User> temp1 = repo.findOne(ID1);
        Optional<User> temp2 = repo.findOne(ID2);
        if (temp1.isPresent() && temp2.isPresent()) {
            Tuple<Long, Long> tuple;
            if (ID1 < ID2) {
                tuple = new Tuple<>(ID1, ID2);
            } else {
                tuple = new Tuple<>(ID2, ID1);
            }
            friendshipRepo.delete(tuple);
        }
    }

    /**
     * Method that adds user to the network
     *
     * @param user User to be added to the network
     */
    public void addUser(User user) {
        repo.save(user);
    }

    /**
     * Method that returns all entities
     *
     * @return Iterator of all users in the network
     */
    public Iterable<User> getAll() {
        return repo.findAll();
    }

    /**
     * Method that computes the max ID
     *
     * @return Max ID in the network.
     */
    public Long getMaxID() {
        final Long[] maxID = {-1L};
        repo.findAll().forEach(e -> {
            if (maxID[0] < e.getId()) {
                maxID[0] = e.getId();
            }
        });
        return maxID[0];
    }

    /**
     * Method that removes one user.
     *
     * @param ID ID of user to be deleted
     */
    public void removeUser(Long ID) {
        removeRelations(ID);
        repo.delete(ID);
    }

    private void removeRelations(Long ID) {
        User removedUser;
        if (repo.findOne(ID).isPresent()) {
            removedUser = repo.findOne(ID).get();
            List<User> removedUserFriendlist = Optional.ofNullable(removedUser.getFriends()).stream().flatMap(Collection::stream).collect(Collectors.toList());
            removedUserFriendlist.forEach(removedUserFriend -> {
                removeFriendRelation(removedUser.getId(), removedUserFriend.getId());
                List<User> temp = removedUserFriend.getFriends();
                temp.remove(removedUser);
                removedUserFriend.setFriends(temp);
            });
        }
    }
}
