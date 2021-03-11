package socialnetwork.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import socialnetwork.domain.User;
import socialnetwork.repository.Repository;
import socialnetwork.repository.exceptions.RepositoryException;

public class RegisterController {


    public TextField emailTextField;
    public TextField lastNameTextField;
    public TextField firstNameTextField;
    public Button registerButton;
    private GUI mainGUI;

    public void onRegisterClick(MouseEvent mouseEvent) {
        try {
            if (emailTextField.getText().isEmpty() || lastNameTextField.getText().isEmpty()
                    || firstNameTextField.getText().isEmpty()) {
                Alert empty = new Alert(Alert.AlertType.INFORMATION, "Fields cannot be empty");
                empty.show();
            } else {
                User newUser = new User(firstNameTextField.getText(), lastNameTextField.getText());
                newUser.setEmail(emailTextField.getText());
                mainGUI.userService.addUser(newUser);
                mainGUI.screenController.activate("login");
                Alert succes = new Alert(Alert.AlertType.INFORMATION, "User added successfully!");
                succes.show();
            }
        } catch (RepositoryException exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR, exception.getMessage());
            alert.show();
        }
    }

    public void setMainGUI(GUI mainGUI) {
        this.mainGUI = mainGUI;
    }
}
