package view;

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Observable;
import java.util.Observer;

import controller.WordleController;
import javafx.application.Application;
import model.WordleModel;


/**
 * @author Sangeun Park
 * 
 * Wordle
 * 
 * takes argument '-text' or '-gui'.
 * If argument is none of these, it will automatically call the gui version.
 *  
 */
public class Wordle{
	
	public static void main(String[] args) throws FileNotFoundException {
		
		if (args[0].equals("-text")) {
			WordleTextView wordleText = new WordleTextView();
			wordleText.main(args);			
		} else if (args[0].equals("-gui")) {
			Application.launch(WordleGUIView.class, args);
		} else {
			Application.launch(WordleGUIView.class, args);
		}
		
		
	}
    
}
