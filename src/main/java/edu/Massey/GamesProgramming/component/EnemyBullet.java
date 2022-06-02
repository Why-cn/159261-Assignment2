package edu.Massey.GamesProgramming.component;

import java.awt.*;

import edu.Massey.GamesProgramming.Main;
import edu.Massey.GamesProgramming.utility.*;
import static edu.Massey.GamesProgramming.Main.lives;

/*
Boss bullet class
 */

public class EnemyBullet extends ParentCompo {

    public AudioClip playerPlaneExplosion;

    public EnemyBullet(Image img, int x, int y, int width, int height, double speed, Main main) {
        super(img, x, y, width, height, speed, main);
        playerPlaneExplosion = AudioClip.loadAudio("src/main/resources/sounds/planeexplosion.wav");
        //enemyPlaneShoot = AudioClip.loadAudio("src/main/resources/sounds/shot.wav");
    }
    //public AudioClip enemyPlaneShoot;

    @Override
    public void paintObj(Graphics graphics) {
        super.paintObj(graphics);
        y += speed;
        //Collision check(Player&Enemy bullets)
        if (this.getRec().intersects(this.jframe.planeFighter.getRec())) {
            if (lives != 0) {
                lives--;
                Explode explodeObj = new Explode(x, y);
                Utilities.explodeArrayList.add(explodeObj);
                AudioClip.playAudio(playerPlaneExplosion);
                this.x = -300;
                this.y = 300;
                Utilities.deleteList.add(explodeObj);
                Utilities.deleteList.add(this);
            } else {
                Main.status = GameStatus.FAIL;
            }
        }
        //Enemy bullet disappear when hit the lower frame
        if (y > 600) {
            this.x = -1;
            this.y = -1;
            Utilities.deleteList.add(this);
        }
    }

    @Override
    public Rectangle getRec() {
        return super.getRec();
    }
}

