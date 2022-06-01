package edu.Massey.GamesProgramming.component;

import edu.Massey.GamesProgramming.Main;
import edu.Massey.GamesProgramming.utility.AudioClip;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/*
Player plane class
 */

public class PlaneFighter extends ParentCompo {

    public AudioClip playerPlaneExplosion;

    public PlaneFighter(Image img, int x, int y, int width, int height, double speed, Main main) {
        super(img, x, y, width, height, speed, main);
        playerPlaneExplosion = AudioClip.loadAudio("src/main/resources/sounds/planeexplosion.wav");
        this.jframe.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                PlaneFighter.super.x = e.getX() - 16;
                PlaneFighter.super.y = e.getY() - 16;
            }
        });
    }

    @Override
    public Image getImg() {
        return super.getImg();
    }

    @Override
    public void paintObj(Graphics graphics) {
        super.paintObj(graphics);
        //Collision check(Player&Boss)
        if (this.jframe.boss != null && this.getRec().intersects(this.jframe.boss.getRec())) {
            Main.status = 3;
        }
    }

    @Override
    public Rectangle getRec() {
        return super.getRec();
    }
}