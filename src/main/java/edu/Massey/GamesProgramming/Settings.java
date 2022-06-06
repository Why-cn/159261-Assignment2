package edu.Massey.GamesProgramming;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class Settings extends JFrame {
    private JFrame frame;
    private JPanel panel;

    int numOfLives = 0;
    int musicVolume = 0;
    int gameEffectsVolume = 0;

    public static void main(String[] args){
        MainMenu s = new MainMenu();
    }
    public Settings() {
        loadSettings();
        setupGUI();
    }
    private void setupGUI() {
        frame = new JFrame();
        panel = new JPanel();

        int winHeight = 400;
        int winWidth = 600;

        frame.setTitle("Game Settings");
        frame.setSize(winWidth,winHeight);
        panel.setLayout(null);
        frame.setLocationRelativeTo(null);

        JLabel settingsHeader = new JLabel("Settings Menu");
        settingsHeader.setFont(new Font("Calibri", Font.PLAIN, 18));
        settingsHeader.setBounds(240,20,120,30);

        JButton mainMenuButton = new JButton("Main Menu");
        JButton quitButton = new JButton("Quit");
        JButton saveButton = new JButton("Save Settings");

        mainMenuButton.setBounds(150, 320,100,35);
        saveButton.setBounds(250,320,120,35);
        quitButton.setBounds(370, 320, 100,35);

        JLabel numOfLivesL = new JLabel("Number of Lives:");
        JSlider numOfLivesS = new JSlider(JSlider.HORIZONTAL,1,10,numOfLives);
        numOfLivesS.setMajorTickSpacing(1);
        numOfLivesS.setPaintLabels(true);
        numOfLivesS.setPaintTicks(true);

        numOfLivesL.setBounds(110,70,120,25);
        numOfLivesS.setBounds(230, 70, 150, 50);


        JLabel musicVolumeL = new JLabel("Music Volume:");
        JSlider musicVolumeS = new JSlider(JSlider.HORIZONTAL,0,100,musicVolume);
        musicVolumeS.setMajorTickSpacing(10);
        musicVolumeS.setPaintLabels(true);
        musicVolumeS.setPaintTicks(true);

        musicVolumeL.setBounds(125,150,120,25);
        musicVolumeS.setBounds(230, 150,250,50);

        JLabel gameEffectsVolumeL = new JLabel("Game Effects Volume:");
        JSlider gameEffectsVolumeS = new JSlider(JSlider.HORIZONTAL, 0,100,gameEffectsVolume);
        gameEffectsVolumeS.setMajorTickSpacing(10);
        gameEffectsVolumeS.setPaintLabels(true);
        gameEffectsVolumeS.setPaintTicks(true);

        gameEffectsVolumeL.setBounds(85,230,170,25);
        gameEffectsVolumeS.setBounds(230,230,250,50);



        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });


        mainMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
               MainMenu s = new MainMenu();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                numOfLives = numOfLivesS.getValue();
                musicVolume = musicVolumeS.getValue();
                gameEffectsVolume = gameEffectsVolumeS.getValue();
                System.out.println(numOfLives + " " + musicVolume + " " + gameEffectsVolume);
                saveGameSettings();
                JOptionPane.showMessageDialog(null,"Settings have successfully been saved!");
            }
        });

        panel.add(settingsHeader);
        panel.add(gameEffectsVolumeL);
        panel.add(gameEffectsVolumeS);
        panel.add(musicVolumeL);
        panel.add(musicVolumeS);
        panel.add(numOfLivesL);
        panel.add(numOfLivesS);
        panel.add(saveButton);
        panel.add(mainMenuButton);
        panel.add(quitButton);
        frame.add(panel);
        frame.setVisible(true);
    }

    private void loadSettings() {
        {
            try {
                File file = new File("src/main/resources/settings.txt");
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);
                String line;
                while ((line = br.readLine()) != null) {
                    convertStringToSettings(line);
                }
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void convertStringToSettings(String s) {
        String[] data = s.split("=");
        if (data[0].equals("numOfLives")){
            numOfLives = Integer.parseInt(data[1]);
        }
        if(data[0].equals("musicVolume")){
            musicVolume = Integer.parseInt(data[1]);
        }
        if(data[0].equals("gameEffectsVolume")) {
            gameEffectsVolume = Integer.parseInt(data[1]);
        }
    }

    private void saveGameSettings() {
        File file = new File("settings.txt");
        if (file.delete()) {

        }
        else{
            System.out.println("Failed to deleted file");
        }
        try {
            FileWriter newFile = new FileWriter("settings.txt");
            newFile.write("numOfLives=" + numOfLives + "\nmusicVolume="+musicVolume+"\ngameEffectsVolume="+gameEffectsVolume);
            newFile.close();
        }
        catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
