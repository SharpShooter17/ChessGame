package client;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ResultModalController {
	private Stage stage;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label resultLabel;

    @FXML
    void goToRoomButtonClicked(ActionEvent event) {
    	this.stage.close();
    }

    @FXML
    void initialize() {
        assert resultLabel != null : "fx:id=\"resultLabel\" was not injected: check your FXML file 'resultModal.fxml'.";
    }

    public void setStage(Stage stg){
    	this.stage = stg;
    }

    public void setResultText(String text){
    	this.resultLabel.setText(text);
    }
}
