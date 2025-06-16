/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author jojox
 */
// MiniSun.java
package culminating;
import processing.core.PApplet;
import java.util.Random;

public class MiniSun {
    private PApplet p;
    float x, y;
    float vx, vy;
    int timer;

    public MiniSun(PApplet p, float centerX, float centerY) {
        this.p = p;
        Random rand = new Random();
        x = centerX + rand.nextInt(100) - 50;
        y = centerY + rand.nextInt(100) - 50;
        vx = rand.nextFloat() * 2 - 1;
        vy = rand.nextFloat() * 2 - 1;
        timer = 0;
    }

    public void update() {
        x += vx;
        y += vy;
        timer++;
    }

    public void display() {
        p.fill(255, 200, 0);
        p.noStroke();
        p.ellipse(x, y, 30, 30);
    }

    public boolean isHit(float mx, float my) {
        return PApplet.dist(mx, my, x, y) < 20;
    }
}
