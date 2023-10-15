# Wordle

---

Wordle is a word puzzle game that gained popularity for its simplicity and addictiveness. The game consists of guessing a hidden five-letter word within six attempts. Each time you guess a word, you receive feedback on which letters are correct and in the correct position (indicated in green), which letters are correct but in the wrong position (indicated in yellow), and which letters are not in the word at all (indicated in gray).


## Installation

---

Before playing Wordle, you need to install Eclipse and set up the project. Here's how:

1. **Install Eclipse**: If you don't already have Eclipse installed, download it from the [official Eclipse website](https://www.eclipse.org/).

2. **Clone the Repository**: Clone the Wordle repository to your local machine using Git or download it as a ZIP archive.

3. **Open Eclipse**: Launch Eclipse and set up a workspace.

4. **Import the Project**: Go to "File" > "Import," select "General" > "Existing Projects into Workspace," and choose the Wordle project directory.


## Running the Game

---

To run the Wordle game in Eclipse, follow these steps:

1. **Run Configuration**: Open the "Run Configurations" in Eclipse.

2. **Java Application**: Create a new "Java Application" configuration.

3. **Main Class**: Set the main class as the entry point of the Wordle game. Project: src, Program arguments: -gui or -text

4. **Run**: Click "Run" to start the game.

5. **Follow the Game Instructions**: The Wordle game should launch, and you can follow the in-game instructions to play.

## How to Play Wordle

Wordle is a word puzzle game where your goal is to guess a hidden five-letter word within six attempts. The game provides feedback after each guess to help you narrow down the possibilities.

1. **Start a New Game**: Open Wordle in Eclipse, following the installation and running instructions provided in the README. The game will display an empty grid for your guesses.

2. **Make Your First Guess**: Begin by making your first guess for the hidden word. Enter a five-letter word into the grid. Your first guess might be random, but don't worry; the feedback will guide you.

3. **Receive Feedback**: After each guess, you'll receive feedback in the form of colored blocks:

    - Green Blocks: These blocks represent letters that are correct and in the correct position in the word.
    - Yellow Blocks: These blocks represent letters that are correct but in the wrong position.
    - Gray Blocks: These blocks represent letters that are not in the word.

   Use this feedback to refine your guesses. Pay attention to the green and yellow blocks, as they indicate which letters are part of the word.

4. **Refine Your Guesses**: Based on the feedback, modify your next guess. Adjust the position of letters to match the green and yellow blocks and eliminate letters that were marked in gray.

5. **Continue Guessing**: Repeat steps 3 and 4 until you guess the hidden word or use up all six attempts. Remember that you have only six tries, so use them wisely.

6. **Win or Retry**: If you guess the word within six attempts, you win the game! You can then choose to start a new game or challenge yourself with another round.


Now that you know how to play Wordle, launch the game in Eclipse and test your word-solving skills. Good luck!


## Model/View/Controller

---

1.	A main class (named Wordle) that serves as our view, creates the Model and Controller, and deals with user input and output.
2.	A model class (named WordleModel) that stores the representation of the hidden word, which letters have been guessed and responds to guesses about that word.
3.	A controller class (named WordleController) to query the model based on the input from the user that comes through the view.




## Notes

---

- Java16
- JavaFX17
- JRE[16.0.2]
- JDK16
- MVC
- Eclipse[4.20.0]
