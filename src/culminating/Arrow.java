/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * The Arrow class handles the behavior of an arrow object, which can be rotated,
 * fired, and used to interact with targets (e.g., suns). It includes collision
 * detection, movement logic, and drawing functionality for a game built with Processing.
 * 
 * @author jojox
 * @version 1.0 1st version of Arrow.java for culminating assignment
 * @date 06/15/2025
 */

package culminating;

import processing.core.PApplet;
import processing.core.PImage;

public class Arrow {
    private PApplet app;        // Reference to PApplet for drawing and dimensions
    private PImage image;       // Arrow image

    private float x, y;         // Current position of the arrow
    private float angle;        // Current rotation angle of the arrow
    private boolean flying;     // True if arrow has been fired and is in motion
    private float speed = 15;   // Arrow movement speed

    private float baseX = 110;  // Default starting X position
    private float baseY = 295;  // Default starting Y position
    private float baseAngle;    // Default angle before shooting

    /**
     * Constructor to create an arrow with a custom image.
     * 
     * @param app  the PApplet instance
     * @param path the file path to the arrow image
     */
    public Arrow(PApplet app, String path) {
        this.app = app;
        this.image = app.loadImage(path);    // Load custom arrow image
        this.image.resize(120, 15);          // Resize for consistency
        reset();                             // Set arrow to default state
    }

    /**
     * Constructor that loads a default arrow image if no path is specified.
     * 
     * @param app the PApplet instance
     */
    public Arrow(PApplet app) {
        this.app = app;
        this.image = app.loadImage("defaultArrow.png"); // Load default image
        this.image.resize(120, 15);
        reset();
    }

    /**
     * Rotates and moves the arrow before being shot.
     * 
     * @param deltaAngle the change in angle to apply
     * @param dx         the change in X position
     * @param dy         the change in Y position
     */
    public void rotateAndTranslate(float deltaAngle, float dx, float dy) {
        if (!flying) {  // Only allow adjustment before shooting
            float newAngle = baseAngle + deltaAngle;

            // Constrain angle between -20 and +8 degrees
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

    /**
     * Fires the arrow, locking its current position and angle.
     */
    public void shoot() {
        if (!flying) {
            x = baseX;
            y = baseY;
            angle = baseAngle;
            flying = true;
        }
    }

    /**
     * Updates the arrow's position based on angle and speed if it's flying.
     * Resets the arrow if it goes off-screen.
     */
    public void update() {
        if (flying) {
            float radians = PApplet.radians(angle);
            x += speed * PApplet.cos(radians);
            y += speed * PApplet.sin(radians);

            // Reset arrow if it exits screen bounds
            if (x < -image.width || x > app.width + image.width ||
                y < -image.height || y > app.height + image.height) {
                reset();
            }
        }
    }

    /**
     * Resets the arrow to its initial position and stops motion.
     */
    public void reset() {
        x = baseX;
        y = baseY;
        angle = baseAngle;
        flying = false;
    }

    /**
     * Displays the arrow image with rotation applied at its position.
     */
    public void display() {
        app.pushMatrix();
        app.translate(x, y);                    // Move to current position
        app.rotate(PApplet.radians(angle));     // Rotate the arrow
        app.imageMode(PApplet.CENTER);          // Draw image from center
        app.image(image, 0, 7);                 // Offset image vertically slightly
        app.popMatrix();
    }

    /**
     * Gets the base X position of the arrow.
     * 
     * @return X coordinate before being shot
     */
    public float getX() {
        return baseX;
    }

    /**
     * Gets the base Y position of the arrow.
     * 
     * @return Y coordinate before being shot
     */
    public float getY() {
        return baseY;
    }

    /**
     * Gets the angle at which the arrow is aimed.
     * 
     * @return angle in degrees
     */
    public float getAngle() {
        return baseAngle;
    }

    /**
     * Checks if the arrow is currently flying (in motion).
     * 
     * @return true if flying, false otherwise
     */
    public boolean isFlying() {
        return flying;
    }

    /**
     * Determines if the arrow collides with a circular target (e.g., miniSun or bossSun).
     * 
     * @param sunX the X coordinate of the target
     * @param sunY the Y coordinate of the target
     * @return true if arrow tip is within 35 pixels of the target
     */
    public boolean isColliding(float sunX, float sunY) {
        float radians = PApplet.radians(angle);
        float tipX = x + (image.width / 2f) * PApplet.cos(radians); // Calculate arrow tip X
        float tipY = y + (image.width / 2f) * PApplet.sin(radians); // Calculate arrow tip Y
        float distance = PApplet.dist(tipX, tipY, sunX, sunY);      // Distance from sun center
        return distance < 35;                                       // Return true if within hit range
    }
}