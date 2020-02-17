/**
 * Project Name: POC_Chess_Puzzle
 * Class Name: ChessBoardUI
 * Description: ChessBoardUI allows the caller to mutate and access the UI
 * for the chess board.
 */

package view;

import javax.swing.*;
import javax.swing.UIManager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
 
import java.awt.Dialog;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import controller.Manager;
import java.awt.Font;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import model.*;

public class ChessBoardUI {

    // -----------------------------------------------------------------------
    // Class Instance Variables
    // -----------------------------------------------------------------------
    
    JDialog dialog; 
    DefaultTableModel defaultTableModel; // table used to show path
    JLabel labelStatus; // stats
 
    String[][] scoreArray; // array that stores scores
    
    // -----------------------------------------------------------------------
    // Class Instance Variables (Protected)
    // -----------------------------------------------------------------------
    
    // objects
    protected Manager myManager = null;
    protected Theme myTheme = null;
    protected State gameState = null;
        
    protected JButton[][] tiles = new JButton[8][8]; // board is created from buttons
    protected String[] mainMenu; // button names for the main menu
    protected ImageIcon[] mainIcon; // button images for the main menu
    
    protected JButton[] level = new JButton[9];
    
    // -----------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------
    
    /**
     * Description: allow the caller to create a ChessBoardUI object
     * Pre condition: newManager and newTheme can not be null
     * Post condition: ChessBoardUI object is created
     * @param newManager specifies the Manager object
     * @param newTheme specifies the Theme object 
     */ 
    public ChessBoardUI(Manager newManager, Theme newTheme) {
        this.myManager = newManager;
        this.myTheme = newTheme;
    }
    
    // -----------------------------------------------------------------------
    // Public Methods (Override)
    // -----------------------------------------------------------------------
    
    public void installHandler() {
        
    }

    public void initializeLevel() {
        
    }
    
    public void undo() {
    }   
    
    // -----------------------------------------------------------------------
    // Public Methods
    // -----------------------------------------------------------------------

