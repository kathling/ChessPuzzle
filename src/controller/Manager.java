/**
 * Project Name: POC_Chess_Puzzle
 * Class Name: Manager
 * Description: The Manager allows the caller to access and mutate the main UI 
 * and state.
 */

package controller;

import model.State;
import model.CaptureState;
import model.ReleaseState;
import view.ChessBoardUI;
import view.Theme;

public class Manager {
    
    // -----------------------------------------------------------------------
    // Class Instance Variables
    // -----------------------------------------------------------------------
    
    State myState;
    ChessBoardUI myMainUI = null;
    
    // -----------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------

    /**
     * Description: allow the caller to create a Manager object
     * Pre condition: none
     * Post condition: Manager object is created, myState is set to null
     */
    public Manager() {
        myState = null;
    }
 
    /**
     * Description: allow the caller to create a Manager object
     * Pre condition: the State object must exist
     * Post condition: Manager object is created and myState is set to s
     * @param s specifies a State object
     */
    public Manager(State s) {
        myState = s;
    }
    
    // -----------------------------------------------------------------------
    // Public Methods
    // -----------------------------------------------------------------------
   
    /**
     * Description: allow the caller to set the main UI reference
     * Pre condition: the input argument can not be null 
     * Post condition: myMainUI is set to the input argument
     * @param newMainUI specifies the main UI reference
     */
    public void setMainUI(ChessBoardUI newMainUI) {
        if (newMainUI == null) {
            return;
        }
        
        myMainUI = newMainUI;
    }
    
    /**
     * Description: allow the caller to start a new game 
     * Pre condition: Manager and myMainUI object must exist/ can not be null
     * Post condition: create a game state and render the UI
     */    
    public void startNewGame() {
        System.out.println(">>> NEW GAME");

        // create and initialize game state
        myState = new CaptureState(this);
        myState.initializeGame();
        
        // render ui
        if (myMainUI != null) {
            myMainUI.clear();
            myMainUI.renderState(myState);
            myMainUI.installHandler();
            myMainUI.advanceLevel(myState.getCurrentLevel());
        }
        
        // print out game state for debugging
        System.out.println(myState.toString());
    }

    /**
     * Description: allow the caller to advance to the next level
     * Pre condition: myState and myMainUI must exist/ can not be null
     * Post condition: reinitialize game state and render the UI again 
     * @return true if game can advance; otherwise false
     */    
    public boolean advanceNextLevel() {
        System.out.println("INFO: Next level");

        // check to ensure the game state can be advanced 
        boolean gameContinue = myState.advanceLevel();
        System.out.println("gameContinue: " + gameContinue);
        if (!gameContinue) {
            return false;
        }

        // re-initialize game state
        myState.initializeGame();
       
        // render ui again
        if (myMainUI != null) {
            myMainUI.clear();
            myMainUI.renderState(myState);
            myMainUI.installHandler();
            myMainUI.advanceLevel(myState.getCurrentLevel());
        }
        
        // print out game state for debugging
        System.out.println(myState.toString());
        return true;
    }
    
    /**
     * Description: allow the caller to create a puzzle 
     * Pre condition: myState and myMainUI must exist/ can not be null
     * Post condition: create a game state and render the UI
     */    
    public void createPuzzle() {
        System.out.println(">>> CREATE PUZZLE");

        // create and initialize game state
        myState = new ReleaseState(this);
        myState.initializeGame();
        
        // render ui
        if (myMainUI != null) {
            myMainUI.clear();
            myMainUI.renderState(myState);
            myMainUI.installHandler();
            myMainUI.initializeLevel();
        }
        
        // print out game state for debugging
        System.out.println(myState.toString());        
    }

    
    /**
     * Description: allow the user to dispatch a command 
     * Pre condition: command must exist and can not be null
     * Post condition: delegate to an internal method
     * @param command specifies the command to execute
     */    
    public void dispatch(String command) {
        switch (command) {
            case "undo":
                undo();
                break;
            case "save puzzle":
                savePuzzle();
                break;
            default:
                System.out.println("ERROR: Unknown command:" + command);
                break;
        }
    }
    
    /**
     * Description: allow the caller to update status 
     * Pre condition: myMainUI and newStatus can not be null 
     * Post condition: print a new status
     * @param newStatus specifies the new status
     */    
    public void updateStatus(String newStatus) {
        if (myMainUI == null) {
            return;
        }
        
        myMainUI.updateStatus(newStatus);
    }

    // -----------------------------------------------------------------------
    // Private Methods
    // -----------------------------------------------------------------------    
    
    /**
     * Description: allow the caller to undo a move
     * Pre condition: myMainUI and myState can not be null
     * Post condition: the last move will be removed from the main ui and game 
     * state
     */    
    private void undo() {
        System.out.println(">>> UNDO");
        
        if (myState == null) {
            return;
        }
        
        if (myMainUI == null) {
            return;
        }
        
        myMainUI.undo();
        myState.undo();
        
        // print out game state for debugging
        System.out.println(myState.toString());
    }    
    
    /**
     * Description: allow the caller to save puzzle
     * Pre condition: myState can not be null
     * Post condition: puzzle will be saved to the file system
     */    
    private void savePuzzle() {
        System.out.println(">>> SAVE PUZZLE");
        
        if (myState == null) {
            return;
        }
            
        myState.saveGame();
    }
        
}
