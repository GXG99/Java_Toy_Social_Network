package socialnetwork.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import socialnetwork.domain.Message;
import socialnetwork.dto.FriendDTO;
import socialnetwork.dto.MessageDTO;
import socialnetwork.service.UserService;
import socialnetwork.utils.Constants;
import socialnetwork.utils.observer.Observable;

import java.util.List;

public class ChatController {

    public TableView<FriendDTO> tableViewFriends;
    public TextArea textAreaChat;
    public TextField textFieldMessage;
    ObservableList<FriendDTO> model = FXCollections.observableArrayList();

    public TableColumn<FriendDTO, String> tableColumnID;
    public TableColumn<FriendDTO, String> tableColumnFirstName;
    public TableColumn<FriendDTO, String> tableColumnLastName;

    private GUI mainGUI;
    private UserService service;

    public void setMainGUI(GUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    @FXML
    public void initialize() {
        tableColumnID.setCellValueFactory(new PropertyValueFactory<FriendDTO, String>("id"));
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<FriendDTO, String>("FirstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<FriendDTO, String>("LastName"));
        textAreaChat.setWrapText(true);
    }

    public void setService(UserService service) {
        this.service = service;
        service.loadAllMessages();
        initFriends();
    }

    private void initFriends() {
        List<FriendDTO> friendDTOList = service.getFriends();
        model.setAll(friendDTOList);
        tableViewFriends.setItems(model);
    }

    public void onBackClick(MouseEvent mouseEvent) {
        mainGUI.screenController.activate("main");
        textAreaChat.clear();
    }

    public void onEnterPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            Message message = new Message(service.getLoggedUser().getId(), tableViewFriends.getSelectionModel().getSelectedItem().getId(), textFieldMessage.getText());
            service.sendMessage(message);
            String text = "[" + message.getDate().format(Constants.DATE_TIME_FORMATTER) + "] " +
                    service.getLoggedUser().getFirstName() + " " +
                    service.getLoggedUser().getLastName() + ": " + message.getMessage() + "\n";
            textAreaChat.setText(textAreaChat.getText() + text);
            textFieldMessage.clear();
        }
    }

    public void onSendClick(MouseEvent mouseEvent) {
        if (!textFieldMessage.getText().isEmpty()) {
            Message message = new Message(service.getLoggedUser().getId(), tableViewFriends.getSelectionModel()
                    .getSelectedItem().getId(), textFieldMessage.getText());
            service.sendMessage(message);
            String text = "[" + message.getDate().format(Constants.DATE_TIME_FORMATTER) + "] " +
                    service.getLoggedUser().getFirstName() + " " +
                    service.getLoggedUser().getLastName() + ": " + message.getMessage() + "\n";
            textAreaChat.setText(textAreaChat.getText() + text);
            textFieldMessage.clear();
        }
    }

    public void onMouseClick(MouseEvent mouseEvent) {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY) &&
                tableViewFriends.getSelectionModel().getSelectedItem() != null) {
            textAreaChat.clear();
            List<MessageDTO> messageList = service.loadMessages(tableViewFriends.getSelectionModel()
                    .getSelectedItem().getId());
            messageList.forEach(message -> {
                textAreaChat.setText(textAreaChat.getText() + message.toString());
            });
        }
    }
}
