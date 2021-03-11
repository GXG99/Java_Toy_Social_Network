package socialnetwork.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import socialnetwork.domain.User;
import socialnetwork.service.UserService;
import java.util.List;

public class AddFriendController {

    public TableView<User> tableView;
    public TableColumn<User, String> tableColumnFirstName;
    public TableColumn<User, String> tableColumnLastName;
    public TableColumn<User, String> tableColumnEmail;
    public Button cancelButton;

    ObservableList<User> model = FXCollections.observableArrayList();
    UserService service;

    Stage stage;
    
    public void setService(UserService service, Stage stage) {
        this.service = service;
        this.stage = stage;
        initTableView();
    }

    private void initTableView() {
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        tableColumnEmail.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
        initUsers();
    }

    private void initUsers() {
        List<User> users = service.getNonFriends();
        model.setAll(users);
        tableView.setItems(model);
    }

    public void onAddClick(MouseEvent mouseEvent) {
        User selectedUser = tableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            service.addFriend(service.getLoggedUser().getId(), selectedUser.getId());
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Request sent");
            alert.show();
        }
    }

    public void onCancelClick(MouseEvent mouseEvent) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
