package socialnetwork.controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import socialnetwork.dto.FriendRequestDTO;
import socialnetwork.service.UserService;

import java.util.List;

public class SentRequestsController {

    ObservableList<FriendRequestDTO> model = FXCollections.observableArrayList();
    public TableColumn<FriendRequestDTO, String> tableColumnId;
    public TableColumn<FriendRequestDTO, String> tableColumnFirstName;
    public TableColumn<FriendRequestDTO, String> tableColumnLastName;
    public TableColumn<FriendRequestDTO, String> tableColumnDate;
    public TableView<FriendRequestDTO> tableView;
    private GUI mainGUI;
    private UserService service;

    @FXML
    public void initialize() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<FriendRequestDTO, String>("Id"));
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<FriendRequestDTO, String>("FirstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<FriendRequestDTO, String>("LastName"));
        tableColumnDate.setCellValueFactory(new PropertyValueFactory<FriendRequestDTO, String>("Date"));
    }

    public void setService(UserService service) {
        this.service = service;
        initRequests();
    }

    private void initRequests() {
        List<FriendRequestDTO> sentRequests = service.getSentRequests();
        model.setAll(sentRequests);
        tableView.setItems(model);
    }

    public void setMainGUI(GUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    public void onBackClick(MouseEvent mouseEvent) {
        mainGUI.screenController.activate("requests");
    }

    public void onRemoveClick(MouseEvent mouseEvent) {
        FriendRequestDTO selectedRequest = tableView.getSelectionModel().getSelectedItem();
        service.removeRequest(service.getLoggedUser().getId(), selectedRequest.getId());
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Friend request deleted");
        alert.show();
    }
}
