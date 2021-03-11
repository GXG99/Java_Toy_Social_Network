package socialnetwork.repository.database;

import socialnetwork.domain.Message;
import socialnetwork.repository.Repository;
import socialnetwork.repository.exceptions.RepositoryException;

import javax.swing.text.html.Option;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class MessageDbRepository implements Repository<Long, Message> {

    private final String url;
    private final String username;
    private final String password;

    public MessageDbRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Optional<Message> findOne(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Iterable<Message> findAll() {
        Set<Message> messages = new HashSet<>();
        String selectAllQuerry = "SELECT * FROM messages";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(selectAllQuerry);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long sender_uid = resultSet.getLong("sender_uid");
                Long receiver_uid = resultSet.getLong("receiver_uid");
                String text = resultSet.getString("message");
                LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();
                Message message = new Message(sender_uid, receiver_uid, text);
                message.setDate(date);
                message.setId(id);
                messages.add(message);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return messages;
    }

    @Override
    public Optional<Message> save(Message entity) {
        if (entity == null) {
            throw new RepositoryException("Entity cannot be null");
        }
        return Optional.ofNullable(putIfAbsent(entity));
    }

    private Message putIfAbsent(Message entity) {
        String saveQuerry = "INSERT INTO messages(sender_uid, receiver_uid, message, date) VALUES (" +
                entity.getSender() + "," +
                entity.getReceiver() + ",'" +
                entity.getMessage() +"','" +
                entity.getDate() + "'"
                + ")";
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
    public Optional<Message> delete(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Message> update(Message entity) {
        return Optional.empty();
    }
}
