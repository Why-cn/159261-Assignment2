package edu.Massey.GamesProgramming;

import edu.Massey.GamesProgramming.component.*;
import edu.Massey.GamesProgramming.utility.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/*
Entrance of the app
 */

public class Main extends JFrame {
    //JFrame size
    int width = 600;
    int height = 600;
    //Count of repaint frame
    int frame = 1;
    //Number of enemy
    int enemyNum = 0;
    //Status 0:initialize 1:playing 2:pause 3:fail 4:pass
    public static int status = 0;
    //Score
    public static int score = 0;
    //Fighter life
    public static int lives = 3;
    //Cache image
    Image cacheImage = null;
    //Sound setting
    public AudioClip menuBgm;
    public AudioClip planeBgm;
    public AudioClip planeShoot;
    public AudioClip bossAppear;
    public AudioClip bossShoot;
    public AudioClip planeExplosion;
    public AudioClip bossExplosion;
    public AudioClip playerPlaneExplosion;
    public AudioClip gameWin;
    public AudioClip gameLose;
    //Background
    Background bg = new Background(Utilities.backgroundImg, 0, -1000, 2);
    //Player plane fighter
    public PlaneFighter planeFighter = new PlaneFighter(Utilities.fighterImg, 290, 550, 20, 30, 0, this);
    //Boss plane
    public EnemyBoss boss = null;

    //Entrance of the game
    public static void main(String[] args) {
        Main gameFrame = new Main();
        gameFrame.start();
    }

    //Initialize
    public void start() {
        this.setVisible(true);
        this.setSize(width, height);
        this.setLocationRelativeTo(null);
        this.setTitle("ShootingStar Team Show");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        Utilities.componentList.add(bg);
        Utilities.componentList.add(planeFighter);
        //Load sounds
        menuBgm = AudioClip.loadAudio("src/main/resources/sounds/menumusic.wav");
        planeBgm = AudioClip.loadAudio("src/main/resources/bgm/bgm.wav");
        planeShoot = AudioClip.loadAudio("src/main/resources/sounds/shot.wav");
        planeExplosion = AudioClip.loadAudio("src/main/resources/sounds/explosion.wav");
        gameWin = AudioClip.loadAudio("src/main/resources/sounds/stageclear.wav");
        gameLose = AudioClip.loadAudio("src/main/resources/sounds/gameover.wav");
        bossAppear = AudioClip.loadAudio("src/main/resources/sounds/bossappear.wav");
        bossShoot = AudioClip.loadAudio("src/main/resources/sounds/bossshot.wav");
        playerPlaneExplosion = AudioClip.loadAudio("src/main/resources/sounds/planeexplosion.wav");
        bossExplosion = AudioClip.loadAudio("src/main/resources/sounds/bossexplosion.wav");
        //Start menu BGM
        AudioClip.startAudioLoop(menuBgm);
        //Click mouse to start
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == 1 && status == 0) {
                    status = 1;
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
                        case 1 -> {
                            status = 2;
                            repaint();
                        }
                        case 2 -> status = 1;
                        default -> {
                        }
                    }
                }
            }
        });

        //Recreate & repaint everything
        while (true) {
            if (status == 1) {
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
        if (status == 0) {
            //Menu
            Utilities.drawMessage(graphics, "WWⅡ Plane Shooting fighter", Color.white, 40, 50, 150);
            Utilities.drawMessage(graphics, "Click mouse to start game", Color.white, 30, 150, 350);
            Utilities.drawMessage(graphics, "Mouse to control the plane", Color.white, 20, 200, 400);
            Utilities.drawMessage(graphics, "Space to pause the game", Color.white, 20, 200, 440);
        }
        if (status == 1) {
            Utilities.componentList.addAll(Utilities.explodeArrayList);
            //Playing
            for (int i = 0; i < Utilities.componentList.size(); i++) {
                Utilities.componentList.get(i).paintObj(graphics);
            }
            Utilities.componentList.removeAll(Utilities.deleteList);
        }
        if (status == 2) {
            //Pause
            Utilities.drawMessage(graphics, "Click space to continue game", Color.yellow, 40, 60, 300);
        }
        if (status == 3) {
            //Being shot down
            AudioClip.playAudio(playerPlaneExplosion);
            AudioClip.stopAudioLoop(planeBgm);
            graphics.fillRect(0, 0, width, height);
            Utilities.drawMessage(graphics, "GAME OVER", Color.RED, 50, 150, 300);
            AudioClip.playAudio(gameLose);
        }
        if (status == 4) {
            //Defeat the Boss
            AudioClip.playAudio(bossExplosion);
            AudioClip.stopAudioLoop(planeBgm);
            graphics.fillRect(0, 0, width, height);
            Utilities.drawMessage(graphics, "YOU WIN", Color.green, 50, 160, 300);
            AudioClip.playAudio(gameWin);
        }
         if (status != 0) {
            Utilities.drawMessage(graphics, score + " points", Color.white, 30, 30, 100);
            Utilities.drawMessage(graphics, "Plane: " + lives, Color.white, 30, 450, 100);
        }
        g.drawImage(cacheImage, 0, 0, null);
    }

    //Player bullet & Enemy plane & Boss plane and bullet
    void createCompo() {
        if (frame % 20 == 0) {
            Utilities.fighterBulletList.add(new FighterBullet(Utilities.fighterBulletImg, planeFighter.getX() + 17, planeFighter.getY() - 20, 15, 30, 4, this));
            Utilities.componentList.add(Utilities.fighterBulletList.get(Utilities.fighterBulletList.size() - 1));
            AudioClip.playAudio(planeShoot, 0.1f);
        }
        if (frame % 12 == 0) {
            Utilities.enemyArrayList.add(new NormalEnemy(Utilities.enemyImg, (int) (Math.random() * 12) * 50, 0, 48, 37, 6, this));
            Utilities.componentList.add(Utilities.enemyArrayList.get(Utilities.enemyArrayList.size() - 1));
            enemyNum++;
        }
        //Boss appear
        if (score > 20) {
            if (boss == null) {
                boss = new EnemyBoss(Utilities.enemyBossImg, 250, 36, 150, 100, 6, this);
                Utilities.componentList.add(boss);
                AudioClip.playAudio(bossAppear);
            }
            if (frame % 12 == 0 && boss != null) {
                Utilities.enemyBulletList.add(new EnemyBullet(Utilities.enemyBulletImg, boss.getX() + 50, boss.getY() + 70, 15, 25, 6, this));
                Utilities.componentList.add(Utilities.enemyBulletList.get(Utilities.enemyBulletList.size() - 1));
                AudioClip.playAudio(bossShoot);
            }
        }
    }
}