package edu.Massey.GamesProgramming;

import edu.Massey.GamesProgramming.component.*;
import edu.Massey.GamesProgramming.utility.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

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
    private static int[] easy = {5, 30, 15, 12, 20};
    private static int[] test = {5, 1, 15, 12, 20};
    private static int[] currentHardLevel = test;
    //Fighter life
    public static int lives = currentHardLevel[0];
    // Reaching score of the boss appearing
    private int reachingScore = currentHardLevel[1];
    private int planeFireRate = currentHardLevel[2]; // less is faster
    private int enemyGeneRate = currentHardLevel[3]; // less is more frequent
    private int bossFireRate = currentHardLevel[4]; // less is faster


    // === Sound settings ===
    private final String soundbase = "src/main/resources/sounds/";
    public AudioClip menuBgm;
    private final String menuBgmPath = soundbase + "menumusic.wav";
    public AudioClip planeBgm;
    private final String planeBgmPath = "src/main/resources/bgm/bgm.wav";
    public AudioClip planeShoot;
    private final String planeShootPath = soundbase + "shot.wav";
    public AudioClip bossAppear;
    private final String bossAppearPath = soundbase + "bossappear.wav";
    public AudioClip bossShoot;
    private final String bossShootPath = soundbase + "bossshot.wav";
    public AudioClip planeExplosion;
    private final String planeExplosionPath = soundbase + "gameover.wav";
    public AudioClip bossExplosion;
    private final String bossExplosionPath = soundbase + "explosion.wav";
    public AudioClip playerPlaneExplosion;
    private final String playerPlaneExplosionPath = soundbase + "planeexplosion.wav";
    public AudioClip gameWin;
    private final String gameWinPath = soundbase + "stageclear.wav";
    public AudioClip gameLose;
    private final String gameLosePath = soundbase + "gameover.wav";


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

        //Space to Pause
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 32) {
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
            //Menu
            Utilities.drawMessage(graphics, "WWⅡ Plane Shooting fighter", Color.white, 40, 50, 150);
            Utilities.drawMessage(graphics, "Click mouse to start game", Color.white, 30, 150, 350);
            Utilities.drawMessage(graphics, "Mouse to control the plane", Color.white, 20, 200, 400);
            Utilities.drawMessage(graphics, "Space to pause the game", Color.white, 20, 200, 440);
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
            AudioClip.playAudio(playerPlaneExplosion);
            AudioClip.stopAudioLoop(planeBgm);
            graphics.setColor(Color.BLACK);
            graphics.fillRect(0, 0, width, height);
            Utilities.drawMessage(graphics, "GAME OVER", Color.RED, 50, 150, 300);
            Utilities.drawMessage(graphics, "Click space to restart game", Color.yellow, 40, 60, 300);
            AudioClip.playAudio(gameLose);
        }
        if (status == GameStatus.PASS) {
            //Defeat the Boss
            AudioClip.playAudio(bossExplosion);
            AudioClip.stopAudioLoop(planeBgm);
            graphics.setColor(Color.BLUE);
            graphics.fillRect(0, 0, width, height);
            Utilities.drawMessage(graphics, "YOU WIN", Color.GREEN, 50, 160, 300);
            Utilities.drawMessage(graphics, "Click space to restart game", Color.yellow, 40, 60, 300);

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
        lives = currentHardLevel[0];
        Utilities.componentList.clear();
        boss = null;
        status = GameStatus.PLAYING;
    }
}