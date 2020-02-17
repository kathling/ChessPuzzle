/**
 * Project Name: POC_Chess_Puzzle
 * Class Name: CaptureState
 * Description: The ReleaseState allows the caller to mutate and access the 
 * game state.
 */

package model;

import controller.Manager;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ReleaseState extends State {

    // -----------------------------------------------------------------------
    // Class Instance Variables
    // -----------------------------------------------------------------------

    private boolean isBoardSaved = false; // if board has been saved, true
    
    // -----------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------
    
    /**
     * Description: allow the caller to create a ReleaseState object
     * Pre condition: the Manager object must exist
     * Post condition: ReleaseState object is created and myManager is set to 
     * newManager
     * @param newManager specifies a Manager object
     */
    public ReleaseState(Manager newManager) {
        super(newManager);
    }

    // -----------------------------------------------------------------------
    // Public Methods (Override)
    // -----------------------------------------------------------------------

    /**
     * Description: allow the caller to initialize the game
     * Pre condition: object must exist
     * Post condition: load game
     */      
    @Override
    public void initializeGame() {
        
        // call base class
        super.initializeGame();
        
        pawnPosition = new ArrayList<>();
        path = new ArrayList<>();
    }

    /**
     * Description: allow the caller to check if the move is valid
     * Pre condition: object and position must exist
     * Post condition: return a boolean value
     * @param newPos specifies the position to check
     * @return true if position is valid, false otherwise
     */        
    @Override
    public boolean isValidMove(Position newPos) {
        
        // If no bishop position set, move is valid
        if (bishopPosition == null) {
            return true;
        }
            
        // Skip if tile is occupied
        if (occupiedPosition[newPos.getY()][newPos.getX()] != '.')
            return false;
        
        // Skip if new position is not a diagonal move
        if (!newPos.isDiagonal(bishopPosition))
            return false;
        
        return true;
    }
    
    /**
     * Description: allow the caller to move position
     * Pre condition: object and Position must exist
     * Post condition: move the bishop position and increment the number of 
     * pawns
     * @param newPos specifies the position to move
     */     
    @Override
    public void movePosition(Position newPos) {
        if (bishopPosition != null) {
            numberOfPawns++;
        }
        setBishopPosition(newPos);
    }
 
    /**
     * Description: allow the caller to undo a move
     * Pre condition: object and Position must exist, lastIndex can not be less
     * than 0, bishopPosition can not be null.
     * Post condition: undo the previous move (see code for more details)
     */     
    @Override
    public void undo() {  
        // the previous move
        int lastIndex = path.size() - 1;
        
        // no previous move
        if (lastIndex < 0) {
            if (bishopPosition != null) {
               occupiedPosition[bishopPosition.getY()][bishopPosition.getX()] = '.';
            }
            bishopPosition = null;
            return;
        }
        
        Position lastPos;
        lastPos = path.remove(lastIndex);
        addUndos();
        pawnPosition.remove(lastIndex);
        
        // set the bishopPosition and pawns back to the last position
        if (bishopPosition != null) {
            occupiedPosition[bishopPosition.getY()][bishopPosition.getX()] = '.';
            bishopPosition = lastPos;
            occupiedPosition[bishopPosition.getY()][bishopPosition.getX()] = 'Q';
            numberOfPawns--;  
            
            isBoardSaved = false;
        } else {

        }
    }
     
    /**
     * Description: allow the caller to save the game to a file
     * Pre condition: object must exist, board should not be previously saved
     * Post condition: save puzzle to the file system
     */    
    @Override
    public void saveGame() {
        // return immediately (short circuit) when there is no pawns.
        if (numberOfPawns == 0) {
            System.out.println("INFO: Nothing to save");
            return;
        }
        
        if (isBoardSaved) {
            System.out.println("INFO: Board already saved");
            return;            
        }

        // clone the state and save recursively 
        ReleaseState newState = myclone(); 

        newState.saveGame_recursion();
    }
    
    // -----------------------------------------------------------------------
    // Private Methods
    // -----------------------------------------------------------------------
    
    /**
     * Description: allow the caller to set the bishop position
     * Pre condition: object must exist, occupiedPosition and path must be 
     * initialized, bishopPosition can not be null
     * Post condition: replace old bishop position with 'P', new position with 
     * 'Q'
     * @param newPos specifies the bishop's new position
     */        
    private void setBishopPosition(Position newPos) {
        if (bishopPosition != null) {
            occupiedPosition[bishopPosition.getY()][bishopPosition.getX()] = 'P';        
            pawnPosition.add(bishopPosition);
            path.add(bishopPosition);
        }
        bishopPosition = newPos;
        occupiedPosition[newPos.getY()][newPos.getX()] = 'Q';
        
        // moving the bishop will unsave the board
        isBoardSaved = false;
    }  
       
    /**
     * Description: allow the caller to save the game using recursion
     * Pre condition: object and dict must exist, numberOfPawns should be 
     * greater than 0, board should not be saved
     * Post condition: save puzzle at the current state and call recursively 
     * for the reduction by number of pawns
     */    
    private void saveGame_recursion() {
        
        // return immediately (short circuit) when there is no pawns.
        if (numberOfPawns == 0) {
            System.out.println("INFO: Nothing to save");
            return;
        }
        
        if (isBoardSaved) {
            System.out.println("INFO: Board already saved");
            return;            
        }

        if (numberOfPawns <= 3) {
            System.out.println("INFO: Board too simple ... not worth to play.");
            return;
        }

        // use the dict (index file) to get the next file number for the given 
        // pawn
        readIndexFile();
        int nextFileNumber = getNextNumber(numberOfPawns);
        
        // save the puzzle to the file
        saveFile(nextFileNumber);
        
        // update the dict and save the index file
        putNextNumber(numberOfPawns, nextFileNumber);
        writeIndexFile();
        isBoardSaved = true;

        // debug
        //System.out.println("# of Pawns:" + numberOfPawns);

        // undo the move and save the puzzlie recursively.
        // the resusive state is the numberOfPawns.
        undo();
        saveGame_recursion();
        
    }

    /**
     * Description: allow the caller to get the next number 
     * Pre condition: the dict must exist and can not be null
     * Post condition: return the next number
     * @param pawnNumber specifies the key to the dictionary 
     * @return an integer, the next number, return 1 if pawnNumber does not 
     * exist in the dict
     */    
    private int getNextNumber(int pawnNumber){
        if (dict == null) {
            return 0;
        }
        
        // if the key exists in the dict, increment by 1
        // otherwise return 1
        if (dict.containsKey(pawnNumber)) {
            return dict.get(pawnNumber) + 1;
        }
        return 1;
    }

    /**
     * Description: allow the caller to put the next number
     * Pre condition: the dict must exist
     * Post condition: add (key,value) to the dict
     * @param pawnNumber specifies the key
     * @param fileNumber specifies the value
     */
    private void putNextNumber(int pawnNumber, int fileNumber){
        dict.put(pawnNumber, fileNumber);
    }
   
    /**
     * Concept: Writing to files.
     * Description:  allow the caller to save the file
     * Pre condition: object must exist
     * Post condition: save the puzzle to the file system
     * @param nextFileNumber specifies the next file number
     */    
    private void saveFile(int nextFileNumber) {
        FileWriter fw = null;
        
        try {
            String fileSpecifier = "./dataset/puzzle/%02d-%04d.txt";
            String file = String.format(fileSpecifier, numberOfPawns, nextFileNumber);
            
            fw = new FileWriter(file);

            System.out.println(">>> WRITING TO FILE " + file);
        }
        catch (IOException e) {
            System.out.println("An error occured while opening the file.");
        }
        
        PrintWriter pw = new PrintWriter(fw);
        String s1 = "";
        
        s1 = "#--*-- Bishop Position --*--";
        pw.println(s1);        
        
        s1 = bishopPosition.toString();
        pw.println(s1);
        
        s1 = "#--*-- Pawn Position --*--";
        pw.println(s1);              
        
        for (int i = pawnPosition.size() - 1; i >= 0; i--) {
            s1 = pawnPosition.get(i).toString();
            pw.println(s1);
        }
       
        pw.close();        
        
    }
    
    /**
     * Description:  allow the caller to create a clone of a State
     * Pre condition: objects must exist
     * Post condition: create a replicate of the State (simplified version),
     * which is used by saveGame_recursion()
     * @return a ReleaseState
     */    
    private ReleaseState myclone() {
        ReleaseState newClone = new ReleaseState(myManager);

        // the class variables specified below will be mutated by the 
        // saveGame_recursion method
        
        newClone.numberOfPawns = numberOfPawns;
        newClone.bishopPosition = bishopPosition;
        
        newClone.pawnPosition = new ArrayList<>();
        for (int i = 0; i < pawnPosition.size(); i++) { // clone the positions
            Position pos = pawnPosition.get(i);
            newClone.pawnPosition.add(pos);
        }

        newClone.path = new ArrayList<>();
        for (int i = 0; i < path.size(); i++) {
            Position pos = path.get(i);
            newClone.path.add(pos);
        }
        
        return newClone;
    }
    
}
