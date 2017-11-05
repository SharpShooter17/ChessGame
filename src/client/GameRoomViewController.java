package client;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import common.Request;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class GameRoomViewController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<GameRoomTableView> whiteTableView;

    @FXML
    private TableColumn<GameRoomTableView, String> whiteNickCol;

    @FXML
    private TableColumn<GameRoomTableView, Boolean> whiteReadyCol;

    @FXML
    private TableView<GameRoomTableView> blackTableView;

    @FXML
    private TableColumn<GameRoomTableView, String> blackNickCol;

    @FXML
    private TableColumn<GameRoomTableView, Boolean> blackReadyCol;

    @FXML
    private Button readyButton;

    @FXML
    void goToLobbyButtonClick(ActionEvent event) {
    	SceneController.getSceneController().getController().getClient().sendRequest(new Request(305, null));
    }

    @FXML
    void readyToPlayButtonClick(ActionEvent event) {
    	if ( this.readyButton.getText().equals("Ready to play") ) {
    		SceneController.getSceneController().getController().getClient().sendRequest(new Request(300, null));
    	} else {
    		SceneController.getSceneController().getController().getClient().sendRequest(new Request(301, null));
    	}
    }

    public void setText(boolean isReady){
    	if (isReady){
    		this.readyButton.setText("Not ready");
    	} else {
    		this.readyButton.setText("Ready to play");
    	}
    }

    private ObservableList<GameRoomTableView> whiteTable;
    private ObservableList<GameRoomTableView> blackTable;

    public void clearWhiteTable(){
    	this.whiteTable.clear();
    }
    public void clearBlackTable(){
    	this.blackTable.clear();
    }
    public void addToWhiteTable(GameRoomTableView item){
    	this.whiteTable.add(item);
    }
    public void addToBlackTable(GameRoomTableView item){
    	this.blackTable.add(item);
    }

    @FXML
    void initialize() {
        assert whiteTableView != null : "fx:id=\"whiteTableView\" was not injected: check your FXML file 'gameRoomView.fxml'.";
        assert whiteNickCol != null : "fx:id=\"whiteNickCol\" was not injected: check your FXML file 'gameRoomView.fxml'.";
        assert whiteReadyCol != null : "fx:id=\"whiteReadyCol\" was not injected: check your FXML file 'gameRoomView.fxml'.";
        assert blackTableView != null : "fx:id=\"blackTableView\" was not injected: check your FXML file 'gameRoomView.fxml'.";
        assert blackNickCol != null : "fx:id=\"blackNickCol\" was not injected: check your FXML file 'gameRoomView.fxml'.";
        assert blackReadyCol != null : "fx:id=\"blackReadyCol\" was not injected: check your FXML file 'gameRoomView.fxml'.";
        assert readyButton != null : "fx:id=\"redyButton\" was not injected: check your FXML file 'gameRoomView.fxml'.";

        whiteTable = FXCollections.observableArrayList();
        blackTable = FXCollections.observableArrayList();

        whiteTableView.itemsProperty().set(whiteTable);
        blackTableView.itemsProperty().set(blackTable);

        whiteNickCol.setCellValueFactory( new PropertyValueFactory<GameRoomTableView, String>("name") );
        whiteReadyCol.setCellValueFactory(new PropertyValueFactory<GameRoomTableView, Boolean>("ready"));
        blackNickCol.setCellValueFactory( new PropertyValueFactory<GameRoomTableView, String>("name") );
        blackReadyCol.setCellValueFactory(new PropertyValueFactory<GameRoomTableView, Boolean>("ready"));

        whiteReadyCol.setCellFactory(col -> new TableCell<GameRoomTableView, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
            	if (item != null)
            		setText( item ? "Ready" : "Not ready" );
            }

        });

        blackReadyCol.setCellFactory(col -> new TableCell<GameRoomTableView, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
            	if (item != null)
            		setText( item ? "Ready" : "Not ready" );
            }

        });
    }
}
