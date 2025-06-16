/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author jojox
 */
package culminating;

import processing.core.PApplet;

public class BossSun extends Sun {
    private float upperBound, lowerBound;
    private float speed = 2;
    private int health = 100;

    public BossSun(PApplet p, String imagePath, float x, float y, float upperBound, float lowerBound) {
        super(p, imagePath);
        this.x = x;
        this.y = y;
        this.upperBound = upperBound;
        this.lowerBound = lowerBound;
        this.image.resize(160, 160); // enlarged boss sun
    }

    public void update() {
        y += speed;
        if (y > lowerBound || y < upperBound) {
            speed *= -1;
        }
    }

    public void display() {
        p.imageMode(PApplet.CENTER);
        p.image(image, x, y);
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health < 0) health = 0;
    }

    public int getHealth() {
        return health;
    }
}
