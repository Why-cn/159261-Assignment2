package edu.Massey.GamesProgramming;

import edu.Massey.GamesProgramming.component.*;
import edu.Massey.GamesProgramming.utility.*;
import edu.Massey.GamesProgramming.Settings.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/*
Entrance of the app
 */

public class Main extends JFrame {
    // === Basic Settings ===

    //JFrame size
    int width = 600;
    int height = 600;
    //Count of repaint frame
    int frame = 1;

    // === Hard Level Settings ===
    // To keep other files read, using static
    // int[] = {Player Plane(PP) Lives, Boss appears when score is reached, PP Fire Rate, Normal Enemy Generate Rate,
    // BOSS Fire Rate, Boss life}
    private static int[] easy = {5, 20, 15, 12, 20, 15};
    private static int[] normal = {4, 30, 18, 10, 15, 20};
    private static int[] hard = {3, 40, 20, 8, 12, 25};
    private static int[] test = {5, 1, 15, 12, 20, 20};
    private static int[] currentLevel = {0,0,0,0,0,0};

    private static String difficulty = "";
    private static int gameEffectsVolume = 0;
    private static int musicVolume = 0;

    //Fighter life
    public static int lives = currentLevel[0];
    // Reaching score of the boss appearing
    private static int reachingScore = currentLevel[1];
    private static int planeFireRate = currentLevel[2]; // less is faster
    private static int enemyGeneRate = currentLevel[3]; // less is more frequent
    private static int bossFireRate = currentLevel[4]; // less is faster
    public static int bossLife = currentLevel[5];


    // === Sound settings ===
    private static final String soundBase = "src/main/resources/sounds/";
    public AudioClip menuBgm;
    public static final String menuBgmPath = soundBase + "menumusic.wav";
    public AudioClip planeBgm;
    public static final String planeBgmPath = "src/main/resources/bgm/bgm.wav";
    public AudioClip planeShoot;
    public static final String planeShootPath = soundBase + "shot.wav";
    public AudioClip bossAppear;
    public static final String bossAppearPath = soundBase + "bossappear.wav";
    public AudioClip bossShoot;
    public static final String bossShootPath = soundBase + "bossshot.wav";
    public AudioClip planeExplosion;
    public static final String planeExplosionPath = soundBase + "explosion.wav";
    public AudioClip bossExplosion;
    public static final String bossExplosionPath = soundBase + "bossexplosion.wav";
    public AudioClip playerPlaneExplosion;
    public static final String playerPlaneExplosionPath = soundBase + "planeexplosion.wav";
    public AudioClip gameWin;
    public static final String gameWinPath = soundBase + "stageclear.wav";
    public AudioClip gameLose;
    public static final String gameLosePath = soundBase + "gameover.wav";


    // === Init Settings ===
    // Background
    Background bg = new Background(Utilities.backgroundImg, 0, -1000, 2);
    // Player plane fighter
    public PlaneFighter planeFighter = new PlaneFighter(Utilities.fighterImg, 290, 550, 20, 30, 0, this);
    // Boss plane
    public EnemyBoss boss = null;
    // Number of enemy
    int enemyNum = 0;
    // Game Status
    public static GameStatus status = GameStatus.INITIALIZE;
    // Score
    public static int score = 0;
    //Cache image
    Image cacheImage = null;

    // === Entrance of the game ===
    public static void main(String[] args) {
        loadSettings();
        Main gameFrame = new Main();
        gameFrame.start();
    }

