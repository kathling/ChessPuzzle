/**
 * Project Name: POC_Chess_Puzzle
 * Class Name: ClassicTheme
 * Description: ClassicTheme allows the caller to access the graphics and audio.
 */
package view;

import java.awt.Color;
import javax.swing.ImageIcon;

public class ClassicTheme extends Theme {

    // -----------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------
    
    ClassicTheme() {
        super();
    }

    // -----------------------------------------------------------------------
    // Public Methods
    // -----------------------------------------------------------------------
    
    /**
     * Description: allow the caller to load assets
     * Pre condition: Theme/object must exist
     * Post condition: set the ImageIcon for pawnIcon, bishopIcon, undoIcon, 
     * saveIcon, set the board colour, and set the sound effects.
     */    
    public void loadAsset() {
        pawnIcon = new ImageIcon("./asset/Chess_pdt60.png");
        bishopIcon = new ImageIcon("./asset/Chess_blt60.png");
        undoIcon = new ImageIcon("./asset/undo.png");
        saveIcon = new ImageIcon("./asset/save.png");
        
        boardColorBlack = new Color(222, 189, 148);
        boardColorWhite = new Color(140, 75, 43);
        
        soundCapture = "./asset/Capture.wav";
        soundInvalidMove = "./asset/InvalidMove.wav";
        soundUndo = "./asset/Undo.wav";
        soundNextLevel = "./asset/NextLevel.wav";
        soundGameOver = "./asset/GameOver.wav";
    }    
    
}
