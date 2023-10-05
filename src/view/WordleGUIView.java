package view;


import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import controller.WordleController;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.WordleModel;
import utilities.Guess;
import utilities.INDEX_RESULT;

/**
 * @author Sangeun Park
 * 
 * Wordle Gui version
 * 
 * It doesn't take mouse input.
 * 
 * It follows the same game logic with the real Wordle.
 * 
 * +start(Stage stage)
 * -createGuessGrid(GridPane gridpaneGuess): GridPane 
 * -createKeyBoardFirst(GridPane gridpaneKeyBoardFirst): GridPane 
 * -createKeyBoardSecond(GridPane gridpaneKeyBoardSecond): GridPane 
 * -createKeyBoardThrid(GridPane gridpaneKeyBoardThird): GridPane 
 * -popupGameOver(int count, String answer)
 * -popupInvalidInput()
 * -rotate(Label tile, int i): SequentialTransition 
 * -updateKeyBoard()
 * +update(Observable o, Object arg)
 */
public class WordleGUIView extends Application implements Observer{

	/* Constants for the scene */
	private static final int SCENE_SIZE = 800;

	/* Constants for grid of letters */
	private static final int GRID_GAP = 5;

	/* Constants for letters in grid */
	private static final int LETTER_FONT_SIZE = 50;
	private static final int LETTER_SQUARE_SIZE = 65;
	private static final int LETTER_BORDER_WIDTH = 2;
	
	
	// USER GUESS
	private static Label[][] label = new Label[6][5]; // for guess grid
	private String input;
	private int count = 0; //count number of user inputs
	private int index = 0; //index of input
	private static String guess = "";
	
	
	// KEYBOARD
	private Label[] firstRow = new Label[10]; 
	private Label[] secondRow = new Label[9];
	private Label[] thridRow = new Label[9];
	
