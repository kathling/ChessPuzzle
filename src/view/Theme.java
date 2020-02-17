/**
 * Project Name: POC_Chess_Puzzle
 * Class Name: Theme
 * Description: Theme allows the caller to access the graphics and audio.
 */

package view;

import java.awt.Color;
import javax.swing.ImageIcon;

public abstract class Theme {
    // -----------------------------------------------------------------------
    // Class Instance Variables (Protected)
    // -----------------------------------------------------------------------
    
    // Images
    protected ImageIcon pawnIcon = null; 
    protected ImageIcon bishopIcon = null;
    
    // Buttons
    protected ImageIcon undoIcon = null;
    protected ImageIcon saveIcon = null;
    
    // Board Colour 
    protected Color boardColorBlack = null;
    protected Color boardColorWhite = null;

    // sound file
    protected String soundCapture;
    protected String soundInvalidMove;
    protected String soundUndo;
    protected String soundNextLevel;
    protected String soundGameOver;
    
    // -----------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------
    
    /**
     * Description: allow the caller to create a Theme object
     * Pre condition: none
     * Post condition: Theme object is created
     */     
    public Theme() {
        
    }

    // -----------------------------------------------------------------------
    // Accessor (aka Getter)
    // -----------------------------------------------------------------------
    
    public ImageIcon getPawnIcon() {
        return pawnIcon;
    }
    
    public ImageIcon getBishopIcon() {
        return bishopIcon;
    }
    
    public Color getBoardColorBlack() {
        return boardColorBlack;
    }
    
    public Color getBoardColorWhite() {
        return boardColorWhite;
   }
    
    // -----------------------------------------------------------------------
    // Abstract Methods
    // -----------------------------------------------------------------------
    
    abstract public void loadAsset();
    
}
