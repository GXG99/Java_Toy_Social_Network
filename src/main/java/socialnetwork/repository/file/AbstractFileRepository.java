package socialnetwork.repository.file;

import socialnetwork.domain.Entity;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.memory.InMemoryRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E> {
    String fileName;

    public AbstractFileRepository(String fileName, Validator<E> validator) {
        super(validator);
        this.fileName = fileName;
        loadData();
    }

    /**
     * Method that loads data from file to in-memory repositories
     */
    private void loadData() {
        Path path = Paths.get(fileName);
        try {
            List<String> lines = Files.readAllLines(path);
            lines.forEach(line -> {
                E entity = extractEntity(Arrays.asList(line.split(";")));
                super.save(entity);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * extract entity  - template method design pattern
     * creates an entity of type E having a specified list of @code attributes
     *
     * @param attributes list of attributes
     * @return an entity of type E
     */
    public abstract E extractEntity(List<String> attributes);

    protected abstract String createEntityAsString(E entity);

    /**
     * @param entity Entity to be saved and written to file
     * @return Optional of Entity if entity is present, empty otherwise.
     */
    @Override
    public Optional<E> save(E entity) {
        Optional<E> optional = super.save(entity);
        if (optional.isPresent()) {
            return optional;
        }
        writeToFile(entity);
        return Optional.empty();
    }

    /**
     * Method that writes an entity to file
     * @param entity Entity to be written to file
     */
    protected void writeToFile(E entity) {
        try (BufferedWriter bW = new BufferedWriter(new FileWriter(fileName, true))) {
            bW.write(createEntityAsString(entity));
            bW.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param id ID of entity to be deleted
     * @return Optional of entity if entity is present, empty otherwise.
     */
    @Override
    public Optional<E> delete(ID id) {
        Optional<E> optional = super.delete(id);
        if (!optional.isPresent()) {
            return optional;
        }
        refreshFile();
        return Optional.empty();
    }

    /**
     * Method that updates file content with in-memory content.
     */
    public void refreshFile() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName, false))) {
            for (E user : super.findAll()) {
                try {
                    bufferedWriter.write(createEntityAsString(user));
                    bufferedWriter.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

