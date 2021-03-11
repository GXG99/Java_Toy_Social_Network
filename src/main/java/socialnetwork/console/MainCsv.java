package socialnetwork.console;
import socialnetwork.domain.Friendship;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.validators.FriendshipValidator;
import socialnetwork.repository.file.FriendFile;
import socialnetwork.service.AdminService;
import socialnetwork.ui.AdminUI;
import socialnetwork.config.ApplicationContext;
import socialnetwork.domain.User;
import socialnetwork.domain.validators.UserValidator;
import socialnetwork.repository.Repository;
import socialnetwork.repository.file.UserFile;


public class MainCsv {
    public static void main(String[] args) {
        String fileName = ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.users");
        String friendshipFile = ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.friends");
        Repository<Long, User> userFileRepository = new UserFile(fileName, new UserValidator());
        Repository<Tuple<Long,Long>, Friendship> friendshipFileRepository = new FriendFile(friendshipFile, new FriendshipValidator());
        AdminUI adminUI = new AdminUI(new AdminService(userFileRepository, friendshipFileRepository));
        adminUI.runUI();
    }
}


