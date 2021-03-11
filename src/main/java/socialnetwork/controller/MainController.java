package socialnetwork.controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import socialnetwork.dto.FriendDTO;
import socialnetwork.service.UserService;
import socialnetwork.utils.observer.Observer;

import java.io.IOException;
import java.util.List;

public class MainController implements EventHandler<ActionEvent>, Observer {

    ObservableList<FriendDTO> model = FXCollections.observableArrayList();
    UserService service;
    private GUI mainGUI;
    public Label loggedUser;
    public Pane textPane;

    @FXML
    private TableColumn<FriendDTO, String> tableColumnId;
    @FXML
    private TableColumn<FriendDTO, String> tableColumnFirstName;
    @FXML
    private TableColumn<FriendDTO, String> tableColumnLastName;
    @FXML
    private TableColumn<FriendDTO, String> tableColumnDate;
    @FXML
    private TableView<FriendDTO> tableView;

    public void setMainGUI(GUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    @FXML
    public void initialize() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<FriendDTO, String>("Id"));
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<FriendDTO, String>("FirstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<FriendDTO, String>("LastName"));
        tableColumnDate.setCellValueFactory(new PropertyValueFactory<FriendDTO, String>("Date"));
    }


    public void setLoggedUser(String user) {
        loggedUser.setText(user);
        loggedUser.layoutXProperty().bind(textPane.widthProperty().subtract(loggedUser.widthProperty()).divide(2));
        loggedUser.layoutYProperty().bind(textPane.heightProperty().subtract(loggedUser.heightProperty()).divide(2));
        initLogged();
    }

    private void initLogged() {
        service = mainGUI.userService;
        service.addObserver(this);
        initFriends();
    }

    private void initFriends() {
        List<FriendDTO> friendDTOList = service.getFriends();
        model.setAll(friendDTOList);
        tableView.setItems(model);
    }

    public void onLogOutClick(MouseEvent mouseEvent) {
        mainGUI.refreshUserService();
        mainGUI.primaryStage.setWidth(650d);
        mainGUI.primaryStage.setHeight(450d);
        mainGUI.screenController.activate("login");
    }

    public void onAddClick(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/addFriendView.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            //
            Stage addFriendStage = new Stage();
            addFriendStage.setTitle("Add friend");
            addFriendStage.initOwner(mainGUI.primaryStage);
            addFriendStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            addFriendStage.setScene(scene);
            AddFriendController addFriendController = loader.getController();
            addFriendController.setService(service, addFriendStage);
            addFriendStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onRemoveClick(MouseEvent mouseEvent) {
        FriendDTO selectedFriend = tableView.getSelectionModel().getSelectedItem();
        if (selectedFriend != null) {
            service.removeFriend(service.getLoggedUser().getId(), selectedFriend.getId());
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Friend was deleted");
            alert.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "A message must be selected");
            alert.show();
        }
    }

    public void onFriendRequestsClick(MouseEvent mouseEvent) {
        mainGUI.friendRequestsController.setService(service);
        mainGUI.screenController.activate("requests");
    }

    public void onChatClick(MouseEvent mouseEvent) {
        mainGUI.chatController.setService(service);
        mainGUI.screenController.activate("chat");
        mainGUI.primaryStage.sizeToScene();
    }

    @Override
    public void handle(ActionEvent event) {

    }

    @Override
    public void update() {
        reload();
    }

    private void reload() {
        initFriends();
    }


}
