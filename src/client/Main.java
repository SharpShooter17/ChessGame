package client;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

	long time = 500000000;
	long lastNow = 0;

	@Override
	public void start(Stage stage) throws Exception {
		stage.initStyle(StageStyle.DECORATED);
		stage.centerOnScreen();

		Controller controller = new Controller();

		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				controller.getClient().run();
			};
		};
		SceneController.getSceneController().init(stage, timer, controller);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
