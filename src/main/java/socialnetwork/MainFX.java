package socialnetwork;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import socialnetwork.config.ApplicationContext;
import socialnetwork.controller.*;
import socialnetwork.domain.validators.FriendshipValidator;
import socialnetwork.domain.validators.UserValidator;
import socialnetwork.repository.database.FriendshipDbRepository;
import socialnetwork.repository.database.MessageDbRepository;
import socialnetwork.repository.database.UserDbRepository;
import socialnetwork.service.UserService;

import java.io.IOException;

public class MainFX extends Application {

    private GUI mainGUI;
    private UserService userService;
    private LoginController loginController;
    private MainController mainController;
    private RegisterController registerController;
    private FriendRequestsController friendRequestsController;
    private SentRequestsController sentRequestsController;
    private ChatController chatController;

    public final String url = ApplicationContext.getPROPERTIES().getProperty("database.socialnetwork.url");
    public final String username = ApplicationContext.getPROPERTIES().getProperty("database.socialnetwork.username");
    public final String password = ApplicationContext.getPROPERTIES().getProperty("database.socialnetwork.password");
    public ScreenController screenController;
    public Scene mainScene;

    @Override
    public void init() throws IOException {

        //Load service
        userService = new UserService(new UserDbRepository(url, username, password, new UserValidator()),
                new FriendshipDbRepository(url, username, password, new FriendshipValidator()), new MessageDbRepository(url, username, password));
        mainScene = new Scene(new Pane());
        screenController = new ScreenController(mainScene);

        //Load login controller
        FXMLLoader loginLoader = new FXMLLoader();
        loginLoader.setLocation(getClass().getResource("/views/loginView.fxml"));
        screenController.addScreen("login", loginLoader.load());
        loginController = loginLoader.getController();

        //Load register controller
        FXMLLoader registerLoader = new FXMLLoader();
        registerLoader.setLocation(getClass().getResource("/views/registerView.fxml"));
        screenController.addScreen("register", registerLoader.load());
        registerController = registerLoader.getController();

        //Load main controller
        FXMLLoader mainLoader = new FXMLLoader();
        mainLoader.setLocation(getClass().getResource("/views/mainView.fxml"));
        screenController.addScreen("main", mainLoader.load());
        mainController = mainLoader.getController();

        //Load friend requests controller
        FXMLLoader friendRequestLoader = new FXMLLoader();
        friendRequestLoader.setLocation(getClass().getResource("/views/friendRequests.fxml"));
        screenController.addScreen("requests", friendRequestLoader.load());
        friendRequestsController = friendRequestLoader.getController();

        //Load sent requests controller
        FXMLLoader sentRequestLoader = new FXMLLoader();
        sentRequestLoader.setLocation(getClass().getResource("/views/sentRequests.fxml"));
        screenController.addScreen("sentRequests", sentRequestLoader.load());
        sentRequestsController = sentRequestLoader.getController();

        //Load chat controller
        FXMLLoader chatLoader = new FXMLLoader();
        chatLoader.setLocation(getClass().getResource("/views/chatView.fxml"));
        screenController.addScreen("chat", chatLoader.load());
        chatController = chatLoader.getController();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        mainGUI = new GUI(registerController, loginController, mainController, screenController, primaryStage,
                mainScene, userService, friendRequestsController, sentRequestsController, chatController);
        loginController.setMainGUI(mainGUI);
        mainController.setMainGUI(mainGUI);
        registerController.setMainGUI(mainGUI);
        friendRequestsController.setMainGUI(mainGUI);
        sentRequestsController.setMainGUI(mainGUI);
        chatController.setMainGUI(mainGUI);
        screenController.activate("login");
        primaryStage.setTitle("Social network");
        primaryStage.setScene(mainScene);
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    public static void main(String[] args) {
        launch(args);
    }
}