package socialnetwork.console;

import socialnetwork.config.ApplicationContext;
import socialnetwork.domain.Friendship;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.User;
import socialnetwork.domain.validators.FriendshipValidator;
import socialnetwork.domain.validators.UserValidator;
import socialnetwork.repository.Repository;
import socialnetwork.repository.database.FriendshipDbRepository;
import socialnetwork.repository.database.UserDbRepository;
import socialnetwork.service.AdminService;
import socialnetwork.service.UserService;
import socialnetwork.ui.AdminUI;
import socialnetwork.ui.UserUI;

public class MainDb {
    public static void main(String[] args) {
        final String url = ApplicationContext.getPROPERTIES().getProperty("database.socialnetwork.url");
        final String username = ApplicationContext.getPROPERTIES().getProperty("database.socialnetwork.username");
        final String password = ApplicationContext.getPROPERTIES().getProperty("database.socialnetwork.password");
        Repository<Long, User> userDbRepository = new UserDbRepository(url, username, password, new UserValidator());
        Repository<Tuple<Long,Long>, Friendship> friendshipDbRepository = new FriendshipDbRepository(url, username, password, new FriendshipValidator());
        AdminUI adminUI = new AdminUI(new AdminService(userDbRepository, friendshipDbRepository));
        adminUI.runUI();
    }
}
