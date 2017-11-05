package client;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import chessEngine.Bishop;
import chessEngine.Board;
import chessEngine.Figure;
import chessEngine.Horse;
import chessEngine.King;
import chessEngine.NoFigureOnPosition;
import chessEngine.NoKingOnTheBoardException;
import chessEngine.Pawn;
import chessEngine.Position;
import chessEngine.PositionNotValidException;
import chessEngine.Queen;
import chessEngine.Rook;
import common.Move;
import common.Request;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GameViewController {
	public static final int tileSize = 96;
	private Board board;
	private Image textures[];

	enum IMAGEPATTERN {BISHOP, HORSE, KING, PAWN, QUEEN, ROOK};

	public void move(Position from, Position to){
		try {
			board.move(from, to);
		} catch (PositionNotValidException e) {
			e.printStackTrace();
		} catch (NoFigureOnPosition e) {
			e.printStackTrace();
		}
	}

	private Position from;
	private Position to;

	private void setPosition(int x, int y) throws PositionNotValidException, NoFigureOnPosition, NoKingOnTheBoardException{
		if ( this.from == null ){
			Position p = new Position(x, y);
			if ( this.getBoard().getFigureFrom(p) != null ){
				this.from = p;
				this.drawLight(x, y);
			}
		} else if (from.equals(new Position(x,y))){
			this.from = null;
			this.draw();
		} else if (this.to == null){
			this.to = new Position(x, y);
			SceneController.getSceneController().getController().getClient().sendRequest(new Request(304, new Move(from, to)));
			this.from = null;
			this.to = null;
			this.draw();
		}
	}


    @FXML
    private Label youAreLabel;

    @FXML
    private Button castlingShortButton;

    @FXML
    private Button castlingLongButton;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Canvas canvas;

    private GraphicsContext gc;

    @FXML
    void canvasMouseClicked(MouseEvent event) {
    	System.out.println("X: " + event.getSceneX() + " Y: " + event.getSceneY() );
    }

    @FXML
    void castlingLongButtonClicked(ActionEvent event) {
    	SceneController.getSceneController().getController().getClient().sendRequest(new Request(308, SceneController.getSceneController().getController().getClient().getMyId()));
    }

    @FXML
    void castlingShortButtonClicked(ActionEvent event) {
    	SceneController.getSceneController().getController().getClient().sendRequest(new Request(307, SceneController.getSceneController().getController().getClient().getMyId()));
    }

    @FXML
    void giveUpButtonClicked(ActionEvent event) {
    	SceneController.getSceneController().getController().getClient().sendRequest(new Request(302, null));
    }

    private void drawBoard(){
    	for (int i = 0; i < Board.BOARDX; i++){
    		for (int j = 0; j < Board.BOARDY; j++){
    			if ((i + j) % 2 == 0) {
    				gc.setFill(Color.WHITE);
    			} else {
    				gc.setFill(Color.BLACK);
    			}
    	    	gc.fillRect(i * tileSize, j * tileSize, tileSize, tileSize);
    		}
    	}
    }

    private void drawFigures(){
    	for (int x = 0; x < Board.BOARDX; x++){
    		for (int y = 0; y < Board.BOARDY; y++){
    			Figure fig = board.getFigureFrom(new Position(x,y));
    			if (fig != null) {
	    			ImagePattern ip = getImagePattern( fig );
	    			gc.setFill(ip);
	    			gc.fillRect(x*tileSize, y*tileSize, tileSize, tileSize);

    			}
    		}
    	}
    }
    private void drawLight(int x, int y) throws PositionNotValidException, NoFigureOnPosition, NoKingOnTheBoardException{
    	gc.setLineWidth(5);
    	gc.setStroke(Color.CORNFLOWERBLUE);
    	gc.strokeRect(x*tileSize, y*tileSize, tileSize, tileSize);
    	Position pos = new Position(x,y);
    	List<Position> list = this.board.makeListOfAvaivableMovements(pos);
    	//list = this.board.checkPositionsIfIsCheck(pos, list);
    	gc.setStroke(Color.LIGHTGREEN);
    	for (Position position : list) {
    		gc.strokeRect(position.getX()*tileSize, position.getY()*tileSize, tileSize, tileSize);
		}
    }

    private int mousePositionToCord(double pos){
    	return (int) pos / tileSize;
    }

    public void draw() {
    	drawBoard();
        drawFigures();
    }

    @FXML
    void initialize() {
        assert canvas != null : "fx:id=\"canvas\" was not injected: check your FXML file 'gameView.fxml'.";
        assert youAreLabel != null : "fx:id=\"youAreLabel\" was not injected: check your FXML file 'gameView.fxml'.";
        assert castlingShortButton != null : "fx:id=\"castlingShortButton\" was not injected: check your FXML file 'gameView.fxml'.";
        assert castlingLongButton != null : "fx:id=\"castlingLongButton\" was not injected: check your FXML file 'gameView.fxml'.";

        this.canvas.setWidth(Board.BOARDX*tileSize);
        this.canvas.setHeight(Board.BOARDY*tileSize);
        gc = canvas.getGraphicsContext2D();

        canvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	int x = mousePositionToCord( event.getSceneX() );
                int y = mousePositionToCord( event.getSceneY() );
                try {
					setPosition(x,y);
				} catch (PositionNotValidException | NoFigureOnPosition | NoKingOnTheBoardException e) {
					e.printStackTrace();
				}
            }
        });
        loadTextures();
    }

    private void loadTextures(){
    	textures = new Image[12];
    	this.textures[0] = new Image("file:client/data/whiteBishop.png");
    	this.textures[1] = new Image("file:client/data/whiteHorse.png");
    	this.textures[2] = new Image("file:client/data/whiteKing.png");
    	this.textures[3] = new Image("file:client/data/whitePawn.png");
    	this.textures[4] = new Image("file:client/data/whiteQueen.png");
    	this.textures[5] = new Image("file:client/data/whiteRook.png");
    	this.textures[6] = new Image("file:client/data/blackBishop.png");
    	this.textures[7] = new Image("file:client/data/blackHorse.png");
    	this.textures[8] = new Image("file:client/data/blackKing.png");
    	this.textures[9] = new Image("file:client/data/blackPawn.png");
    	this.textures[10] = new Image("file:client/data/blackQueen.png");
    	this.textures[11] = new Image("file:client/data/blackRook.png");
    }

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public void setColor(chessEngine.Color color){
		this.youAreLabel.setText( color == chessEngine.Color.White ? new String("White") : new String("Black") );
	}

	private ImagePattern getImagePattern(Figure figure){
		int add = (figure.getColor() == chessEngine.Color.White ? 0 : 6);
		int numOfTexture = 0;

		if (figure instanceof Pawn){
			numOfTexture = 3;
		} else if (figure instanceof King){
			numOfTexture = 2;
		} else if (figure instanceof Horse){
			numOfTexture = 1;
		} else if (figure instanceof Bishop) {
			numOfTexture = 0;
		} else if (figure instanceof Rook){
			numOfTexture = 5;
		} else if (figure instanceof Queen){
			numOfTexture = 4;
		}
		return new ImagePattern(textures[add + numOfTexture]);
	}

	public void modal(String text){
    	Stage stg = new Stage();
    	SceneController.getSceneController().getResultModalController().setStage(stg);
    	SceneController.getSceneController().getResultModalController().setResultText(text);
    	stg.initModality(Modality.APPLICATION_MODAL);
    	stg.initStyle(StageStyle.UNDECORATED);
    	stg.setScene( SceneController.getSceneController().getResultScene());
    	stg.show();
    	SceneController.getSceneController().setGameRoomScene();
	}
}
