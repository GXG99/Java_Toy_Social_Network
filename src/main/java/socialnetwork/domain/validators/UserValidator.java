package socialnetwork.domain.validators;

import socialnetwork.domain.User;

public class UserValidator implements Validator<User> {

    @Override
    public void validate(User entity) throws ValidationException {
        String errorList = "";
        if (entity.getFirstName() == null || entity.getFirstName().equals("")) {
            errorList += "First name cannot be NULL\n";
        }
        if (entity.getLastName() == null || entity.getLastName().equals("")) {
            errorList += "Last name cannot be NULL\n";
        }
        if(!errorList.isEmpty())
            throw new ValidationException(errorList);
    }
}
