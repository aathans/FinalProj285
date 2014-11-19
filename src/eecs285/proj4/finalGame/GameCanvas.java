package eecs285.proj4.finalGame;

import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;
import javax.swing.*;

/**
 * Created by Alex on 11/17/14.
 */
abstract class GameCanvas extends JPanel implements KeyListener {

    private static boolean[] keyboardState = new boolean[525];
    public static JButton option;
    public static JComboBox<String> diffChoice;

    public GameCanvas(){
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.setBackground(Color.black);
        this.addKeyListener(this);
        option = new JButton("Options");
        option.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Window.setDisabled();
                final JFrame optionFrame = new JFrame("Options");
                JPanel Layout = new JPanel();
                Layout.setLayout(new BoxLayout(Layout, BoxLayout.Y_AXIS));
                JPanel difficultyLevel = new JPanel();
                difficultyLevel.setLayout(new FlowLayout(FlowLayout.LEFT));
                JLabel diffLabel = new JLabel("Difficulty Level");
                difficultyLevel.add(diffLabel);
                String[] diffString = new String[]{"Easy", "Medium", "Hard"};
                diffChoice = new JComboBox<String>(diffString);
                difficultyLevel.add(diffChoice);
                Layout.add(difficultyLevel);

                JButton okButton = new JButton("OK");
                okButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        optionFrame.dispose();
                        Window.setEnabled();
                    }
                });
                JPanel okPanel = new JPanel();
                okPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
                okPanel.add(okButton);
                Layout.add(okPanel);
                optionFrame.add(Layout);
                optionFrame.pack();
                optionFrame.setVisible(true);

            }
        });
        option.setFocusable(false);
        add(option);
    }

    public abstract void Draw(Graphics2D g2d);

    @Override
    public void paintComponent(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        super.paintComponent(g2d);
        Draw(g2d);
    }


    public static boolean keyboardKeyState(int key){
        return keyboardState[key];
    }

    @Override
    public void keyPressed(KeyEvent event){
        keyboardState[event.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent event){
        keyboardState[event.getKeyCode()] = false;
        keyReleasedLogic(event);
    }

    @Override
    public void keyTyped(KeyEvent event){

    }

    public abstract void keyReleasedLogic(KeyEvent event);


}
