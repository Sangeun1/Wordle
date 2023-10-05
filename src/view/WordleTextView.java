package view;


import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import controller.WordleController;
import model.WordleModel;
import utilities.Guess;
import utilities.INDEX_RESULT;

/**
 * @author Sangeun Park
 * 
 * Wordle text verison
 * 
 * 
 * valid guesses should be 5 letter long and only alphabetic.
 * Users can play as many times as they want.
 * It will show unguessed, correct, incorrect, wrong indexed alphabets for users to make guesses easier.
 * Uppercase character indicates your guess in correct in a correct index.
 * Lowercase character indicates that your guessed is correct but a wrong index.
 * Blank line shows your guess is incorrect.
 * 
 * It uses Observer, Observable pattern.
 *
 */
public class WordleTextView implements Observer{
	
	private INDEX_RESULT[] guessedCharacter;
	private Guess[] progress;

	public static void main(String[] args) throws FileNotFoundException {
    	
    	boolean playing = true;
    	
		System.out.print("Enter a guess: ");
		
		do {
			playing = startGame(playing); 
			if (playing) {
				System.out.print("Enter a guess: ");
			} else {
				System.out.println("GAME OVER");
			}
		} while (playing);
    	
    }
    
    
    
    /**
     * start game, game logic
     * 
     * call WordleModel, WordleController
     * 
     * @param playing, boolean 
     * @return boolean
     * @throws FileNotFoundException
     */
    private static boolean startGame(boolean playing) throws FileNotFoundException {
    	Scanner input = new Scanner(System.in);
    	String userGuess = input.next().toLowerCase();
		
    	WordleTextView view = new WordleTextView();
		WordleModel model = new WordleModel();
		WordleController controller = new WordleController(model);
		
		model.addObserver(view);
		
		while(!controller.isGameOver()) {
			if (controller.validate(userGuess) || controller.checkValidWord(userGuess)) {
				System.out.println("That's an invalid word");
			}
			controller.makeGuess(userGuess);
			
			if (userGuess.equals(controller.getAnswer())) {
				playing = false;
				break;
			}
			
			if (controller.getAnswer() != userGuess && !controller.isGameOver()) {
				System.out.print("Enter a guess: ");
				userGuess = input.next().toLowerCase();
			} else {
				break;
			}
		}
		
		System.out.println("Good game! The word was " + controller.getAnswer().toUpperCase() + ".");
		System.out.println("Would you like to play again? yes/no");

		String response = input.next().toLowerCase();
		
		if (response.equals("yes")) {
			playing = true;
		} else if (response.equals("no")) {
			playing = false;
		} 
		
		return playing;
		
    }


	/**
	 * update method, Observer.
	 * 
	 * will print out guess grid, unguessed, correct, incorrect, wrong index alphabets.
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof WordleModel) {
			WordleModel model = (WordleModel) o;
			guessedCharacter = model.getGuessedCharacters();
			progress = model.getProgress();
			
			for (int i = 0; i < 6; i++) {
				if (progress[i] != null) {
					for (int j = 0; j < 5; j++) {
						if (progress[i].getIndices()[j] == INDEX_RESULT.CORRECT) {
							System.out.print(progress[i].getGuess().toUpperCase().charAt(j) + " ");
						} else if (progress[i].getIndices()[j] == INDEX_RESULT.CORRECT_WRONG_INDEX) {
							System.out.print(progress[i].getGuess().charAt(j) + " ");
						} else if (progress[i].getIndices()[j] == INDEX_RESULT.INCORRECT) {
							System.out.print("_" + " ");
						}						
					}
					System.out.println();
				} else {
					System.out.println("_ _ _ _ _");
				}
			}
			System.out.println();
			
			ArrayList<Character> unguessed = new ArrayList<>();
			ArrayList<Character> correct = new ArrayList<>();
			ArrayList<Character> incorrect = new ArrayList<>();
			ArrayList<Character> wrongIndex = new ArrayList<>();
			
			for (int i = 0; i < 26; i++) {
				int ascii = i + 97;
				char c = (char) ascii ;
				char alphabet = Character.toUpperCase(c);
				
				if (guessedCharacter[i] == null) {
					unguessed.add(alphabet);
				} else if (guessedCharacter[i] == INDEX_RESULT.CORRECT) {
					correct.add(alphabet);
				} else if (guessedCharacter[i] == INDEX_RESULT.CORRECT_WRONG_INDEX) {
					wrongIndex.add(alphabet);
				} else if (guessedCharacter[i] == INDEX_RESULT.INCORRECT) {
					incorrect.add(alphabet);
				}
			}
			System.out.println("Unguessed: " + unguessed);
			System.out.println("Correct: " + correct);
			System.out.println("Correct Wrong Index: " + wrongIndex);
			System.out.println("Incorrect: " + incorrect);
			
			System.out.println();
		}		
	}
    
}
