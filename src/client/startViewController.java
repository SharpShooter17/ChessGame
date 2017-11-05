package client;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;

public class startViewController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ProgressBar progressBar;

    @FXML
    void initialize() {
        assert progressBar != null : "fx:id=\"progressBar\" was not injected: check your FXML file 'startView.fxml'.";
    }
    public void setProgress(double progress){
    	this.progressBar.setProgress(progress);
    }
    public double getProgress(){
    	return this.progressBar.getProgress();
    }
}
