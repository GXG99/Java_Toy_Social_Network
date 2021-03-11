package socialnetwork.controller;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import socialnetwork.MainFX;
import socialnetwork.domain.User;
import socialnetwork.repository.exceptions.RepositoryException;
import socialnetwork.service.UserService;


import java.io.IOException;

public class LoginController {

    private GUI mainGUI;

    @FXML
    private TextField emailTextField;

    @FXML
    private Button loginButton;

    public void setMainGUI(GUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    public void onMouseClick(MouseEvent mouseEvent) throws IOException {
        try {
            mainGUI.userService.logIn(emailTextField.getText());
            User loggedUser = mainGUI.userService.getLoggedUser();
            mainGUI.mainController.setLoggedUser("Welcome " + loggedUser.getFirstName() + " " + loggedUser.getLastName());
            mainGUI.screenController.activate("main");
            mainGUI.primaryStage.sizeToScene();
        } catch (RepositoryException exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR, exception.getMessage());
            alert.show();
        }
    }

    public void onRegisterClick(MouseEvent mouseEvent) {
        mainGUI.screenController.activate("register");
    }
}
