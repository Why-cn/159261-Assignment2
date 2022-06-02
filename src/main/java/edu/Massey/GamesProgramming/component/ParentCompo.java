package edu.Massey.GamesProgramming.component;

import edu.Massey.GamesProgramming.Main;

import java.awt.*;

/*
Basic obj class
 */

public class ParentCompo {
    Image img;
    int x;
    int y;
    int width;
    int height;
    double speed;
    Main jframe;

    public Image getImg() {
        return img;
    }

    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }

    public ParentCompo(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public ParentCompo(Image img, int x, int y, double speed) {
        this.img = img;
        this.x = x;
        this.y = y;
        this.speed = speed;
    }
    public ParentCompo(Image img, int x, int y, int width, int height, double speed, Main frame) {
        this.img = img;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.jframe = frame;
    }

    public void paintObj(Graphics graphics) {
        graphics.drawImage(img, x, y, null);
    }

    // Hitbox
    public Rectangle getRec() {
        return new Rectangle(x, y, width, height);
    }
}
