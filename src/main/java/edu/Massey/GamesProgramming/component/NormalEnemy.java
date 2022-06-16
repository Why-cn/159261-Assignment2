package edu.Massey.GamesProgramming.component;

import java.awt.*;

import edu.Massey.GamesProgramming.Main;
import edu.Massey.GamesProgramming.utility.*;
import static edu.Massey.GamesProgramming.Main.lives;

/*
Enemy small plane class
 */

public class NormalEnemy extends ParentCompo {

    public AudioClip planeExplosion;

    public NormalEnemy(Image img, int x, int y, int width, int height, double speed, Main main) {
        super(img, x, y, width, height, speed, main);
        planeExplosion = AudioClip.loadAudio(Main.planeExplosionPath);
    }

    @Override
    public void paintObj(Graphics graphics) {
        y += speed;
        super.paintObj(graphics);
        //Collision check(Player&Enemy)
        if (this.getRec().intersects(this.jframe.planeFighter.getRec())) {
            if (lives != 0) {
                lives--;
                Explode explodeObj = new Explode(x, y);
                Utilities.explodeArrayList.add(explodeObj);
                AudioClip.playAudio(planeExplosion, Main.gameEffectsVolume);
                this.x = -200;
                this.y = 200;
                Utilities.deleteList.add(explodeObj);
                Utilities.deleteList.add(this);
            } else {
                Main.status = GameStatus.FAIL;
            }
        }
        //Enemy plane disappear when hit the lower frame
        if (y > 600) {
            this.x = -100;
            this.y = -100;
            Utilities.deleteList.add(this);
        }
        //Collision check(Player bullet&Enemy plane)
        for (FighterBullet fighterBullet : Utilities.fighterBulletList) {
            if (this.getRec().intersects(fighterBullet.getRec())) {
                Explode explode = new Explode(x, y);
                Utilities.explodeArrayList.add(explode);
                AudioClip.playAudio(planeExplosion, Main.gameEffectsVolume);
                fighterBullet.setX(-100);
                fighterBullet.setY(-100);
                this.x = -200;
                this.y = -200;
                Utilities.deleteList.add(fighterBullet);
                Utilities.deleteList.add(explode);
                Utilities.deleteList.add(this);
                Main.score++;
            }
        }
    }

    @Override
    public Rectangle getRec() {
        return super.getRec();
    }
}
