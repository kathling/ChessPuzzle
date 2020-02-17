/**
 * Date: January 13, 2019
 * Project Name: POC_Chess_Puzzle 
 * Class: Main Class
 * Description: The program incorporates all of the concepts learned in this 
 * semester to create a fun chess puzzle game! The main menu gives you four main
 * options: 
 * 1. CREATE PUZZLE
 *      - allows the user to create a puzzle that can be played in the "play
 *        puzzle" option.
 * 2. PLAY PUZZLE (kid version for players under 10), 
 *      - allows the user to play puzzles from the puzzle file system. The icons
 *        are catered towards a younger audience (cat & fish icons).
 * 3. PLAY PUZZLE (classic version for players above 10)
 *      - allows the user to play puzzles from the puzzle file system. The icons
 *        will be the classic icons used in chess (bishop & pawn icons).
 * 4. EXIT.
 *      - allows the user to exit the program.
 */

package launcher;

import view.MenuUI;

public class Main {
    public static void main(String[] args) {
        MenuUI ui = new MenuUI(); // create the user interface
        ui.createAndShowGUI();
    }
}
