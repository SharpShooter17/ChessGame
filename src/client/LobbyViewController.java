package client;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import common.Request;
import common.Space;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class LobbyViewController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<SpaceTableView> tableViewLobby;

    @FXML
    private TableColumn<SpaceTableView, Integer> idCol;

    @FXML
    private TableColumn<SpaceTableView, String> LobbyNameColumn;

    @FXML
    private TableColumn<SpaceTableView, Integer> numberColumn;

    @FXML
    private Button enterToLobbyButton;

    @FXML
    private Button exitButton;

    @FXML
    void enterButtonClicked(ActionEvent event) {
    	SpaceTableView tab = tableViewLobby.getSelectionModel().getSelectedItem();
    	if (tab != null) {
    		Request req = new Request(101, new Space(tab.getName(), tab.getId(), tab.getPlayers()));
    		SceneController.getSceneController().getController().getClient().sendRequest(req);
    	}
    }

    @FXML
    void exitButtonClicked(ActionEvent event) {
    	SceneController.getSceneController().getController().getClient().closeConnection();
    	Platform.exit();
    }

    private ObservableList<SpaceTableView> data;

    @FXML
    void initialize() {
    	assert tableViewLobby != null : "fx:id=\"tableViewLobby\" was not injected: check your FXML file 'lobbyView.fxml'.";
        assert idCol != null : "fx:id=\"idCol\" was not injected: check your FXML file 'lobbyView.fxml'.";
        assert LobbyNameColumn != null : "fx:id=\"LobbyNameColumn\" was not injected: check your FXML file 'lobbyView.fxml'.";
        assert numberColumn != null : "fx:id=\"numberColumn\" was not injected: check your FXML file 'lobbyView.fxml'.";
        assert enterToLobbyButton != null : "fx:id=\"enterToLobbyButton\" was not injected: check your FXML file 'lobbyView.fxml'.";
        assert exitButton != null : "fx:id=\"exitButton\" was not injected: check your FXML file 'lobbyView.fxml'.";


        tableViewLobby.setRowFactory( tv -> {
            TableRow<SpaceTableView> row = new TableRow<SpaceTableView>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                	enterButtonClicked(null);
                }
            });
            return row ;
        });

        data = FXCollections.observableArrayList();
        tableViewLobby.itemsProperty().set(data);
        idCol.setCellValueFactory( new PropertyValueFactory<SpaceTableView, Integer>("id") );
        LobbyNameColumn.setCellValueFactory( new PropertyValueFactory<SpaceTableView, String>("name") );
        numberColumn.setCellValueFactory( new PropertyValueFactory<SpaceTableView, Integer>("players") );
    }

    private void clearData(){
    	data.clear();
    }

    public void addLobby(List<SpaceTableView> list){
    	clearData();
    	data.addAll(list);
    }
}
