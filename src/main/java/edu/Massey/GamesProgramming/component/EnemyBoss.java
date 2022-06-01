package edu.Massey.GamesProgramming.component;

import edu.Massey.GamesProgramming.Main;
import edu.Massey.GamesProgramming.utility.*;

import java.awt.*;

/*
Boss class
 */

public class EnemyBoss extends ParentCompo {

    public AudioClip planeExplosion;
    int life = 20;

    public EnemyBoss(Image img, int x, int y, int width, int height, double speed, Main main) {
        super(img, x, y, width, height, speed, main);
        planeExplosion = AudioClip.loadAudio("src/main/resources/sounds/explosion.wav");
    }

    @Override
    public void paintObj(Graphics graphics) {
        super.paintObj(graphics);
        if (x > 550 || x < -40) {
            speed = -speed;
        }
        x += speed;
        for (FighterBullet fighterBullet : Utilities.fighterBulletList) {
            if (this.getRec().intersects(fighterBullet.getRec())) {
                Explode explode = new Explode(x, y);
                Utilities.explodeArrayList.add(explode);
                AudioClip.playAudio(planeExplosion);
                fighterBullet.setX(-100);
                fighterBullet.setY(100);
                Utilities.deleteList.add(fighterBullet);
                life--;
            }
            if (life <= 0) {
                Main.status = 4;
            }
        }
        //White backcolor of life value
        graphics.setColor(Color.white);
        graphics.fillRect(20, 40, 100, 10);
        //Life value
        graphics.setColor(Color.red);
        graphics.fillRect(20, 40, life * 100 / 20, 10);
    }

    @Override
    public Rectangle getRec() {
        return super.getRec();
    }
}
