/**
 * Project Name: POC_Chess_Puzzle
 * Class Name: KidTheme
 * Description: KidTheme allows the caller to access the graphics and audio.
 */

package view;

import java.awt.Color;
import javax.swing.ImageIcon;

public class KidTheme extends Theme {

    // -----------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------
    
    /**
     * Description: allow the caller to create a KidTheme object
     * Pre condition: none
     * Post condition: KidTheme object is created
     */         
    KidTheme() {
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
        pawnIcon = new ImageIcon("./asset/fish.png");
        bishopIcon = new ImageIcon("./asset/cat.png");
        undoIcon = new ImageIcon("./asset/undo.png");
        saveIcon = new ImageIcon("./asset/save.png"); 
        
        boardColorBlack = new Color(255, 255, 131);
        boardColorWhite = new Color(255, 224, 131);   
        
        soundCapture = "./asset/Capture.wav";
        soundInvalidMove = "./asset/InvalidMove.wav";
        soundUndo = "./asset/Undo.wav";
        soundNextLevel = "./asset/NextLevel.wav";
        soundGameOver = "./asset/GameOver.wav"; 
    }    
    
}