    // === Initialize ===
    public void start() {
        this.setVisible(true);
        this.setSize(width, height);
        this.setLocationRelativeTo(null);
        this.setTitle("ShootingStar Team Show");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        Utilities.componentList.add(bg);
        Utilities.componentList.add(planeFighter);
        //Load sounds
        menuBgm = AudioClip.loadAudio(menuBgmPath);
        planeBgm = AudioClip.loadAudio(planeBgmPath);
        planeShoot = AudioClip.loadAudio(planeShootPath);
        planeExplosion = AudioClip.loadAudio(planeExplosionPath);
        gameWin = AudioClip.loadAudio(gameWinPath);
        gameLose = AudioClip.loadAudio(gameLosePath);
        bossAppear = AudioClip.loadAudio(bossAppearPath);
        bossShoot = AudioClip.loadAudio(bossShootPath);
        playerPlaneExplosion = AudioClip.loadAudio(playerPlaneExplosionPath);
        bossExplosion = AudioClip.loadAudio(bossExplosionPath);
        //Start menu BGM
        AudioClip.startAudioLoop(menuBgm);
        //Click mouse to start
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == 1 && status == GameStatus.INITIALIZE) {
                    status = GameStatus.PLAYING;
                    //Start playing BGM
                    AudioClip.stopAudioLoop(menuBgm);
                    AudioClip.startAudioLoop(planeBgm);
                }
            }
        });

        // Key Control
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_SPACE) {
                    switch (status) {
                        case PLAYING -> {
                            status = GameStatus.PAUSE;
                            repaint();
                        }
                        case PAUSE -> status = GameStatus.PLAYING;
                        case FAIL, PASS -> restart();
                        default -> {
                        }
                    }
                } else if (key == KeyEvent.VK_ENTER && status == GameStatus.INITIALIZE) {
                    status = GameStatus.MENU;
                /*
                } else if (key == KeyEvent.VK_1 && status == GameStatus.MENU) {
                    currentLevel = easy;
                } else if (key == KeyEvent.VK_2 && status == GameStatus.MENU) {
                    currentLevel = normal;
                } else if (key == KeyEvent.VK_3 && status == GameStatus.MENU) {
                    currentLevel = hard;

                */
                } else if (key == KeyEvent.VK_ENTER && status == GameStatus.MENU) {
                    status = GameStatus.PLAYING;
                }
            }
        });

        //Recreate & repaint everything
        while (true) {
            if (status == GameStatus.PLAYING) {
                createCompo();
                repaint();
            }
            try {
                Thread.sleep(25);
            } catch (InterruptedException ignored) {
            }
        }
    }

    //Paint&repaint
    @Override
    public void paint(Graphics g) {
        if (cacheImage == null) {
            cacheImage = createImage(width, height);
        }
        Graphics graphics = cacheImage.getGraphics();
        graphics.fillRect(0, 0, width, height);
        graphics.drawImage(Utilities.backgroundImg, 0, 0, null);
        frame++;
        if (status == GameStatus.INITIALIZE) {
            // Init Menu
            Utilities.drawMessage(graphics, "WWⅡ Plane Shooting fighter", Color.white, 40, 50, 150);
            Utilities.drawMessage(graphics, "Click mouse to start game", Color.white, 30, 150, 350);
            Utilities.drawMessage(graphics, "Mouse to control the plane", Color.white, 20, 200, 400);
            Utilities.drawMessage(graphics, "Space to pause the game", Color.white, 20, 200, 440);
            Utilities.drawMessage(graphics, "Enter to set the hard level", Color.white, 20, 200, 480);
        }
        if (status == GameStatus.MENU) {
            // Hard Level Menu
            Utilities.drawMessage(graphics, "WWⅡ Plane Shooting fighter", Color.white, 40, 50, 150);
            Utilities.drawMessage(graphics, "Type key to set hard level", Color.white, 30, 150, 350);
            Utilities.drawMessage(graphics, "1 - easy(default), 2 - normal, 3 - hard", Color.white, 20, 200, 400);
            Utilities.drawMessage(graphics, "Enter to start the game", Color.white, 20, 200, 440);
        }
        if (status == GameStatus.PLAYING) {
            Utilities.componentList.addAll(Utilities.explodeArrayList);
            //Playing
            for (int i = 0; i < Utilities.componentList.size(); i++) {
                Utilities.componentList.get(i).paintObj(graphics);
            }
            Utilities.componentList.removeAll(Utilities.deleteList);
        }
        if (status == GameStatus.PAUSE) {
            //Pause
            Utilities.drawMessage(graphics, "Click space to continue game", Color.yellow, 40, 60, 300);
        }
        if (status == GameStatus.FAIL) {
            //Being shot down
            lives = 0;
            AudioClip.playAudio(playerPlaneExplosion);
            AudioClip.stopAudioLoop(planeBgm);
            graphics.setColor(Color.BLACK);
            graphics.fillRect(0, 0, width, height);
            Utilities.drawMessage(graphics, "GAME OVER", Color.RED, 50, 150, 300);
            Utilities.drawMessage(graphics, "Hint: Don't crash onto the boss!", Color.yellow, 30, 50, 500);
            Utilities.drawMessage(graphics, "Click space to restart game", Color.yellow, 40, 60, 400);
            AudioClip.playAudio(gameLose);
        }
        if (status == GameStatus.PASS) {
            //Defeat the Boss
            AudioClip.playAudio(bossExplosion);
            AudioClip.stopAudioLoop(planeBgm);
            graphics.setColor(Color.BLUE);
            graphics.fillRect(0, 0, width, height);
            Utilities.drawMessage(graphics, "YOU WIN", Color.GREEN, 50, 160, 300);
            Utilities.drawMessage(graphics, "Click space to restart game", Color.yellow, 40, 60, 400);

            AudioClip.playAudio(gameWin);
        }
         if (status != GameStatus.INITIALIZE) {
            Utilities.drawMessage(graphics, score + " points", Color.white, 30, 30, 100);
            Utilities.drawMessage(graphics, "Plane: " + lives, Color.white, 30, 450, 100);
        }
        g.drawImage(cacheImage, 0, 0, null);
    }

    //Player bullet & Enemy plane & Boss plane and bullet
    void createCompo() {
        if (frame % planeFireRate == 0) {
            Utilities.fighterBulletList.add(new FighterBullet(Utilities.fighterBulletImg, planeFighter.getX() + 17, planeFighter.getY() - 20, 15, 30, 4, this));
            Utilities.componentList.add(Utilities.fighterBulletList.get(Utilities.fighterBulletList.size() - 1));
            AudioClip.playAudio(planeShoot, 0.1f);
        }
        if (frame % enemyGeneRate == 0) {
            Utilities.enemyArrayList.add(new NormalEnemy(Utilities.enemyImg, (int) (Math.random() * 12) * 50, 0, 48, 37, 6, this));
            Utilities.componentList.add(Utilities.enemyArrayList.get(Utilities.enemyArrayList.size() - 1));
            enemyNum++;
        }
        //Boss appear
        if (score > reachingScore) {
            if (boss == null) {
                boss = new EnemyBoss(Utilities.enemyBossImg, 250, 36, 150, 100, 6, this);
                Utilities.componentList.add(boss);
                AudioClip.playAudio(bossAppear);
            }
            if (frame % bossFireRate == 0 && boss != null) {
                Utilities.enemyBulletList.add(new EnemyBullet(Utilities.enemyBulletImg, boss.getX() + 50, boss.getY() + 70, 15, 25, 6, this));
                Utilities.componentList.add(Utilities.enemyBulletList.get(Utilities.enemyBulletList.size() - 1));
                AudioClip.playAudio(bossShoot);
            }
        }
    }

    void restart() {
        score = 0;
        lives = currentLevel[0];
        Utilities.componentList.clear();
        Utilities.componentList.add(bg);
        Utilities.componentList.add(planeFighter);
        boss = null;
        status = GameStatus.PLAYING;
        AudioClip.startAudioLoop(planeBgm);
    }
    private static void loadSettings() {
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

    private static void convertStringToSettings(String s) {
        String[] data = s.split("=");
        if (data[0].equals("difficulty")){
            difficulty = data[1];
            if (difficulty.equals("Easy")){
                currentLevel = easy;
            }
            if (difficulty.equals("Normal")){
                currentLevel = normal;
            }
            if (difficulty.equals("Hard")){
                currentLevel = hard;
            }
            lives = currentLevel[0];
            reachingScore = currentLevel[1];
            planeFireRate = currentLevel[2];
            enemyGeneRate = currentLevel[3];
            bossFireRate = currentLevel[4];
            bossLife = currentLevel[5];

            
        }
        if(data[0].equals("musicVolume")){
            musicVolume = Integer.parseInt(data[1]);
        }
        if(data[0].equals("gameEffectsVolume")) {
            gameEffectsVolume = Integer.parseInt(data[1]);
        }
    }
}