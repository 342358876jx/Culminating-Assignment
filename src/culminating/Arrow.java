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
import processing.core.PImage;

public class Arrow {
    private PApplet app;          // Reference to the PApplet (Processing sketch)
    private PImage image;         // Arrow image

    private float x, y;           // Current position of the arrow
    private float angle;          // Current angle of the arrow
    private boolean flying;       // Whether the arrow has been shot and is in motion
    private float speed = 15;     // Speed at which the arrow travels

    private float baseX = 110;    // Initial X position before shooting
    private float baseY = 295;    // Initial Y position before shooting
    private float baseAngle;      // Initial angle before shooting

    // Constructor with custom image path
    public Arrow(PApplet app, String path) {
        this.app = app;
        this.image = app.loadImage(path);    // Load image from given path
        this.image.resize(120, 15);          // Resize arrow image
        reset();                             // Reset position and angle
    }

    // Overloaded constructor with default image path
    public Arrow(PApplet app) {
        this.app = app;
        this.image = app.loadImage("defaultArrow.png");  // Load default image
        this.image.resize(120, 15);
        reset();
    }

    // Rotate and move the arrow before shooting
    public void rotateAndTranslate(float deltaAngle, float dx, float dy) {
        if (!flying) {
            float newAngle = baseAngle + deltaAngle;

            // Limit how far the arrow can rotate
            if (newAngle >= -20 && newAngle <= 8) {
                baseAngle = newAngle;
                baseX += dx;
                baseY += dy;
                x = baseX;
                y = baseY;
                angle = baseAngle;
            }
        }
    }

    // Fires the arrow by setting it in motion
    public void shoot() {
        if (!flying) {
            x = baseX;
            y = baseY;
            angle = baseAngle;
            flying = true;
        }
    }

    // Updates the arrow’s position if flying
    public void update() {
        if (flying) {
            float radians = PApplet.radians(angle);
            x += speed * PApplet.cos(radians);  // Move in direction of angle
            y += speed * PApplet.sin(radians);

            // Reset arrow if it goes off screen
            if (x < -image.width || x > app.width + image.width ||
                y < -image.height || y > app.height + image.height) {
                reset();
            }
        }
    }

    // Resets arrow to its starting position and stops motion
    public void reset() {
        x = baseX;
        y = baseY;
        angle = baseAngle;
        flying = false;
    }

    // Draws the arrow on screen with current rotation
    public void display() {
        app.pushMatrix();
        app.translate(x, y);                     // Move to current position
        app.rotate(PApplet.radians(angle));      // Rotate based on angle
        app.imageMode(PApplet.CENTER);           // Center the image
        app.image(image, 0, 7);                  // Draw the image slightly offset
        app.popMatrix();
    }

    // Getter for base X position
    public float getX() {
        return baseX;
    }

    // Getter for base Y position
    public float getY() {
        return baseY;
    }

    // Getter for base angle
    public float getAngle() {
        return baseAngle;
    }

    // Returns whether the arrow is currently flying
    public boolean isFlying() {
        return flying;
    }

    // Checks collision between the arrow tip and the given sun coordinates
    public boolean isColliding(float sunX, float sunY) {
        float radians = PApplet.radians(angle);
        float tipX = x + (image.width / 2f) * PApplet.cos(radians);  // Arrow tip X
        float tipY = y + (image.width / 2f) * PApplet.sin(radians);  // Arrow tip Y
        float distance = PApplet.dist(tipX, tipY, sunX, sunY);       // Distance from sun
        return distance < 35;                                        // Collision threshold
    }
    
    
}