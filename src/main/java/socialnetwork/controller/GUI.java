package socialnetwork.controller;

import javafx.scene.Scene;
import javafx.stage.Stage;
import socialnetwork.config.ApplicationContext;
import socialnetwork.domain.validators.FriendshipValidator;
import socialnetwork.domain.validators.UserValidator;
import socialnetwork.repository.database.FriendshipDbRepository;
import socialnetwork.repository.database.MessageDbRepository;
import socialnetwork.repository.database.UserDbRepository;
import socialnetwork.service.UserService;

public class GUI {

    public final String url = ApplicationContext.getPROPERTIES().getProperty("database.socialnetwork.url");
    public final String username = ApplicationContext.getPROPERTIES().getProperty("database.socialnetwork.username");
    public final String password = ApplicationContext.getPROPERTIES().getProperty("database.socialnetwork.password");

    public LoginController loginController;
    public MainController mainController;
    public ScreenController screenController;
    public RegisterController registerController;
    public FriendRequestsController friendRequestsController;
    public SentRequestsController sentRequestsController;
    public ChatController chatController;
    public Stage primaryStage;
    public Scene mainScene;
    public UserService userService;

    public GUI(RegisterController registerController, LoginController loginController, MainController mainController,
               ScreenController screenController, Stage primaryStage, Scene mainScene, UserService userService,
               FriendRequestsController friendRequestsController, SentRequestsController sentRequestsController, ChatController chatController) {
        this.registerController = registerController;
        this.loginController = loginController;
        this.mainController = mainController;
        this.screenController = screenController;
        this.primaryStage = primaryStage;
        this.mainScene = mainScene;
        this.userService = userService;
        this.friendRequestsController = friendRequestsController;
        this.sentRequestsController = sentRequestsController;
        this.chatController = chatController;
    }

    public void refreshUserService() {
        this.userService = new UserService(new UserDbRepository(url, username, password, new UserValidator()),
                new FriendshipDbRepository(url, username, password, new FriendshipValidator()), new MessageDbRepository(url, username, password));
    }
}
