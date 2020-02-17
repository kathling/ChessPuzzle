/**
 * Project Name: POC_Chess_Puzzle
 * Class Name: CreatePuzzleUI
 * Description: CreatePuzzleUI allows the caller to mutate and access the UI
 * for the create puzzle mode.
 */

package view;

import controller.Manager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import model.Position;

public class CreatePuzzleUI extends ChessBoardUI{

    // -----------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------
   
    /**
     * Description: allow the caller to create a CreatePuzzleUI object
     * Pre condition: newManager, newTheme, mainMenu, and mainIcon can not be 
     * null
     * Post condition: CreatePuzzleUI object is created, elements are added to 
     * mainMenu and mainIcon 
     * @param newManager specifies the Manager object
     * @param newTheme specifies the Theme object (classic)
     */       
    public CreatePuzzleUI(Manager newManager, Theme newTheme) {
        super(newManager, newTheme);
        mainMenu = new String[] {"undo", "save puzzle"};
        mainIcon = new ImageIcon[] {myTheme.undoIcon, myTheme.saveIcon};
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
        for (int y = 0; y < 8; y++) { // install a handler to each tile
            for (int x = 0; x < 8; x++) {
                Position pos = new Position(x, y);
                MoveHandler handler = new MoveHandler(pos, myTheme.getPawnIcon(), false);
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
        for (int i = 0; i < level.length; i++) {
            level[i].setVisible(false);
        }
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
            //release state 
            setTiles(gameState.getBishopPosition(), null);
        }
        if (pawnPos != null) {
            setTiles(pawnPos, myTheme.getBishopIcon());
        }
        undoMove();        
    }    

}
