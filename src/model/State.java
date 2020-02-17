/**
 * Project Name: POC_Chess_Puzzle
 * Class Name: State
 * Description: The State allows the caller to mutate and access the game state.
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

public class State {

    // -----------------------------------------------------------------------
    // Class Static Variables
    // -----------------------------------------------------------------------
    
    static final int maxLevel = 8; // the max level, 9 total levels
    static final String statusSpecifier = "Puzzle: %s        Score: %d        # of Clicks / Moves / Undos : %d / %d / %d \n";
    
    // -----------------------------------------------------------------------
    // Class Instance Variables
    // -----------------------------------------------------------------------

    int numberOfPawns = 0; // number of pawns
    ArrayList<Position> pawnPosition; // the pawn positions
    
    Position bishopPosition = null;
    ArrayList<Position> path; // path taken by the bishop
    
    // dictionary, stores the pawn number and its max file number 
    Map<Integer, Integer> dict = null;
                                          
    char[][] occupiedPosition = new char[8][8]; // the occupied positions

    // stats: Clicks/Moves/Undos, counters
    int numberOfClicks = 0;
    int numberOfMoves = 0;
    int numberOfUndos = 0;

    String puzzleFileName = "Unknown";
    
    // level is zero-index
    int currentLevel = 0; // the current level
    
    // object
    Manager myManager;

    // -----------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------
    
    /**
     * Description: allow the caller to create a State object
     * Pre condition: the Manager object must exist
     * Post condition: State object is created and myManager is set to 
     * newManager
     * @param newManager specifies a Manager object
     */
    public State(Manager newManager) {
        myManager = newManager;
        
    }
    
    // -----------------------------------------------------------------------
    // Public Methods (Override)
    // -----------------------------------------------------------------------

    /**
     * Description: allow the caller to initialize the game
     * Pre condition: State/object must exist
     * Post condition: initialize the occupiedPosition, path and 
     * bishopPosition
     */    
    public void initializeGame() {
        
        for (int y = 0; y < 8; y++) { // initialize occupiedPosition
            for (int x = 0; x < 8; x++) {
                occupiedPosition[y][x] = '.';
            }
        }        
        path = new ArrayList<>();
        bishopPosition = null;
    }

    public boolean isValidMove(Position newPos) {
        return false;
    }
 
    public void movePosition(Position newPos) {

    }
    
    public void saveGame() {  
        
    }

    public void undo() {  
        
    }
    
    // -----------------------------------------------------------------------
    // Accessor (aka Getter)
    // -----------------------------------------------------------------------
    
    public Position getBishopPosition() {
        return bishopPosition;
    }
    
    public Position getLastPawnPosition() {
        if (path.size() == 0) {
            return null;
        }
        
        int lastIndex = path.size() - 1;
        return path.get(lastIndex);
    }
    
    public int getNumberOfPawns() {
        return numberOfPawns;
    }
    
    public Position getPawnPositionByIndex(int i) {
        return pawnPosition.get(i);
    }
    
    public int getCurrentLevel() {
        return currentLevel;
    }
    
    // -----------------------------------------------------------------------
    // Public Methods
    // -----------------------------------------------------------------------
    
    /**
     * Description: allow the object to show its representation  
     * Pre condition: object must exist 
     * Post condition: return its own representation as a string
     * @return a string, s1 its own representation 
     */    
    public String toString() {
        String s1 = "";

        s1 += "--*-- Puzzle File Name --*--\n";
        s1 += String.format("puzzle filename: %s %n", puzzleFileName);
        
        s1 += "--*-- Level --*--\n";
        s1 += String.format("current/max: %d/%d %n", currentLevel, maxLevel);
        
        s1 += "--*-- Bishop Position --*--\n";
        if (bishopPosition != null) {
            s1 += String.format("%s %n", bishopPosition.toString());
        }

        s1 += "--*-- Pawn Position --*--\n";
        if (pawnPosition != null) {
            for (int i = 0; i < pawnPosition.size(); i++) {
                s1 += String.format("%d: %s %n", i, pawnPosition.get(i).toString());
            }
        }
                
        s1 += "--*-- Board Position --*--\n";
        for (int y = 0; y < 8; y++) {
            String row = "";
            for (int x = 0; x < 8; x++) {
                row += Character.toString(occupiedPosition[y][x]) + " ";
            }
            s1 += row + "\n";
        }
        
        s1 += "--*-- Pawns Left --*--\n";        
        s1 += String.format("%d %n", numberOfPawns);

        s1 += "--*-- Path --*--\n";
        if (path != null) {
            for (int i = 0; i < path.size(); i++) {
                s1 += String.format("%d: %s %n", i, path.get(i).toString());
            }
        }
        
        s1 += "--*-- Stats --*--\n";        
        s1 += String.format("C/M/U: %d/%d/%d %n", numberOfClicks, numberOfMoves, numberOfUndos);        
        
        return s1;
    }    
    
    /**
     * Description: allow the caller to check if the game is over
     * Pre condition: object must exist
     * Post condition: return a boolean
     * @return true if game is over, false otherwise
     */    
    public boolean isGameOver() {
        return (numberOfPawns == 0 && pawnPosition != null);
    }

    /**
     * Description: allow the caller to check if the path is empty
     * Pre condition: object must exist
     * Post condition: return a boolean
     * @return true if path is empty, false otherwise
     */    
    public boolean isPathEmpty() {
        return path.isEmpty();
    }
    
    /**
     * Description: allow the caller to advance the game level
     * Pre condition: object must exist
     * Post condition: advance to the current level if currentLevel < maxLevel
     * @return true if game can be advanced, false otherwise
     */    
    public boolean advanceLevel() {
        System.out.println("INFO: before advanceLevel() " + currentLevel);
        
        if (currentLevel >= maxLevel) {
            return false;
        }
        currentLevel++;
        System.out.println("INFO: after advanceLevel() " + currentLevel);
        return true;
    }

    /**
     * Description: allow the caller to track the number of undos
     * Pre condition: object must exist
     * Post condition: increment counter and update the status
     */    
    public void addUndos() {
        ++numberOfUndos;
        updateStatus();
    }

    /**
     * Description: allow the caller to track the number of clicks
     * Pre condition: object must exist
     * Post condition: increment counter and update the status
     */        
    public void addClicks() {
        ++numberOfClicks;
        updateStatus();
    }

    /**
     * Description: allow the caller to track the number of moves
     * Pre condition: object must exist
     * Post condition: increment counter and update the status
     */        
    public void addMoves() {
        ++numberOfMoves;
        updateStatus();
    }    

    // -----------------------------------------------------------------------
    // Protected Methods
    // -----------------------------------------------------------------------
    
    /**
     * Description: allow caller to update status
     * Pre condition: myManager must exist
     * Post condition: update the status 
     */    
    protected void updateStatus() {
        if (myManager == null) {
            return;
        }
        
        int score = numberOfMoves*4 + numberOfClicks - numberOfUndos*2;
        
        String newStatus = String.format(statusSpecifier, 
                puzzleFileName, 
                score,
                numberOfClicks, numberOfMoves, numberOfUndos);
                
        myManager.updateStatus(newStatus);
    }
    
    /**
     * Concept: Reading from files.
     * Description: allow caller to read the index file
     * Pre condition: object must exist
     * Post condition: read the index file from the file system
     * 
     * sample index file format: 
     * # pawnNumber fileNumber
     * 3 16
     * 4 17
     * 5 17
     * 6 16
     * 
     * The last file number of pawn number 5 is 17
     */    
    protected void readIndexFile() {
        // read index file and build a dict.
        // dict[pawn_number] = file_number
        if (dict == null) {
            dict = new HashMap<Integer, Integer>();
        }
        
        try {
            String file = "./dataset/index.txt";
            System.out.println(">>> READING FROM FILE " + file);            
            
            FileReader fr = new FileReader(file);
            Scanner s = new Scanner(fr);
            
            String line = "";
            
            while(s.hasNextLine()){ // read through the  file
                line = s.nextLine();
                if (line.startsWith("#"))
                    continue;
                
                String[] list = line.split(" ", 2);

                int pawnNumber = Integer.parseInt(list[0]);
                int fileNumber = Integer.parseInt(list[1]);
                
                String s1 = String.format("%d %d", pawnNumber, fileNumber);
                System.out.println(s1);
                
                dict.put(pawnNumber, fileNumber);
            }
            
            s.close();
        }
        catch (IOException e) {
            System.out.println("Error reading from file");
        }        
    }
    
    /**
     * Concept: writing to files.
     * Description: allow caller to write to the index file
     * Pre condition: object must exist
     * Post condition: write the index file to the file system
     */        
    protected void writeIndexFile() {
        FileWriter fw = null;
        
        try {
            String file = "./dataset/index.txt";
            fw = new FileWriter(file);

            System.out.println(">>> WRITING TO FILE " + file);            
        }
        catch (IOException e) {
            System.out.println("Error writing to file");
            return;
        }             
        
        PrintWriter pw = new PrintWriter(fw);
        String s1 = "";
        
        s1 = "# pawnNumber fileNumber";
        pw.println(s1);   

        for (Integer key : dict.keySet()) {
            Integer value = dict.get(key);
            s1 = String.format("%d %d", key, value);
            pw.println(s1);
        }
        
        pw.close();
    }
    
}
