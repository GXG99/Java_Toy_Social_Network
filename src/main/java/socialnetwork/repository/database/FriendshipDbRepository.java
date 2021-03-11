package socialnetwork.repository.database;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.validators.FriendshipValidator;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.Repository;
import socialnetwork.repository.exceptions.RepositoryException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class FriendshipDbRepository implements Repository<Tuple<Long, Long>, Friendship> {
    private final String url;
    private final String username;
    private final String password;
    private final Validator<Friendship> validator;

    public FriendshipDbRepository(String url, String username, String password, FriendshipValidator validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Optional<Friendship> findOne(Tuple<Long, Long> id) {
        if (id == null) {
            throw new RepositoryException("ID cannot be null");
        }
        return Optional.ofNullable(findFriendship(id));
    }

    private Friendship findFriendship(Tuple<Long, Long> id) {
        Friendship friendship = null;
        String querry = "SELECT * FROM friendships WHERE (friendships.first_uid = " + id.getLeft() +
                "AND friendships.second_uid = " + id.getRight() + ")" +
                "OR (friendships.second_uid = " + id.getLeft() + "AND friendships.first_uid = " +
                id.getRight() + ")";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(querry);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                Long first_uid = resultSet.getLong("first_uid");
                Long second_uid = resultSet.getLong("second_uid");
                LocalDateTime dateTime = resultSet.getTimestamp("date").toLocalDateTime();
                String status = resultSet.getString("status");
                friendship = new Friendship();
                friendship.setStatus(status);
                friendship.setId(new Tuple<>(first_uid, second_uid));
                friendship.setDate(dateTime);
                return friendship;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return friendship;
    }

    @Override
    public Iterable<Friendship> findAll() {
        Set<Friendship> friendships = new HashSet<>();
        String selectAllQuerry = "SELECT * FROM friendships";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(selectAllQuerry);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Long firstId = resultSet.getLong("first_uid");
                Long secondId = resultSet.getLong("second_uid");
                LocalDateTime dateTime = resultSet.getTimestamp("date").toLocalDateTime();
                String status = resultSet.getString("status");
                Friendship friendship = new Friendship();
                friendship.setStatus(status);
                friendship.setId(new Tuple<>(firstId, secondId));
                friendship.setDate(dateTime);
                friendships.add(friendship);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return friendships;
    }

    @Override
    public Optional<Friendship> save(Friendship entity) {
        if (entity == null) {
            throw new RepositoryException("Entity cannot be null");
        }
        validator.validate(entity);
        return Optional.ofNullable(putIfAbsent(entity));
    }

    private Friendship putIfAbsent(Friendship entity) {
        if (findOne(entity.getId()).isPresent()) {
            throw new RepositoryException("Friendship already exists!");
        }
        String saveQuerry = "INSERT INTO friendships(first_uid, second_uid, date, status) VALUES " +
                "(" + entity.getId().getLeft() + "," + entity.getId().getRight() + ",'" +
                entity.getTimestamp() + "','" + entity.getStatus() + "')";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(saveQuerry)) {
            statement.executeUpdate();
            return null;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return entity;
    }

    @Override
    public Optional<Friendship> delete(Tuple<Long, Long> id) {
        if (id == null) {
            throw new RepositoryException("ID cannoe be null");
        }
        return Optional.ofNullable(remove(id));
    }

    private Friendship remove(Tuple<Long, Long> id) {
        Friendship removedFriend = null;
        String removeQuerry = "DELETE from friendships WHERE " +
                "(friendships.first_uid = " + id.getLeft() +
                "AND friendships.second_uid = " +id.getRight() + ")" +
                "OR (friendships.second_uid = " + id.getLeft() +
                "AND friendships.first_uid = " + id.getRight() + ")";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(removeQuerry)) {
            if (findOne(id).isPresent()) {
                removedFriend = findOne(id).get();
                statement.executeUpdate();
                return  removedFriend;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return removedFriend;
    }

    @Override
    public Optional<Friendship> update(Friendship entity) {
        if (entity == null) {
            throw new RepositoryException("Entity cannot be null");
        }
        return Optional.ofNullable(computeIfPresent(entity));
    }

    private Friendship computeIfPresent(Friendship entity) {
        String updateQuerry = "UPDATE friendships SET first_uid = " +
                entity.getId().getLeft() + ", second_uid = " +
                entity.getId().getRight() + ", date = '" +
                entity.getTimestamp() + "'"+ ", status = '" +
                entity.getStatus() + "' WHERE " +
                "(friendships.first_uid = " + entity.getId().getLeft() +
                "AND friendships.second_uid = " + entity.getId().getRight() + ")" +
                "OR (friendships.second_uid = " + entity.getId().getLeft() +
                "AND friendships.first_uid = " + entity.getId().getRight() + ")";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(updateQuerry)) {
            if (statement.executeUpdate() > 0) {
                return findFriendship(entity.getId());
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
