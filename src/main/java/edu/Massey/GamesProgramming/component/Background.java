package edu.Massey.GamesProgramming.component;

import java.awt.*;

/*
Background class
 */

public class Background extends ParentCompo {

    public Background(Image img, int x, int y, double speed) {
        super(img, x, y, speed);
    }

    @Override
    public void paintObj(Graphics graphics) {
        super.paintObj(graphics);
        y += speed;
        if (y >= 0) {
            y = -1000;
        }
    }
}
