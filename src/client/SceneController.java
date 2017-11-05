package client;

import java.io.IOException;

import common.Request;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SceneController {
	private static SceneController sceneController = null;

	private Stage stage;
	private Stage newRoomModalStage;

	private Scene LobbyScene;
	private Scene RoomScene;
	private Scene startScene;
	private Scene newRoomModalScene;
	private Scene gameRoomScene;
	private Scene gameScene;
	private Scene resultScene;

	private AnimationTimer gameTimer;

	private Controller controller;
	private LobbyViewController lobbyViewController;
	private RoomViewController roomViewController;
	private startViewController startViewController;
	private NewRoomModalController newRoomModalController;
	private GameRoomViewController gameRoomViewController;
	private GameViewController gameViewController;
	private ResultModalController resultModalController;

	private SceneController(){
	}

	public void init(Stage stage, AnimationTimer timer, Controller controller) throws IOException {
		FXMLLoader loader = new FXMLLoader();

		loader.setLocation(this.getClass().getResource("view/startView.fxml"));
		AnchorPane anchornPane = loader.load();
		startViewController = loader.getController();
		startScene = new Scene(anchornPane);

		this.stage = stage;
		this.controller = controller;
		this.gameTimer = timer;
		this.gameTimer.start();

		this.setStartScene();

		startViewController.setProgress(0.3);

		loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("view/lobbyView.fxml"));			 anchornPane = loader.load();
		lobbyViewController = loader.getController();
		LobbyScene = new Scene(anchornPane);

		startViewController.setProgress(0.4);

		loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("view/roomView.fxml"));
		anchornPane = loader.load();
		roomViewController = loader.getController();
		RoomScene = new Scene(anchornPane);

		startViewController.setProgress(0.5);

		loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("view/newRoomModal.fxml"));
		DialogPane dialog = loader.load();
		newRoomModalController = loader.getController();
		newRoomModalScene = new Scene(dialog);

		startViewController.setProgress(0.6);

		loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("view/gameRoomView.fxml"));
		anchornPane = loader.load();
		gameRoomViewController = loader.getController();
		gameRoomScene = new Scene(anchornPane);

		startViewController.setProgress(0.7);

		loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("view/gameView.fxml"));
		anchornPane = loader.load();
		gameViewController = loader.getController();
		gameScene = new Scene(anchornPane);
		startViewController.setProgress(0.8);

		loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("view/resultModal.fxml"));
		VBox vBox = loader.load();
		resultModalController = loader.getController();
		resultScene = new Scene(vBox);

		startViewController.setProgress(0.9);
	}

	public static SceneController getSceneController(){
		if ( sceneController == null ){
			sceneController = new SceneController();
		}
		return sceneController;
	}

	public void setLobbyScene(){
		this.stage.setTitle("Select lobby");
		this.stage.setScene(this.LobbyScene);
		this.stage.show();
		stage.centerOnScreen();
	}

	public void setRoomScene(){
		this.stage.setScene(this.RoomScene);
		this.stage.setTitle("Select room");
		this.stage.show();
		stage.centerOnScreen();
	}

	public void setStartScene(){
		this.stage.setTitle("Starting..");
		this.stage.setScene(this.startScene);
		this.stage.show();
		stage.centerOnScreen();
		getController().getClient().sendRequest(new Request(100, null));
	}

	public void setGameRoomScene(){
		this.stage.setTitle("Game room");
		this.stage.setScene(this.gameRoomScene);
		this.stage.show();
		stage.centerOnScreen();
		getController().getClient().sendRequest(new Request(306, null));
	}

	public void setGameScene() {
		this.stage.setTitle("Chess TIME!!");
		this.stage.setScene(this.gameScene);
		this.stage.show();
		stage.centerOnScreen();
	}

	public Controller getController(){
		return controller;
	}

	public LobbyViewController getLobbyViewController() {
		return lobbyViewController;
	}

	public RoomViewController getRoomViewController() {
		return roomViewController;
	}

	public startViewController getStartViewController() {
		return startViewController;
	}

	public Stage getStage(){
		return this.stage;
	}

	public Scene getGameRoomScene(){
		return this.gameRoomScene;
	}

	public Scene getNewRoomModalScene() {
		return newRoomModalScene;
	}

	public Scene getGameScene() {
		return gameScene;
	}

	public Scene getResultScene(){
		return this.resultScene;
	}

	public Stage getNewRoomModalStage() {
		return newRoomModalStage;
	}

	public void setNewRoomModalStage(Stage newRoomModalStage) {
		this.newRoomModalStage = newRoomModalStage;
	}

	public NewRoomModalController getNewRoomModalController() {
		return newRoomModalController;
	}
	public GameRoomViewController getGameRoomViewControler(){
		return this.gameRoomViewController;
	}

	public GameViewController getGameViewController() {
		return gameViewController;
	}

	public ResultModalController getResultModalController(){
		return this.resultModalController;
	}
}
