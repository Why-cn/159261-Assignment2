package edu.Massey.GamesProgramming;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainMenu extends JFrame {
    public static void main(String[] args){
       MainMenu s = new MainMenu();
    }
    private JFrame frame;
    private JPanel panel;
    BufferedImage img1;
    JLabel plane1;
    BufferedImage img2;
    JLabel plane2;

    public MainMenu() {
        setupGUI();
    }
    private void setupGUI() {
        frame = new JFrame();
        panel = new JPanel();

        int winHeight = 400;
        int winWidth = 600;

        frame.setTitle("Main Menu");
        frame.setSize(winWidth,winHeight);
        panel.setLayout(null);
        frame.setLocationRelativeTo(null);

        panel.setBackground(Color.GRAY);

        JLabel header = new JLabel("WW2 Plane Fighter Game");
        header.setFont(new Font("Calibri", Font.PLAIN, 18));
        header.setBounds(200,20,250,25);

        try {
            img1 = ImageIO.read(new File("src/main/resources/imgs/plane1.png"));
            plane1 = new JLabel(new ImageIcon(img1));
            img2 = ImageIO.read(new File("src/main/resources/imgs/plane2.png"));
            plane2 = new JLabel(new ImageIcon(img2));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        plane1.setBounds(60,80,97,75);
        plane2.setBounds(440,80,97,75);

        JButton startGameButton = new JButton("Start Game");

        JButton helpButton = new JButton("Help");
        JButton settingsButton = new JButton("Settings");
        JButton quitButton = new JButton("Quit");

        startGameButton.setBounds(225,180,150,30);

        helpButton.setBounds(100, 220, 130, 30);
        settingsButton.setBounds(235, 220, 130, 30);
        quitButton.setBounds(370, 220,130,30);

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        "MTU Group 2.\n" +
                                "WW2 Plane Shooter Game.\n" +
                                "Game can be played solo or two player.\n" +
                                "Two player controls are WASD & Arrow Keys.\n" +
                                "Solo player control is done with the mouse.\n" +
                                "User can control difficulty in the settings.\n" +
                                "Aim of the game is to survive as long as possible and down as many bosses & planes."
                );
            }
        });

        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Settings j = new Settings();
                frame.dispose();
            }
        });

        startGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Main.main(null);
                frame.dispose();
            }
        });

        panel.add(plane1);
        panel.add(plane2);
        panel.add(header);
        panel.add(startGameButton);
        panel.add(helpButton);
        panel.add(settingsButton);
        panel.add(quitButton);
        frame.add(panel);
        frame.setVisible(true);
    }
}
