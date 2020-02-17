/**
 * Project Name: POC_Chess_Puzzle
 * Class Name: CaptureState
 * Description: The CaptureState allows the caller to mutate and access the 
 * game state.
 */

package model;

import controller.Manager;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class CaptureState extends State {
    
    // -----------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------
    
    /**
     * Description: allow the caller to create a CaptureState object
     * Pre condition: the Manager object must exist
     * Post condition: CaptureState object is created and myManager is set to 
     * newManager
     * @param newManager specifies a Manager object
     */
    public CaptureState(Manager newManager) {
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
        
        loadGame(selectPuzzle(currentLevel));
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
        // Skip if tile is not occupied
        if (occupiedPosition[newPos.getY()][newPos.getX()] == '.') {
            return false;
        }

        // Skip if new position is the bishop
        if (newPos.isEqual(bishopPosition)) {
            return false;
        }
        
        // Skip if new position is not a diagonal move
        if (!newPos.isDiagonal(bishopPosition)) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Description: allow the caller to move position
     * Pre condition: object and Position must exist
     * Post condition: move the bishop position and decrement the number of 
     * pawns
     * @param newPos specifies the position to move
     */    
    @Override
    public void movePosition(Position newPos) {
        numberOfPawns--;
        setBishopPosition(newPos);
    }
    

    /**
     * Description: allow the caller to undo a move
     * Pre condition: object and Position must exist
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
            return;
        }
        
        Position lastPos;
        lastPos = path.remove(lastIndex);
        addUndos();
        
        // set the bishopPosition and pawns back to the last position
        if (bishopPosition != null) {
            occupiedPosition[bishopPosition.getY()][bishopPosition.getX()] = 'P';
            bishopPosition = lastPos;
            occupiedPosition[bishopPosition.getY()][bishopPosition.getX()] = 'Q';
            numberOfPawns++;  
        } else {
            // nothing for now.
        }
    }
    
    // -----------------------------------------------------------------------
    // Private Methods
    // -----------------------------------------------------------------------

    /**
     * Description: allow the caller to set the bishop position
     * Pre condition: object must exist, occupiedPosition and path must be 
     * initialized
     * Post condition: replace old bishop position with '.', new position with 
     * 'Q'
     * @param newPos specifies the bishop's new position
     */    
    private void setBishopPosition(Position newPos) {
        occupiedPosition[bishopPosition.getY()][bishopPosition.getX()] = '.';  
        path.add(bishopPosition);     
        bishopPosition = newPos;
        occupiedPosition[newPos.getY()][newPos.getX()] = 'Q';
    } 
    
    /**
     * Description: allow caller to get a random number 
     * Pre condition: max must be greater than min
     * Post condition: return the random number between min and max
     * @param min specifies the minimum number
     * @param max specifies the maximum number
     * @return an integer; -1 if the argument is invalid
     */    
    private int getRandomNumber(int min, int max) {
        if (min > max) {
            return -1;
        }
        int range = max - min + 1;
        int rand = (int)(Math.random() * range) + min;
        return rand;
    }

    /**
     * Description: allow the caller to initialize the occupied position 
     * Pre condition: occupiedPosition and object must exist
     * Post condition: initialize the occupiedPosition based on the 
     * bishopPosition and pawnPosition
     */    
    private void initializeOccupiedPosition() {
        // bishop position 
        occupiedPosition[bishopPosition.getY()][bishopPosition.getX()] = 'Q';
        
        // pawn position
        for (int i = 0; i < pawnPosition.size(); i++) {
            Position pos = pawnPosition.get(i);
            occupiedPosition[pos.getY()][pos.getX()] = 'P';
        }
    }
    
    /**
     * Concept: Reading from files.
     * Description: allow caller to load a game given a puzzleFileName
     * Pre condition: file and objects must exist
     * Post condition: read the puzzle file and load into the internal 
     * representation
     * @param puzzleFileName specifies the file to load
     * 
     * sample input file format:
     * 
     *  #--*-- Bishop Position --*--
     *  4 2
     *  #--*-- Pawn Position --*--
     *  6 4
     *  5 5
     *  1 1
     * 
     */    
    private void loadGame(String puzzleFileName) {
        pawnPosition = new ArrayList<>();
        try {
            String fileSpecifier = "./dataset/puzzle/%s.txt";
            String file = String.format(fileSpecifier, puzzleFileName);
            
            System.out.println(">>> READING FROM FILE " + file);            
            
            FileReader fr = new FileReader(file);
            Scanner s = new Scanner(fr);
            
            String line = "";
            
            while(s.hasNextLine()){
                line = s.nextLine();
                
                // skip line if it starts with # (for comment) 
                if (line.startsWith("#")) {
                    continue;
                }
                
                // create a position from the given line
                Position pos = new Position(line);
            
                // the first position will be the bishop's position
                // the rest will be the pawn position
                if (bishopPosition == null) {
                    bishopPosition = pos;
                } else {
                    pawnPosition.add(pos);
                }
            }
            
            numberOfPawns = pawnPosition.size();
            initializeOccupiedPosition();
            
            s.close();
        }
        catch (IOException e) {
            System.out.println("ERROR: reading from file");
        }
        
    }    
    
    /**
     * Description: allow caller to generate a puzzle file
     * Pre condition: fromLevel <= toLevel
     * Post condition: return a new puzzle file
     * @param fromLevel specifies the min level
     * @param toLevel specifies the max level
     * @return a string, the file name
     */    
    private String generatePuzzleFile(int fromLevel, int toLevel) {
        int selectedLevel = getRandomNumber(fromLevel, toLevel); 
        
        if (selectedLevel == -1) {
            return "";
        }
 
        // if not exist then read the index file
        if (dict == null) {
            readIndexFile();
        }
        int selectedLevelMaxFile = dict.get(selectedLevel);

        // get a random number from 1 to selectedLevelMaxFile
        int selectedFile = getRandomNumber(1, selectedLevelMaxFile);
        String s = String.format("%02d-%04d", selectedLevel, selectedFile);
        return s;
    }
    
    /**
     * Description: allow the caller to select a puzzle
     * Pre condition: file can not be empty
     * Post condition: update the file name and return puzzle file name
     * @param level specifies the level of the game
     * @return a string, the puzzle file name
     */    
    private String selectPuzzle(int level) {
        String file = "";
        
        // generate files within a range of pawns
        switch(level){
            case 0:
                file = generatePuzzleFile(4, 4);
                break;
            case 1:
                file = generatePuzzleFile(5, 6);
                break;
            case 2:
                file = generatePuzzleFile(7, 8);
                break;
            case 3:
                file = generatePuzzleFile(9, 10);
                break;
            case 4:
                file = generatePuzzleFile(11, 12);
                break;
            case 5:
                file = generatePuzzleFile(13, 14);
                break;
            case 6:
                file = generatePuzzleFile(15, 16);
                break;
            case 7:
                file = generatePuzzleFile(17, 18);
                break;
            case 8:
                file = generatePuzzleFile(18, 19);
                break;                
            case 9:
                file = generatePuzzleFile(19, 20);
                break;
            default:
        }
    
        puzzleFileName = file;
        
        // update the file name
        updateStatus();
        
        return file;
    }
}
