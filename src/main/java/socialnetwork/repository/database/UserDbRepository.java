package socialnetwork.repository.database;

import socialnetwork.domain.User;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.Repository;
import socialnetwork.repository.exceptions.RepositoryException;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class UserDbRepository implements Repository<Long, User> {
    private final String url;
    private final String username;
    private final String password;
    private final Validator<User> validator;

    public UserDbRepository(String url, String username, String password, Validator<User> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Optional<User> findOne(Long id) {
        if (id == null) {
            throw new RepositoryException("ID cannot be null");
        }
        return Optional.ofNullable(findUser(id));
    }

    private User findUser(Long id) {
        User user = null;
        String querry = "SELECT * FROM users WHERE users.id = " + id.toString();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(querry);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                Long uID = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                user = new User(firstName, lastName);
                user.setEmail(email);
                user.setId(uID);
                return user;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return user;
    }

    @Override
    public Iterable<User> findAll() {
        Set<User> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                User user = new User(firstName, lastName);
                user.setEmail(email);
                user.setId(id);
                users.add(user);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return users;
    }

    @Override
    public Optional<User> save(User entity) {
        if (entity == null) {
            throw new RepositoryException("Entity cannot be null!");
        }
        validator.validate(entity);
        return Optional.ofNullable(putIfAbsent(entity));
    }

    private User putIfAbsent(User entity) {
        verifyUniqueEmail(entity);
        String saveQuerry = "INSERT INTO users(first_name, last_name, email) VALUES " +
                "('" + entity.getFirstName() + "','" + entity.getLastName() + "','" + entity.getEmail() + "')";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(saveQuerry)) {

            statement.executeUpdate();
            return null;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return entity;
    }

    private void verifyUniqueEmail(User entity) {
        String findQuerry = "SELECT * FROM users WHERE users.email = '" + entity.getEmail() + "'";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(findQuerry);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next())
                throw new RepositoryException("Email already exists");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public Optional<User> delete(Long id) {
        if (id == null) {
            throw new RepositoryException("ID cannot be null");
        }
        return Optional.ofNullable(remove(id));
    }

    private User remove(Long id) {
        String removeQuerry = "DELETE FROM users WHERE users.id = " + id.toString();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(removeQuerry)) {
            statement.executeUpdate();
            return findUser(id);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public Optional<User> update(User entity) {
        if (entity == null) {
            throw new RepositoryException("Entity cannot be null!");
        }
        return Optional.ofNullable(computeIfPresent(entity));
    }

    private User computeIfPresent(User entity) {
        String updateQuerry = "UPDATE users SET first_name = '" +
                entity.getFirstName() + "', last_name = '" +
                entity.getLastName() + "', email = '" +
                entity.getEmail() + "'";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(updateQuerry)) {
            if (statement.executeUpdate() > 0) {
                return findUser(entity.getId());
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
