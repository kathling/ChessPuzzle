/**
 * Project Name: POC_Chess_Puzzle
 * Class Name: Position
 * Description: The Position allows the caller to mutate and access the 
 * position of a piece.
 */
package model;

public class Position {
    
    // -----------------------------------------------------------------------
    // Class Instance Variables
    // -----------------------------------------------------------------------
    
    private int x; // x position
    private int y; // y position

    // -----------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------
    
    /**
     * Description: allow the caller to create a Position object
     * Pre condition: x and y can not be null
     * Post condition: Position object is created
     * @param x specifies the x position
     * @param y specifies the y position
     */    
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Description: allow the caller to create a Position object using a string
     * representation
     * Pre condition: line can not be null
     * Post condition: Position object is created
     * @param line specifies the new x and y position
     */      
    public Position(String line) {
        // read a max of 2 numbers
        String[] list = line.split(" ", 2);
        
        this.x = Integer.parseInt(list[0]);
        this.y = Integer.parseInt(list[1]);
    }

    // -----------------------------------------------------------------------
    // Mutator (aka Setter)
    // -----------------------------------------------------------------------

    /**
     * Description: set the new x position
     * Pre condition: object must exist
     * Post condition: set a new x position to the object
     * @param newX specifies the new x position
     */    
    public void setX(int newX) {
        this.x = newX;
    }

    /**
     * Description: set the new y position
     * Pre condition: object must exist
     * Post condition: set a new y position to the object
     * @param newY specifies the new y position
     */        
    public void setY(int newY) {
        this.y = newY;
    }    

    // -----------------------------------------------------------------------
    // Accessor (aka Getter)
    // -----------------------------------------------------------------------
    
    /**
     * Description: get the x position
     * Pre condition: object must exist
     * Post condition: return the x position
     * @return x position
     */        
    public int getX() {
        return x;
    }

    /**
     * Description: get the y position
     * Pre condition: object must exist
     * Post condition: return the y position
     * @return y position
     */            
    public int getY() {
        return y;
    }   
    
    // -----------------------------------------------------------------------
    // Public Methods
    // -----------------------------------------------------------------------

    /**
     * Description: allow the object to show its representation  
     * Pre condition: object must exist 
     * Post condition: return its own representation as a string
     * @return a string, s its own representation 
     */
    public String toString() {
        String s = String.format("%d %d", x, y);
        return s;
    }
    
    /**
     * Description: allow the object to compare equality to another position
     * Pre condition: both objects must exist
     * Post condition: return a boolean value
     * @param pos specifies another Position object
     * @return true if the x and y values of pos are equal to the object, false
     * otherwise
     */    
    public boolean isEqual(Position pos) {
        return (x == pos.x && y == pos.y);
    }
    
    /**
     * Description: allow the object to check if it is diagonal with another 
     * position
     * Pre condition: both objects must exist
     * Post condition: return a boolean value
     * @param pos specifies another Position object
     * @return true if pos is diagonal to the object, false otherwise
     */        
    public boolean isDiagonal(Position pos) {
        // find the absolute distance between the x coordinates
        int deltaX = Math.abs(x - pos.x); 
        // find the absolute distance between the y coordinates
        int deltaY = Math.abs(y - pos.y);
        
        return (deltaX == deltaY);
    }
    
}
