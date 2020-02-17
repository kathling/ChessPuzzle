/**
 * Project Name: POC_Chess_Puzzle
 * Class Name: PlayPuzzleUI
 * Description: PlayPuzzleUI allows the caller to mutate and access the UI
 * for the play puzzle mode.
 */

package view;

import controller.Manager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import model.Position;

public class PlayPuzzleUI extends ChessBoardUI{
 
    // -----------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------

    /**
     * Description: allow the caller to create a PlayPuzzleUI object
     * Pre condition: newManager, newTheme, mainMenu, and mainIcon can not be 
     * null
     * Post condition: PlayPuzzleUI object is created, elements are added to 
     * mainMenu and mainIcon 
     * @param newManager specifies the Manager object
     * @param newTheme specifies the Theme object 
     */     
    public PlayPuzzleUI(Manager newManager, Theme newTheme) {
        super(newManager, newTheme); 
        mainMenu = new String[] {"undo"};
        mainIcon = new ImageIcon[] {myTheme.undoIcon}; 
    }

    // -----------------------------------------------------------------------
    // Public Methods (Override)
    // -----------------------------------------------------------------------
    
    /**
     * Description: allow the caller to install a handler
     * Pre condition: object must exist
     * Post condition: create a handler and add action listener 
     */        
    @Override
    public void installHandler() {
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Position pos = new Position(x, y);
                //CaptureHandler handler = new CaptureHandler(pos);
                ChessBoardUI.MoveHandler handler = new ChessBoardUI.MoveHandler(pos, null, true);
                tiles[y][x].addActionListener(handler);
            }
        }
    }

    /**
     * Description: allow the caller to initialize the level 
     * Pre condition: object must exist
     * Post condition: set level's visibility to FALSE
     */    
    @Override
    public void initializeLevel() {
        level[0].setEnabled(true);
    }   
    
    /**
     * Description: allow the caller to undo a move
     * Pre condition: object must exist
     * Post condition: set the tiles back to the previous position
     */    
    @Override
    public void undo() {
        Position bishopPos = gameState.getBishopPosition();
        Position pawnPos = gameState.getLastPawnPosition();

        if (gameState.getBishopPosition() != null) {

            // capture state
            if (gameState.isPathEmpty())
                setTiles(gameState.getBishopPosition(), myTheme.getBishopIcon());
            else
                setTiles(gameState.getBishopPosition(), myTheme.getPawnIcon());
        }
        if (pawnPos != null) {
            setTiles(pawnPos, myTheme.getBishopIcon());
        }        
        undoMove();
    }    
    
}
