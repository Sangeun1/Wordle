package model;

import java.io.File;
import java.util.Observable;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

import utilities.Guess;
import utilities.INDEX_RESULT;

/**
 * @author Sangeun Park
 * 
 * WordleModel, Observable
 * 
 * 
 * 
 * -chooseAnswer(): String 
 * +validate(String guess): boolean 
 * +checkValidWord(String guess): boolean 
 * +makeGuess(int guessNumber, String guess)
 * +getAnswer(): String 
 * +getGuessedCharacters(): INDEX_RESULT[] 
 * +getProgress(): Guess[] 
 */
public class WordleModel extends Observable{
	
	private static final String FILENAME = "../Dictionary.txt";
	private String answer;
	private INDEX_RESULT[] guessedCharacters;
	private Guess[] progress;

	
	public WordleModel() throws FileNotFoundException { 
		this.answer = chooseAnswer();
		this.guessedCharacters = new INDEX_RESULT[26];
		this.progress = new Guess[6];
	}
	

	/**
	 * Chooses the answer randomly from the answer file.
	 * 
	 * @return String, answer
	 * @throws FileNotFoundException
	 */
	private String chooseAnswer() throws FileNotFoundException {
		String chosen = null;
		Random rand = new Random();
		int n = 0;		
		File file = new File(FILENAME);
		Scanner read = new Scanner(file);
		while (read.hasNextLine()) {
			n++;
			String line = read.nextLine();
			if(rand.nextInt(n) == 0) {
				chosen = line.toLowerCase();
			}
			
		}
		read.close();
		return chosen;
	}  
	
	
	/**
	 * Validates if user input is alphabetic.
	 * @param guess
	 * @return boolean
	 */
	public boolean validate(String guess) {
		for (int i = 0; i < 5; i++) {
			int ascii = guess.charAt(i);
			if (!(ascii >= 97 && ascii <= 122)) {
				return false;
			}
		}
		return true;
	}
	
	
	/**
	 * Validates if user input is a valid word listed on the dictionary file.
	 * 
	 * @param guess
	 * @return boolean
	 * @throws FileNotFoundException
	 */
	public boolean checkValidWord(String guess) throws FileNotFoundException {
		File file = new File("Words.txt");
		Scanner read =  new Scanner(file);
		while(read.hasNextLine()) {
			String eachWord = read.nextLine();
			if (eachWord.equals(guess)) {
				return true;
			}
		}
		read.close();
		return false;
	}
	
	
	/**
	 * It will store the result of guesses in the form like, [correct, incorrect, incorrect, wrongIndex, incorrect].
	 * 
	 * This method calls setChanged() and notifyObservers().
	 * 
	 * @param guessNumber
	 * @param guess
	 * @throws FileNotFoundException
	 */
	public void makeGuess(int guessNumber, String guess) throws FileNotFoundException {
		if (validate(guess) && checkValidWord(guess)) {
			INDEX_RESULT[] temp = new INDEX_RESULT[5];
			for (int i = 0; i < 5; i++) {
				
				int ascii = (int) guess.charAt(i);
				int index = ascii - 97;
				
				if (answer.contains(guess.subSequence(i, i+1))) {
					guessedCharacters[index] = INDEX_RESULT.CORRECT_WRONG_INDEX;
					temp[i] = INDEX_RESULT.CORRECT_WRONG_INDEX;
					
					if (answer.charAt(i) == guess.charAt(i)) {
						guessedCharacters[index] = INDEX_RESULT.CORRECT;
						temp[i] = INDEX_RESULT.CORRECT;
					}
				} else {
					guessedCharacters[index] = INDEX_RESULT.INCORRECT;
					temp[i] = INDEX_RESULT.INCORRECT;
				}
			}
			
			progress[guessNumber] = new Guess(guess, temp, guess == getAnswer());	
			
			setChanged();
			notifyObservers();
		}
		
	}
	

	/**
	 * get answer. 
	 * 
	 * @return String
	 */
	public String getAnswer() {
		return answer;
	}
	

	/**
	 * get guessedCharacters
	 * 
	 * will represent alphabets.
	 * 
	 * @return INDEX_RESULT[]
	 */
	public INDEX_RESULT[] getGuessedCharacters() {
		return guessedCharacters;
	}
	
	
	/**
	 * get progress 
	 * 
	 * Guess[] is an array of objects.
	 * [
	 * 	[correct, correct, correct, correct, correct],
	 * 	[ ],
	 * 	[ ],
	 * 	[ ],
	 * 	[ ],
	 * 	[ ],
	 * ]
	 * 
	 * @return Guess[]
	 */
	public Guess[] getProgress() {
		return progress;
	}

}