    /**
     * Description: allow the caller to create and show the ui
     * Pre condition: object must exist
     * Post condition: create and show the ui to the user
     * @param parent specifies the container object
     * @param modal specifies if modal window
     * 
     * This method is adopted from the Oracle website, Java Tutorials Code Sample – BorderLayoutDemo.java
     * 
     * Reference: https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing/examples/layout/BorderLayoutDemoProject/src/layout/BorderLayoutDemo.java 
     */       
    public void createAndShowGUI(JFrame parent, boolean modal) {

        try {
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        
        //
        // See Java Modality in Dialogs
        //
        // https://docs.oracle.com/javase/tutorial/uiswing/misc/modality.html
        //
        
        if (modal) {
            dialog = new JDialog(parent, "Chess POC", Dialog.ModalityType.DOCUMENT_MODAL);
        } else {
            dialog = new JDialog(parent, "Chess POC");
        }
        dialog.setLocation(200, 50);
        dialog.setSize(new Dimension(1000, 800));
       
        Container c = dialog.getContentPane();
        addComponentsToPane(c);
        
        // For Modal Window, we have to set visibility to FALSE to allow the UI
        // to continue; later we let the caller to call the ShowDialog to set
        // visibility to TRUE.
        //
        // For Modeless Window, we can set visibility to TRUE and continue.
        //
        if (modal) {
            dialog.setVisible(false);
        } else {
            dialog.setVisible(true);
        }        
        
    }
    
    /**
     * Description: allows the caller to set dialog's visibility to TRUE
     * Pre condition: object must exist
     * Post condition: set dialog's visibility to TRUE
     */    
    public void showDialog() {
        dialog.setVisible(true);
    }

    /**
     * Description: allow the caller to clear the board
     * Pre condition: object must exist
     * Post condition: clear the board and remove the action listener 
     */    
    public void clear() {
        for (int y = 0; y < tiles.length; y++) {
            for (int x = 0; x < tiles.length; x++) {
                JButton btn = tiles[y][x];
                btn.setIcon(null);
                for(ActionListener g : btn.getActionListeners()) {
                    btn.removeActionListener(g);
                }
            }
        }
    }
    
    /**
     * Description: allow the caller to render the game state
     * Pre condition: object must exist
     * Post condition: set tiles according to the bishop and pawn position
     * @param gameState specifies the game's state, used to access the bishop's
     * position
     */    
    public void renderState(State gameState) {
        this.gameState = gameState;

        if (gameState.getBishopPosition() == null) {
            return;
        }
        setTiles(gameState.getBishopPosition(), myTheme.getBishopIcon());
        
        for (int i = 0; i < gameState.getNumberOfPawns(); i++) {
            setTiles(gameState.getPawnPositionByIndex(i), myTheme.getPawnIcon());
        }
    }   

    /**
     * Description: allow the caller to advance to the next level
     * Pre condition: object must exist
     * Post condition: enable level i
     * @param i specifies the level to enable
     */    
    public void advanceLevel(int i) {
        level[i].setEnabled(true);
    }
    
    /**
     * Description: allow the caller to update the status
     * Pre condition: object must exist, labelStatus can not be null
     * Post condition: labelStatus' text is set to newStatus 
     * @param newStatus specifies the new status
     */    
    public void updateStatus(String newStatus) {
        if (labelStatus == null)
            return;
        labelStatus.setText(newStatus);
    }
    
    // -----------------------------------------------------------------------
    // Protected Methods
    // -----------------------------------------------------------------------
    
    /**
     * Description: allow the caller to set tiles 
     * Pre condition: the object must exist, icon can not be null
     * Post condition: tile is set to the icon
     * @param pos specifies the position in which the icon will be set
     * @param icon specifies the ImageIcon that will be set
     */    
    protected void setTiles(Position pos, ImageIcon icon) {
        tiles[pos.getY()][pos.getX()].setIcon(icon);
    }    
    
    // -----------------------------------------------------------------------
    // Protected Class
    // -----------------------------------------------------------------------   

    /**
     * Project Name: POC_Chess_Puzzle
     * Class Name: MoveHandler
     * Description: The MoveHandler allows the caller to perform actions
     * based on the command. It is an action listener to handle the moves.
     */       
    protected class MoveHandler implements ActionListener {
        Position pos;
        ImageIcon icon;
        boolean checkGameOver;
        
        
        public MoveHandler(Position pos, ImageIcon icon, boolean checkGameOver) {
            this.pos = pos;
            this.icon = icon;
            this.checkGameOver = checkGameOver;
        }
        
        public void actionPerformed(ActionEvent e) {
            String s = String.format("New Position (x,y) = %s", pos.toString());
            System.out.println(s);

            gameState.addClicks();
           
            if (!gameState.isValidMove(pos)){ 
                // add wav file
                new AePlayWave(myTheme.soundInvalidMove).start();
                
                return;
            }

            gameState.addMoves();
            
            if (gameState.getBishopPosition() != null) {
                setTiles(gameState.getBishopPosition(), icon);
            }
            addMove(pos);
            setTiles(pos, myTheme.getBishopIcon());

            gameState.movePosition(pos);
            System.out.println(gameState.toString());

            // add wav file
            new AePlayWave(myTheme.soundCapture).start();
            
            if (checkGameOver && gameState.isGameOver()) {
                clearTable();
                if (!myManager.advanceNextLevel()) {

                    // add wav file
                    new AePlayWave(myTheme.soundGameOver).start();
                    
                    showScoreDialog();
                    
                    // close the UI dialog and go back to main menu ui.
                    dialog.dispose();
                }
                else {
                    // add wav file
                    new AePlayWave(myTheme.soundNextLevel).start();
                }
            }
        }
        
        private void showScoreDialog0() {

            String s1 = labelStatus.getText();

            JTextArea mytext = new JTextArea();
            mytext.setText("mytextline1\nmytextline2\nmytextline3\nmytextline4\nmytextline5\nmytextline6");
            mytext.setRows(5);
            mytext.setColumns(10);
            mytext.setEditable(true);
            
            // Data to be displayed in the JTable 
            String[][] data = { 
                { "Kundan Kumar Jha", "4031", "CSE" }, 
                { "Anand Jha", "6014", "IT" } 
            }; 

            // Column Names 
            String[] columnNames = { "Name", "Roll Number", "Department" };             
            
            JScrollPane mypane = new JScrollPane(mytext);

            Object[] objarr = {
                new JLabel(s1),
                mypane,
            };

            JOptionPane pane = new JOptionPane(objarr, JOptionPane.PLAIN_MESSAGE);            

            
            JDialog d = pane.createDialog(null, "result");
            d.setLocation(200,50);
            d.setSize(new Dimension(500,500));
            
            d.setResizable(true);
            d.setVisible(true);
            
            dialog.dispose();
        }
        
        private void showScoreDialog() {

            readScoreFile();

            String statusText = labelStatus.getText();
            String s1 = "";

            int i = statusText.indexOf('S');
            s1 += statusText.substring(i);
            
            // Column Names 
            String[] columnNames = { "Name", "Score", "Clicks", "Moves", "Undos" };             

            // Initializing the JTable 
            JTable j = new JTable(scoreArray, columnNames); 
            j.setBounds(30, 40, 200, 300); 

            // adding it to JScrollPane 
            JScrollPane mypane = new JScrollPane(j); 

            Object[] objarr = {
                new JLabel(s1),
                new JLabel("Keep playing !!! You have a long way to go...."),
                new JLabel("Leaderboard"),
                mypane,
            };

            JOptionPane pane = new JOptionPane(objarr, JOptionPane.PLAIN_MESSAGE);            

            JDialog d = pane.createDialog(null, "Your Score and Stat");
            d.setLocation(200,50);
            d.setSize(new Dimension(500,800));
            
            d.setResizable(true);
            d.setVisible(true);
            
            dialog.dispose();
        }
        
    }
    
    /**
     * Description: allow the caller to read the score file
     * Pre condition: object must exist, scoreArray can not be null, file must 
     * contain values
     * Post condition: read through the file and print out its content
     */        
    private void readScoreFile() {
        
        scoreArray = new String[2500][5];
        int i = 0;
        
        try {
            String file = "./dataset/score/scorefile.csv";
            System.out.println(">>> READING FROM FILE " + file);            
            
            FileReader fr = new FileReader(file);
            Scanner s = new Scanner(fr);
            
            String line = "";
            
            while(s.hasNextLine()){ // read through the file
                line = s.nextLine();
                if (line.startsWith("#"))
                    continue;
                
                String[] list = line.split(",", 5);

                String name = list[0];
                String score = list[1];
                String clicks = list[2];
                String moves = list[3];
                String undos = list[4];
                
                String s1 = String.format("%s %s %s %s %s", 
                        name, score, clicks, moves, undos);
                System.out.println(s1);
                
                scoreArray[i][0] = list[0];
                scoreArray[i][1] = list[1];
                scoreArray[i][2] = list[2];
                scoreArray[i][3] = list[3];
                scoreArray[i][4] = list[4];

                ++i;
            }
            
            s.close();
        }
        catch (IOException e) {
            System.out.println("Error reading from file");
        }        
    }
    
    // -----------------------------------------------------------------------
    // Private Class
    // -----------------------------------------------------------------------
    
    // Listener class to handle the actions taken place in the main menu
    private class MainMenu_Listener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            String command = e.getActionCommand();
            myManager.dispatch(command);
        }
    }    
    
    // -----------------------------------------------------------------------
    // Private Methods
    // -----------------------------------------------------------------------
    
    /**
     * Description: allow the caller to create the panel for the main menu
     * Pre condition: object must exist
     * Post condition: create buttons and add the main menu action listener to 
     * each button
     * @param none
     * @return none
     */    
    private JPanel createPanel_MainMenu() {
        
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        Dimension buttonDimension = new Dimension(97, 60);

        //myTheme.undoIcon;
        
        for (int i = 0; i < mainMenu.length; i++) {
            JButton button;
            button = new JButton(mainMenu[i], mainIcon[i]);
            button.setPreferredSize(buttonDimension);
            panel.add(button);
            //
            button.addActionListener(new MainMenu_Listener());
        }
        return panel;
    }  
  
    /**
     * Description: allow the caller to create the chess board
     * Pre condition: object must exist
     * Post condition: create and add JButtons to the tiles array
     * @param none
     * @return none
     */    
    private JPanel createPanel_ChessBoard() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8,8));

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                tiles[i][j] = new JButton();
                
                if((i + j) % 2 != 0) {
                    tiles[i][j].setBackground(myTheme.boardColorBlack);
                } else {
                    tiles[i][j].setBackground(myTheme.boardColorWhite);
                }
                panel.add(tiles[i][j]);               
            }
        }
        return panel;
    }    
    
    /**
     * Description: allow caller to create the path 
     * Pre condition: object must exist
     * Post condition: create a JTable with three columns for step, x, and y
     * @param none
     * @return none
     */    
    private JPanel createPanel_Path() {
        JPanel panel = new JPanel();
        //panel.setLayout(new FlowLayout());

        JTable j = new JTable();

        // Column Names 
        String[] columnNames = { "step", "x", "y" }; 
  
        defaultTableModel = new DefaultTableModel();
        defaultTableModel.setColumnIdentifiers(columnNames);
        
        j.setModel(defaultTableModel);
        
        TableColumnModel columnModel = j.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(40);
        columnModel.getColumn(1).setPreferredWidth(40);
        columnModel.getColumn(2).setPreferredWidth(40);
        
        // adding it to JScrollPane 
        JScrollPane sp = new JScrollPane(j); 
        sp.setPreferredSize(new Dimension(120, 650));
        panel.add(sp); 
        //panel.add(j);
        
        return panel;
    }
    
    /**
     * Description: create panel level
     * Pre condition: object must exist 
     * Post condition: create buttons and add ImageIcons
     * @param none
     * @return a panel, new panel with the level ImageIcons
     */
    private JPanel createPanel_Level() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(9, 1));
        Dimension buttonDimension = new Dimension(64, 64);
        
        for (int i = 0; i < 9; i++) {
            String file = String.format("./asset/level%d.png", i+1);
            //String file = String.format("./asset/level%d.png", 1);
            Image img = new ImageIcon(file).getImage();
            
            JButton button = new JButton("");
            button.setIcon(new ImageIcon(img));
            button.setEnabled(false);
            button.setPreferredSize(buttonDimension);
            button.setBackground(Color.WHITE);
            panel.add(button);
            
            level[i] = button;
        }
        
        return panel;
    }
    
    /**
     * Description: create panel status
     * Pre condition: object must exist
     * Post condition: add the rules, string s, to the panel
     * @param none
     * @return a panel, the new panel with the status
     */
    private JPanel createPanel_Status() {
        JPanel panel = new JPanel();

        String s = "";
        s += "Rules: ";
        s += "(1) Click any square to start ";
        s += "(2) Bishop can move in diagonal to an unoccupied position ";
        s += "(3) Save puzzle (recursively) !!!";
        
        labelStatus = new JLabel(s);
        labelStatus.setFont(new Font("Sans Serif", Font.PLAIN, 16));
        panel.add(labelStatus);
        return panel;
    }
    
    /**
     * Description: add components to the container
     * Pre condition: object must exist
     * Post condition: add components to the container 
     * @param pane specifies the container to which the components will be added
     * @return none
     */    
    private void addComponentsToPane(Container pane) {
         
        if (!(pane.getLayout() instanceof BorderLayout)) {
            pane.add(new JLabel("Container doesn't use BorderLayout!"));
            return;
        }
         
        JPanel panel;
        JButton button; 

        panel = createPanel_MainMenu();
        //button.setPreferredSize(new Dimension(200,100));
        pane.add(panel, BorderLayout.PAGE_START);

        panel = createPanel_ChessBoard();
        panel.setPreferredSize(new Dimension(600, 600));
        pane.add(panel, BorderLayout.CENTER);

        //button = new JButton("Button 3 (LINE_START)");
        panel = createPanel_Level(); 
        panel.setPreferredSize(new Dimension(80, 600));
        pane.add(panel, BorderLayout.LINE_START);
         
        //button = new JButton("Long-Named Button 4 (PAGE_END)");
        panel = createPanel_Status();
        //panel.setPreferredSize(new Dimension(100, 10));
        pane.add(panel, BorderLayout.PAGE_END);
        
        
        //button = new JButton("5 (LINE_END)");
        panel = createPanel_Path();
        panel.setPreferredSize(new Dimension(200, 650));
        //panel.setSize(new Dimension(100, 600));
        pane.add(panel, BorderLayout.LINE_END);

    }
    
    /**
     * Description: allow the caller to add a move to the table
     * Pre condition: the object must exist
     * Post condition: add the position to the table
     * @param pos specifies the position to add to the table
     * @return none
     */    
    private void addMove(Position pos) {
        if (defaultTableModel == null) {
            return;
        }

        int step = defaultTableModel.getRowCount() + 1;
        int x = pos.getX();
        int y = pos.getY();
        
        String[] row = new String[3];
        row[0] = String.valueOf(step);
        row[1] = String.valueOf(x);
        row[2] = String.valueOf(y);
        
        defaultTableModel.addRow(row);
        
    }

    /**
     * Description: allow the caller to clear the table
     * Pre condition: the object must exist
     * Post condition: all the rows will be removed from the table
     * @param none
     * @return none
     */    
    private void clearTable() {
        if (defaultTableModel == null) {
            return;
        }
        
        System.out.println("row count: " +  defaultTableModel.getRowCount());
        
        for (int i = defaultTableModel.getRowCount(); i> 0; i--) {
            defaultTableModel.removeRow(i-1);
        }
    }
    
    /**
     * Description: allow the caller to remove the last move in the table
     * Pre condition: object must exist
     * Post condition: the last row in the table will be removed
     */    
    protected void undoMove() {
        if (defaultTableModel == null) {
            return;
        }

        // zero-index
        int i = defaultTableModel.getRowCount() - 1;
        if (i < 0)
            return;
        defaultTableModel.removeRow(i);

        // add wav file
        new AePlayWave(myTheme.soundUndo).start();
       
    }
    
    // -----------------------------------------------------------------------
    // internal, not working
    // -----------------------------------------------------------------------
    
    protected class MouseHandler implements MouseListener {
        Position pos;
        Color color;
        
        public MouseHandler(Position pos) {
            this.pos = pos;
        }

        @Override
        public void mouseClicked(MouseEvent me) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

            if (color != null) {
                JButton btn = (JButton)me.getSource();
                btn.setBackground(color);
                color = null;
            }
        }

        @Override
        public void mousePressed(MouseEvent me) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseReleased(MouseEvent me) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseEntered(MouseEvent me) {
            String s = String.format("Enter Position (x,y) = %s", pos.toString());
            System.out.println(s);

            if (!gameState.isValidMove(pos)){ 
                return;
            }
           
            JButton btn = (JButton)me.getSource();
            color = btn.getBackground();
            btn.setBackground(Color.YELLOW);
        }

        @Override
        public void mouseExited(MouseEvent me) {
            String s = String.format("Exit Position (x,y) = %s", pos.toString());
            System.out.println(s);

            if (color != null) {
                JButton btn = (JButton)me.getSource();
                btn.setBackground(color);
                color = null;
            }
        }
        
    }
 
}

