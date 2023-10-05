package controller;

import java.io.FileNotFoundException;

import model.WordleModel;

/**
 * @author Sangeun Park
 * 
 * WordleController
 *
 * +isGameOver(): boolean
 * +getAnswer(): String
 * +validate(String guess): boolean 
 * +checkValidWord(String guess): boolean 
 * +makeGuess(String guess)
 * 
 */
public class WordleController {
	
	private WordleModel model;
	private int count;
	
	public WordleController (WordleModel model) {
		this.model = model;
		this.count = 0;
	} 
	
	/**
	 * checks if game is over or not.
	 * 
	 * @return boolean
	 */
	public boolean isGameOver() {
		return count == 6;
	}
	
	
	/**
	 * get answer
	 * 
	 * @return String
	 */
	public String getAnswer() {
		return model.getAnswer();
	}
	
	
	/**
	 * validates if user input is only alphabetic.
	 * 
	 * @param guess
	 * @return boolean
	 */
	public boolean validate(String guess) {
		return model.validate(guess);
	}
	
	
	/**
	 * validates if user input is on the dictionary file.
	 * 
	 * @param guess
	 * @return boolean
	 * @throws FileNotFoundException
	 */
	public boolean checkValidWord(String guess) throws FileNotFoundException {
		return model.checkValidWord(guess);
	}
	
	
	/**
	 * makeGuess
	 * 
	 * call model.makeGuess
	 * 
	 * @param guess
	 * @throws FileNotFoundException
	 */
	public void makeGuess(String guess) throws FileNotFoundException {
		if (validate(guess) && checkValidWord(guess)) {
			model.makeGuess(count, guess);
			count++;
		}
		
	}

}
