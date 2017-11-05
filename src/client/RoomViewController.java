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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import server.Room;

public class RoomViewController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<SpaceTableView> tableView;

    @FXML
    private TableColumn<SpaceTableView, Integer> idCol;

    @FXML
    private TableColumn<SpaceTableView, String> nameCol;

    @FXML
    private TableColumn<SpaceTableView, Integer> playersCol;

    @FXML
    private Button enterButton;

    @FXML
    private Button lobbyButton;

    @FXML
    void createNewRoomButtonClicked(ActionEvent event) {
    	SceneController.getSceneController().setNewRoomModalStage(new Stage());
    	Stage stg = SceneController.getSceneController().getNewRoomModalStage();
    	stg.initModality(Modality.APPLICATION_MODAL);
    	stg.initStyle(StageStyle.UNDECORATED);
    	stg.setScene( SceneController.getSceneController().getNewRoomModalScene());
    	stg.showAndWait();

    	String str = SceneController.getSceneController().getNewRoomModalController().getRoomName();

    	if ( !str.equals("") ){
    		SceneController.getSceneController().getController().getClient().sendRequest(new Request(201, str));
    	}
    }

    @FXML
    void enterToRoomButtonClicked(ActionEvent event) {
    	SpaceTableView tmp = tableView.getSelectionModel().getSelectedItem();
    	if (tmp != null) {
    		SceneController.getSceneController().getController().getClient().sendRequest(new Request(202, tmp.getId()));
    	}
    }

    @FXML
    void goToLobbyButtonClicked(ActionEvent event) {
    	SceneController.getSceneController().getController().getClient().sendRequest(new Request(204, null));
    }

    private ObservableList<SpaceTableView> data;

    public void addRooms(List<SpaceTableView> list){
    	data.clear();
    	data.addAll(list);
    }

    @FXML
    void initialize() {
        assert tableView != null : "fx:id=\"tableView\" was not injected: check your FXML file 'roomView.fxml'.";
        assert idCol != null : "fx:id=\"idCol\" was not injected: check your FXML file 'roomView.fxml'.";
        assert nameCol != null : "fx:id=\"nameCol\" was not injected: check your FXML file 'roomView.fxml'.";
        assert playersCol != null : "fx:id=\"playersCol\" was not injected: check your FXML file 'roomView.fxml'.";
        assert enterButton != null : "fx:id=\"enterButton\" was not injected: check your FXML file 'roomView.fxml'.";
        assert lobbyButton != null : "fx:id=\"lobbyButton\" was not injected: check your FXML file 'roomView.fxml'.";

        data = FXCollections.observableArrayList();
        tableView.itemsProperty().set(data);
        idCol.setCellValueFactory( new PropertyValueFactory<SpaceTableView, Integer>("id") );
        nameCol.setCellValueFactory( new PropertyValueFactory<SpaceTableView, String>("name") );
        playersCol.setCellValueFactory( new PropertyValueFactory<SpaceTableView, Integer>("players") );

        tableView.setRowFactory( tv -> {
            TableRow<SpaceTableView> row = new TableRow<SpaceTableView>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                	enterToRoomButtonClicked(null);
                }
            });
            return row ;
        });

    }
}
