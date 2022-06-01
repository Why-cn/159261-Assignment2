package edu.Massey.GamesProgramming.utility;

import edu.Massey.GamesProgramming.component.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/*
Game utilities class
 */

public class Utilities {
    //Background
    public static Image backgroundImg = Toolkit.getDefaultToolkit().getImage("src/main/resources/imgs/background.jpg");
    //Boss
    public static Image enemyBossImg = Toolkit.getDefaultToolkit().getImage("src/main/resources/imgs/enemyboss.png");
    //Player plane fighter
    public static Image fighterImg = Toolkit.getDefaultToolkit().getImage("src/main/resources/imgs/fighter.png");
    //Player bullet
    public static Image fighterBulletImg = Toolkit.getDefaultToolkit().getImage("src/main/resources/imgs/fighterBullet.png");
    //Enemy bullet
    public static Image enemyBulletImg = Toolkit.getDefaultToolkit().getImage("src/main/resources/imgs/enemyBullet.png");
    //Enemy plane
    public static Image enemyImg = Toolkit.getDefaultToolkit().getImage("src/main/resources/imgs/enemy.png");
    //List of shown obj
    public static List<ParentCompo> componentList = new ArrayList<>();
    //List of disappear obj
    public static List<ParentCompo> deleteList = new ArrayList<>();
    //List of player bullet
    public static List<FighterBullet> fighterBulletList = new ArrayList<>();
    //List of enemy bullet
    public static List<EnemyBullet> enemyBulletList = new ArrayList<>();
    //List of enemy plane
    public static List<NormalEnemy> enemyArrayList = new ArrayList<>();
    //List of explosion
    public static List<Explode> explodeArrayList = new ArrayList<>();

    //Draw message
    public static void drawMessage(Graphics graphics, String str, Color color, int size, int x, int y) {
        graphics.setColor(color);
        graphics.setFont(new Font("Serif", Font.BOLD, size));
        graphics.drawString(str, x, y);
    }
}
