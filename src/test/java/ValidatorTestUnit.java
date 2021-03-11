import org.junit.Test;
import socialnetwork.domain.User;
import socialnetwork.domain.validators.UserValidator;
import socialnetwork.domain.validators.ValidationException;

public class ValidatorTestUnit {
    @Test
    public void testValidator() {
        UserValidator validator = new UserValidator();

        User validUser = new User("Name1", "Name2");
        validUser.setId(Long.parseLong("1"));
        validator.validate(validUser);

        User invalidUser = new User("", "");
        invalidUser.setId(Long.parseLong("1"));
        try {
            validator.validate(invalidUser);
            assert(false);
        } catch (ValidationException E) {
            assert (true);
        }
    }
}
