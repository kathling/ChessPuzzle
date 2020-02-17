/**
 * Project Name: POC_Chess_Puzzle
 * Class Name: MenuUI
 * Description: The MenuUI allows the caller to mutate and access the UI
 * for the main menu.
 */

package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import view.ChessBoardUI; 
import model.State;
import controller.Manager;

public class MenuUI {

    // -----------------------------------------------------------------------
    // Class Instance Variables
    // -----------------------------------------------------------------------
    
    JFrame frame; //a frame
    
    // -----------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------
    
    /**
     * Description: allow the caller to create a MenuUI object
     * Pre condition:
     * Post condition: MenuUI object is created
     */    
    public MenuUI() {
        
    }

    // -----------------------------------------------------------------------
    // Public Methods
    // -----------------------------------------------------------------------

    /**
     * Description: allow the caller to create and show the ui
     * Pre condition: object must exist
     * Post condition: create and show the ui to the user
     * 
     * This method is adopted from the Oracle website, Java Tutorials Code Sample – BorderLayoutDemo.java
     * 
     * Reference: https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing/examples/layout/BorderLayoutDemoProject/src/layout/BorderLayoutDemo.java 
     */    
    public void createAndShowGUI() {

        try {
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

        //Create and set up the window.
        frame = new JFrame("Chess Menu POC");
        frame.setPreferredSize(new Dimension(200, 200));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Set up the content pane.
        addComponentsToPane(frame.getContentPane());
        //Use the content pane's default BorderLayout. No need for
        //setLayout(new BorderLayout());
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    // -----------------------------------------------------------------------
    // Private Methods
    // -----------------------------------------------------------------------
    
    /**
     * Description: add components to the container
     * Pre condition: object must exist
     * Post condition: add components to the container and setup the listener
     * @param pane specifies the container to which the components will be added
     */    
    private void addComponentsToPane(Container pane) {
         
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
         
        JButton button;
        button = new JButton("CREATE PUZZLE");
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setAlignmentY(Component.CENTER_ALIGNMENT);
        pane.add(button);
        //
        button.addActionListener(new MainMenu_Listener());
        
        button = new JButton("PLAY GAME (10+)");
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setAlignmentY(Component.CENTER_ALIGNMENT);
        pane.add(button);
        //
        button.addActionListener(new MainMenu_Listener());

        button = new JButton("PLAY GAME (< 10)");
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setAlignmentY(Component.CENTER_ALIGNMENT);
        pane.add(button);
        //
        button.addActionListener(new MainMenu_Listener());
        
        button = new JButton("LEADERBOARD");
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setAlignmentY(Component.CENTER_ALIGNMENT);
        pane.add(button);
        //
        button.addActionListener(new MainMenu_Listener());        
        
        button = new JButton("EXIT");
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setAlignmentY(Component.CENTER_ALIGNMENT);
        pane.add(button);
        //
        button.addActionListener(new MainMenu_Listener());
    }

    /**
     * Description: allow the caller to start a new game 
     * Pre condition: instance object must exist
     * Post condition: create and show UI
     * @param isForKid specifies if game is played by kid
     */    
    private void startNewGame(boolean isForKid) {
        // create the controller
        Manager m = new Manager();
        
        // create the theme for kid or classic chessboard
        Theme t;
        if (isForKid) {
            t = new KidTheme();
            t.loadAsset();
        } else {
            t = new ClassicTheme();
            t.loadAsset();
        }
        
        // create the view
        // note that modal=false, which allow multiple views.
        ChessBoardUI ui = new PlayPuzzleUI(m, t);
        m.setMainUI(ui);
        ui.createAndShowGUI(frame, false);
        
        // start the game
        m.startNewGame();
        ui.showDialog();
    }
    
    /**
     * Description: allow the caller to create a puzzle
     * Pre condition: instance object must exist
     * Post condition: create and show UI
     */        
    private void createPuzzle() {
        // create the controller
        Manager m = new Manager();

        // create the theme
        Theme t = new ClassicTheme();
        t.loadAsset();
        
        // create the view
        // note that modal=true, which allow a single view. 
        ChessBoardUI ui = new CreatePuzzleUI(m, t);
        m.setMainUI(ui);
        ui.createAndShowGUI(frame, true);
        
        // create the puzzle
        m.createPuzzle();
        ui.showDialog();
    }

    // -----------------------------------------------------------------------
    // Private Class
    // -----------------------------------------------------------------------
    
    /**
     * Project Name: POC_Chess_Puzzle
     * Class Name: MainMenu_Listener
     * Description: The MainMenu_Listener allows the caller to perform actions
     * based on the command. It is an action listener for the main menu.
     */    
    private class MainMenu_Listener implements ActionListener {
        
        /**
         * Description: allow the caller to perform an action when they click a
         * button
         * Pre condition: object must exist, the param e can not be null
         * Post condition: perform the caller's command if valid, break 
         * otherwise
         * @param e specifies the action event, what command the method must 
         * perform
         */            
        public void actionPerformed(ActionEvent e) {
            switch(e.getActionCommand()) {
                case "CREATE PUZZLE":
                    createPuzzle();
                    break;
                case "PLAY GAME (10+)":
                    startNewGame(false);
                    break;
                case "PLAY GAME (< 10)":
                    startNewGame(true);
                    break;
                case "LEADERBOARD":
                    //nothing for now
                    break;
                case "EXIT":
                    System.exit(0);
                default:
                    System.out.println("ERROR: Unknown command");
                    break;
            }
        }
    }    

    
}

