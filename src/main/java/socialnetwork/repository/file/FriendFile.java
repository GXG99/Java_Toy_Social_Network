package socialnetwork.repository.file;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.validators.Validator;

import java.util.List;

public class FriendFile extends AbstractFileRepository<Tuple<Long, Long>, Friendship> {

    public FriendFile(String fileName, Validator<Friendship> validator) {
        super(fileName, validator);
    }

    @Override
    public Friendship extractEntity(List<String> attributes) {
        Friendship friendship = new Friendship();
        Tuple<Long, Long> tuple = new Tuple<>(Long.parseLong(attributes.get(0)), Long.parseLong(attributes.get(1)));
        friendship.setId(tuple);
        return friendship;
    }

    @Override
    protected String createEntityAsString(Friendship entity) {
        return entity.getId().getLeft() + ";" + entity.getId().getRight();
    }
}
