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

public class FriendRequestsController {
    public TableView<FriendRequestDTO> tableView;
    ObservableList<FriendRequestDTO> model = FXCollections.observableArrayList();
    public TableColumn<FriendRequestDTO, String> tableColumnId;
    public TableColumn<FriendRequestDTO, String> tableColumnFirstName;
    public TableColumn<FriendRequestDTO, String> tableColumnLastName;
    public TableColumn<FriendRequestDTO, String> tableColumnDate;
    private GUI mainGUI;
    private UserService service;

    public void setMainGUI(GUI mainGUI) {
        this.mainGUI = mainGUI;
    }



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

    public void initRequests() {
        List<FriendRequestDTO> friendRequestDTOList = service.getFriendRequests();
        model.setAll(friendRequestDTOList);
        tableView.setItems(model);
    }

    public void onAcceptClick(MouseEvent mouseEvent) {
        FriendRequestDTO selectedRequest = tableView.getSelectionModel().getSelectedItem();
        if (selectedRequest != null) {
            service.acceptRequest(selectedRequest.getId(), service.getLoggedUser().getId());
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Request accepted");
            alert.show();
        }
    }

    public void onDeclineClick(MouseEvent mouseEvent) {
        FriendRequestDTO selectedRequest = tableView.getSelectionModel().getSelectedItem();
        if (selectedRequest != null) {
            service.declineRequest(selectedRequest.getId(), service.getLoggedUser().getId());
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Request declined");
            alert.show();
        }
    }

    public void onShowSentClick(MouseEvent mouseEvent) {
        mainGUI.sentRequestsController.setService(service);
        mainGUI.screenController.activate("sentRequests");
    }

    public void onBackClick(MouseEvent mouseEvent) {
        mainGUI.screenController.activate("main");
    }
}