	private String[] firstRowString = new String[] {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"};
	private String[] secondRowString = new String[] {"A", "S", "D", "F", "G", "H", "J", "K", "L"};
	private String[] thridRowString = new String[] {"ENTER", "Z", "X", "C", "V", "B", "N", "M", "⌫"};
	
	private INDEX_RESULT[] guessedCharacter;
	private Guess[] progress;
	
	private ArrayList<String> unguessed = new ArrayList<>();
	private ArrayList<String> correct = new ArrayList<>();
	private ArrayList<String> incorrect = new ArrayList<>();
	private ArrayList<String> wrongIndex = new ArrayList<>();
	
	

	
	public static void main(String[] args) throws FileNotFoundException {
		launch(args);
	}
	
	
	
	
	/**
	 * starts GUI.
	 * 
	 * This method handles user inputs.
	 */
	@Override
	public void start(Stage stage) throws FileNotFoundException {
				
		
		WordleModel model = new WordleModel();
		model.addObserver(this);
		WordleController controller = new WordleController(model);
		
		EventHandler<KeyEvent> eventHandler = new EventHandler<KeyEvent>() {
			
			@Override
			public void handle(KeyEvent ke) {	
				
				input = ke.getCode().getName();
				char letter = input.charAt(0);
				
				int ascii = (char) letter;
				
				if (ke.getCode().equals(KeyCode.DELETE) || ke.getCode().equals(KeyCode.BACK_SPACE)) {
					if (index > 0 && count < 6)
						index--;
					label[count][index].setFont(Font.font("Clear Sans", FontWeight.BOLD, LETTER_FONT_SIZE));
					label[count][index].setTextFill(Color.WHITE);
					label[count][index].setText("");
				} else if (ke.getCode().equals(KeyCode.ENTER) && index == 5 && count < 6) {
					
					guess += label[count][0].getText();
					guess += label[count][1].getText();
					guess += label[count][2].getText();
					guess += label[count][3].getText();
					guess += label[count][4].getText();
					
					try {
						controller.makeGuess(guess.toLowerCase());
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					
					
					if (controller.getAnswer().equals(guess.toLowerCase())) {
						popupGameOver(count, controller.getAnswer());

							// game over without guessing a correct word
					} else if (controller.isGameOver()) {
						Alert alert = new Alert(Alert.AlertType.INFORMATION);
						alert.setTitle("Game Over");
						alert.setContentText("Ahh. You can play it again.");
						alert.showAndWait();
						
					} else {
						//check invalid input
						try {
							if (controller.checkValidWord(guess.toLowerCase()) && controller.validate(guess.toLowerCase())) {
								index = 0;
								count++;
								label[count][index].setFont(Font.font("Clear Sans", FontWeight.BOLD, LETTER_FONT_SIZE));
								label[count][index].setTextFill(Color.WHITE);
								label[count][index].setText(input);
							} else {
								popupInvalidInput();
							}
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
					}
					guess = "";
				} else if (index < 5 && count < 6) {
					
					if (input.length() == 1 && ascii >= 65 && ascii <= 90) {
						label[count][index].setFont(Font.font("Clear Sans", FontWeight.BOLD, LETTER_FONT_SIZE));
						label[count][index].setTextFill(Color.WHITE);
						label[count][index].setText(input);
						index++;
					} 
					
				}
			
			}
			
		};
		
		
		//scene[ vbox [ gridpane [ label ] ] ]
		BorderPane pane = new BorderPane();
		GridPane gridpaneGuess = new GridPane();
		GridPane gridpaneKeyBoardFirst = new GridPane();
		GridPane gridpaneKeyBoardSecond = new GridPane();
		GridPane gridpaneKeyBoardThrid = new GridPane();
		VBox vboxTitle = new VBox();
		VBox vboxGuess = new VBox();
		VBox vboxKeyBoardFirst = new VBox();
		VBox vboxKeyBoardSecond = new VBox();
		VBox vboxKeyBoardThrid = new VBox();
		Text title =  new Text();
		
		//TITLE
		title.setText("WORDLE");
		title.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 40));
		title.setFill(Color.WHITE);
		vboxTitle.getChildren().add(title);
		vboxTitle.setLayoutX(310);
		pane.getChildren().add(vboxTitle);
		pane.setStyle("-fx-background-color: #121213;");
		
		//GUESS
		gridpaneGuess = createGuessGrid(gridpaneGuess);
		gridpaneGuess.setHgap(GRID_GAP);
		gridpaneGuess.setVgap(GRID_GAP);
		vboxGuess.getChildren().add(gridpaneGuess);
		vboxGuess.setLayoutX((SCENE_SIZE - ((LETTER_SQUARE_SIZE * 5) + (GRID_GAP * 4))) / 2);
		vboxGuess.setLayoutY(50);
		pane.getChildren().add(vboxGuess);
		
		//KEYBOARD
		gridpaneKeyBoardFirst = createKeyBoardFirst(gridpaneKeyBoardFirst);
		gridpaneKeyBoardFirst.setHgap(GRID_GAP);
		vboxKeyBoardFirst.getChildren().add(gridpaneKeyBoardFirst);
		vboxKeyBoardFirst.setLayoutX(((SCENE_SIZE - ((LETTER_SQUARE_SIZE - 20) * 10) - (5 * 9)) / 2)); // squreWidth: 45, gap:5
		vboxKeyBoardFirst.setLayoutY(500);
		
		gridpaneKeyBoardSecond = createKeyBoardSecond(gridpaneKeyBoardSecond);
		gridpaneKeyBoardSecond.setHgap(GRID_GAP);
		vboxKeyBoardSecond.getChildren().add(gridpaneKeyBoardSecond);
		vboxKeyBoardSecond.setLayoutX(((SCENE_SIZE - ((LETTER_SQUARE_SIZE - 20) * 9) - (5 * 8)) / 2)); // squreWidth: 45, gap:5
		vboxKeyBoardSecond.setLayoutY(560);
		
		gridpaneKeyBoardThrid = createKeyBoardThrid(gridpaneKeyBoardThrid);
		gridpaneKeyBoardThrid.setHgap(GRID_GAP);
		vboxKeyBoardThrid.getChildren().add(gridpaneKeyBoardThrid);
		vboxKeyBoardThrid.setLayoutX(157.2); // squreWidth: 45, gap:5
		vboxKeyBoardThrid.setLayoutY(620);
		
		pane.getChildren().add(vboxKeyBoardFirst);
		pane.getChildren().add(vboxKeyBoardSecond);
		pane.getChildren().add(vboxKeyBoardThrid);
		
		Scene scene = new Scene(pane, SCENE_SIZE, SCENE_SIZE);
		
		scene.setOnKeyReleased(eventHandler);
		
		
		stage.setTitle("WORDLE");
		stage.setScene(scene);
		stage.show();
	}
	
	
	
	
	/**
	 * Creates empty guess grid. 
	 * 
	 * It shows an initial empthy 6 * 5 grid.
	 * 
	 * @param gridpaneGuess
	 * @return GridPane
	 */
	private GridPane createGuessGrid(GridPane gridpaneGuess) {
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 5; j++) {
				
				label[i][j] = new Label();
				label[i][j].setMinHeight(LETTER_SQUARE_SIZE);
				label[i][j].setMinWidth(LETTER_SQUARE_SIZE);
				label[i][j].setStyle("-fx-border-color: grey; -fx-border-width:2;");
				label[i][j].setAlignment(Pos.CENTER);
				
				gridpaneGuess.add(label[i][j], j, i);
			}
		}
		return gridpaneGuess;
	}

	
	/**
	 * Creates first row keyboard layout.
	 * 
	 * It will display Q - Z.
	 * 
	 * @param gridpaneKeyBoardFirst
	 * @return GridPane
	 */
	private GridPane createKeyBoardFirst(GridPane gridpaneKeyBoardFirst) {
		for (int i = 0; i < firstRowString.length; i++) {
			
			firstRow[i] = new Label();
			firstRow[i].setText(firstRowString[i]);
			firstRow[i].setMinHeight(LETTER_SQUARE_SIZE - 10);
			firstRow[i].setMinWidth(LETTER_SQUARE_SIZE - 20);
			firstRow[i].setTextFill(Color.WHITE);
			firstRow[i].setStyle("-fx-background-color: #818384; -fx-background-radius: 10 10 10 10;");
			firstRow[i].setFont(Font.font("Clear Sans", FontWeight.BOLD, 20));
			firstRow[i].setAlignment(Pos.CENTER);
			
			gridpaneKeyBoardFirst.add(firstRow[i], i, 0);
		}
		return gridpaneKeyBoardFirst;
	}
	
	/**
	 * Creates second row keyboard layout.
	 * 
	 * It will display A - L.
	 * 
	 * @param gridpaneKeyBoardSecond
	 * @return GridPane
	 */
	private GridPane createKeyBoardSecond(GridPane gridpaneKeyBoardSecond) {
		for (int i = 0; i < secondRowString.length; i++) {
			secondRow[i] = new Label();
			secondRow[i].setFont(Font.font("Clear Sans", FontWeight.BOLD, 20));
			secondRow[i].setText(secondRowString[i]);
			secondRow[i].setMinHeight(LETTER_SQUARE_SIZE - 10);
			secondRow[i].setMinWidth(LETTER_SQUARE_SIZE - 20);
			secondRow[i].setTextFill(Color.WHITE);
			secondRow[i].setStyle("-fx-background-color: #818384; -fx-background-radius: 10 10 10 10;");
			secondRow[i].setAlignment(Pos.CENTER);
			
			gridpaneKeyBoardSecond.add(secondRow[i], i, 0);
		}
		return gridpaneKeyBoardSecond;
	}
	
	/**
	 * Creates thrid row keyboard layout.
	 * 
	 * It will display Z - M including the ENTER and Backspace symbol.
	 * 
	 * @param gridpaneKeyBoardThird
	 * @return GridPane
	 */
	private GridPane createKeyBoardThrid(GridPane gridpaneKeyBoardThrid) {
		for (int i = 0; i < thridRowString.length; i++) {
			thridRow[i] = new Label();
			thridRow[0].setMinWidth(80); //ENTER
			thridRow[i].setFont(Font.font("Clear Sans", FontWeight.BOLD, 20));
			thridRow[i].setText(thridRowString[i]);
			thridRow[i].setMinHeight(LETTER_SQUARE_SIZE - 10);
			thridRow[i].setMinWidth(LETTER_SQUARE_SIZE - 20);
			thridRow[i].setTextFill(Color.WHITE);
			thridRow[i].setStyle("-fx-background-color: #818384; -fx-background-radius: 10 10 10 10;");
			thridRow[i].setAlignment(Pos.CENTER);
			
			gridpaneKeyBoardThrid.add(thridRow[i], i, 0);
		}
		return gridpaneKeyBoardThrid;
	}
	
	
	/**
	 * popup message for Game Over
	 * 
	 * Depends on the number of tries, it will pop up a differnt message.
	 * 
	 * @param count, int
	 * @param answer, String
	 */
	private void popupGameOver(int count, String answer) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Game Over");
		
		if (count == 0) {
			alert.setContentText("Genius!");
		} else if (count == 1) {
			alert.setContentText("Magnificent!");
		} else if (count == 2) {
			alert.setContentText("Impressive!");
		} else if (count == 3) {
			alert.setContentText("Splendid!");
		} else if (count == 4) {
			alert.setContentText("Great!");
		} else if(count == 5) {
			alert.setContentText("Phew.");
		} 
		alert.showAndWait();
	}
	
	
	/**
	 * popup message for Invalid Input
	 * 
	 * notifies users that their guesses were invalid.
	 */
	private void popupInvalidInput() {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Invalid Input");
		alert.setContentText("Valid input should be 5 letter long valid words containing only alphabets.");
		alert.showAndWait();
	}
	
	
	
	/**
	 * Animation, rotating each tile of guess grid.
	 * 
	 * Between each tile, it puases for 0, 0.4, 0.8, 1.2, 1.6 sec
	 * so that it looks revealing each tile.
	 * 
	 * @param tile, Label 
	 * @param i, int
	 * @return SequentialTransition
	 */
	private SequentialTransition rotate(Label tile, int i) {
        RotateTransition rt = new RotateTransition(Duration.millis(400), tile);
        rt.setAxis(Rotate.X_AXIS);
        rt.setByAngle(90);
        rt.setCycleCount(2);
        PauseTransition pt = new PauseTransition(Duration.millis(400 * i));
        SequentialTransition st = new SequentialTransition (tile, pt, rt);
        rt.setAutoReverse(true);
  
		return st;
	}
	
	
	
	/**
	 * Changes the color of the keyboard.
	 * 
	 * Correct: green / Incorrect: dark color / WrongIndex: yellow 
	 * 
	 */
	private void updateKeyBoard() {
		//KEYBOARD
		//65=A, 90=Z
		for (int i = 0; i < 26; i++) {
			int index = i + 65;
			char letter = (char) index;
			String alphabet = String.valueOf(letter).toUpperCase();
			
			if (guessedCharacter[i] == null && !unguessed.contains(alphabet)) {
				unguessed.add(alphabet);
			} else if (guessedCharacter[i] == INDEX_RESULT.CORRECT && !correct.contains(alphabet)) {
				correct.add(alphabet);
			} else if (guessedCharacter[i] == INDEX_RESULT.INCORRECT && !incorrect.contains(alphabet)) {
				incorrect.add(alphabet);
			} else if (guessedCharacter[i] == INDEX_RESULT.CORRECT_WRONG_INDEX && !wrongIndex.contains(alphabet)) {
				wrongIndex.add(alphabet);
			}
			
			
			for (int x = 0; x < firstRowString.length; x++) {
				for (int x1 = 0; x1 < incorrect.size(); x1++) {
					if (firstRowString[x].equals(incorrect.get(x1))) {
						firstRow[x].setStyle("-fx-background-color: #3a3a3c; -fx-background-radius: 10 10 10 10;");
					}
				}
				for (int x2 = 0; x2 < wrongIndex.size(); x2++) {
					if (firstRowString[x].equals(wrongIndex.get(x2))) {
						firstRow[x].setStyle("-fx-background-color: #b59f3b; -fx-background-radius: 10 10 10 10;");
					}
				}
				for (int x3 = 0; x3 < correct.size(); x3++) {
					if (firstRowString[x].equals(correct.get(x3))) {
						firstRow[x].setStyle("-fx-background-color: #538d4e; -fx-background-radius: 10 10 10 10;");
					}
				}
				
			}
			
			for (int y = 0; y < secondRowString.length; y++) {
				for (int y1 = 0; y1 < incorrect.size(); y1++) {
					if (secondRowString[y].equals(incorrect.get(y1))) {
						secondRow[y].setStyle("-fx-background-color: #3a3a3c; -fx-background-radius: 10 10 10 10;");
					}
				}
				for (int y2 = 0; y2 < wrongIndex.size(); y2++) {
					if (secondRowString[y].equals(wrongIndex.get(y2))) {
						secondRow[y].setStyle("-fx-background-color: #b59f3b; -fx-background-radius: 10 10 10 10;");
					}
				}
				for (int y3 = 0; y3 < correct.size(); y3++) {
					if (secondRowString[y].equals(correct.get(y3))) {
						secondRow[y].setStyle("-fx-background-color: #538d4e; -fx-background-radius: 10 10 10 10;");
					}
				}
				
			}
			
			for (int z = 0; z < thridRowString.length; z++) {
				for (int z1 = 0; z1 < incorrect.size(); z1++) {
					if (thridRowString[z].equals(incorrect.get(z1))) {
						thridRow[z].setStyle("-fx-background-color: #3a3a3c; -fx-background-radius: 10 10 10 10;");
					}
				}
				for (int z2 = 0; z2 < wrongIndex.size(); z2++) {
					if (thridRowString[z].equals(wrongIndex.get(z2))) {
						thridRow[z].setStyle("-fx-background-color: #b59f3b; -fx-background-radius: 10 10 10 10;");
					}
				}
				for (int z3 = 0; z3 < correct.size(); z3++) {
					if (thridRowString[z].equals(correct.get(z3))) {
						thridRow[z].setStyle("-fx-background-color: #538d4e; -fx-background-radius: 10 10 10 10;");
					}
				}
			}
		}
	}
	
	
	/**
	 * Update method, from Observer Observerable patter.
	 *
	 * This method should change the the label text and background colors
	 * to indicate which letters of the guess are in the word. 
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof WordleModel) {
			WordleModel model = (WordleModel) o;
			guessedCharacter = model.getGuessedCharacters();
			progress = model.getProgress();
	
			// Guess grid
			for (int i = 0; i < 5; i ++) {
				
				SequentialTransition st = rotate(label[count][i], i);
				
				final int x = i;
				final Label eachTile = label[count][x];
				final INDEX_RESULT indexResult = progress[count].getIndices()[x];
				
				st.setOnFinished(ev -> {
					if (indexResult == INDEX_RESULT.INCORRECT) {
						eachTile.setStyle("-fx-background-color: #3a3a3c;");
					} else if (indexResult == INDEX_RESULT.CORRECT_WRONG_INDEX){
						eachTile.setStyle("-fx-background-color: #b59f3b;");
					} else if (indexResult == INDEX_RESULT.CORRECT) {
						eachTile.setStyle("-fx-background-color: #538d4e;");
					}
					
					PauseTransition pt = new PauseTransition(Duration.millis(1600));
					pt.setOnFinished(evt -> {
						updateKeyBoard();
					});
					pt.play();
				});
				
				st.play();			
			}	
		}
	}

}
