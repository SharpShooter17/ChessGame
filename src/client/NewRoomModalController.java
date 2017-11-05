package client;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class NewRoomModalController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField roomName;

    public String getRoomName(){
    	return this.roomName.getText();
    }

    @FXML
    void addNewRoomButtonClicked(ActionEvent event) {
    	SceneController.getSceneController().getNewRoomModalStage().close();
    }

    @FXML
    void cancelButtonClicked(ActionEvent event) {
    	roomName.setText("");
    	addNewRoomButtonClicked(event);
    }

    @FXML
    void initialize() {
        assert roomName != null : "fx:id=\"roomName\" was not injected: check your FXML file 'newRoomModal.fxml'.";

    }
}
