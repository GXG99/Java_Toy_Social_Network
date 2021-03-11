package socialnetwork.domain.validators;

import socialnetwork.domain.Friendship;

public class FriendshipValidator implements Validator<Friendship> {

    @Override
    public void validate(Friendship entity) throws ValidationException {
        String errorList = "";
        if (entity.getId().getLeft() < 0 || entity.getId().getRight() < 0)
            errorList += "ID cannot be Negative";
        if (entity.getId().getLeft().equals(entity.getId().getRight()))
            errorList += "ID must be distinct";
        if (!errorList.isEmpty())
            throw new ValidationException(errorList);
    }
}
