package edu.Massey.GamesProgramming.component;

import edu.Massey.GamesProgramming.Main;
import edu.Massey.GamesProgramming.utility.*;

import java.awt.*;

/*
Player plane bullet class
 */

public class FighterBullet extends ParentCompo {

    public FighterBullet(Image img, int x, int y, int width, int height, double speed, Main main) {
        super(img, x, y, width, height, speed, main);
    }

    @Override
    public Image getImg() {
        return super.getImg();
    }

    @Override
    public void paintObj(Graphics graphics) {
        super.paintObj(graphics);
        y -= speed;
        //Player bullets disappear when hit the upper frame
        if (y < 0) {
            this.x = -100;
            this.y = -100;
            Utilities.deleteList.add(this);
        }
    }

    @Override
    public Rectangle getRec() {
        return super.getRec();
    }
}
