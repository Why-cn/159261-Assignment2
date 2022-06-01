package edu.Massey.GamesProgramming.component;

import java.awt.*;

/*
Exploding class
 */

public class Explode extends ParentCompo {

    static Image[] exploding = new Image[16];
    int count = 0;
    static {
        for (int i = 0; i < exploding.length; i++) {
            exploding[i] = Toolkit.getDefaultToolkit().getImage("src/main/resources/imgs/explode/e" + (i + 1) + ".gif");
        }
    }

    public Explode(int x, int y) {
        super(x, y);
    }

    @Override
    public void paintObj(Graphics graphics) {
        if (count < 16) {
            super.img = exploding[count];
            super.paintObj(graphics);
            count++;
        }
    }
}

