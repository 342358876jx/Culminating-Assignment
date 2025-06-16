/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * The BossSun class extends the Sun class to represent a larger enemy sun in the game.
 * It moves vertically between set boundaries and has health that can be reduced
 * when the player attacks. This is used in both practice and boss battle stages.
 * 
 * @author jojox
 * @version 1.0 1st version of BossSun.java for culminating assignment
 * @date 06/15/2025
 */

package culminating;

import processing.core.PApplet;

public class BossSun extends Sun {

    // Vertical movement limits for the boss sun
    private float upperBound, lowerBound;

    // Speed at which the boss sun moves up/down
    private float speed = 2;

    // Health points of the boss sun
    private int health = 100;

    /**
     * Constructs a BossSun object with position, movement boundaries, and image path.
     * 
     * @param p the PApplet instance used for drawing
     * @param imagePath the file path of the boss sun image
     * @param x the initial horizontal position of the boss sun
     * @param y the initial vertical position of the boss sun
     * @param upperBound the top limit for vertical movement
     * @param lowerBound the bottom limit for vertical movement
     */
    public BossSun(PApplet p, String imagePath, float x, float y, float upperBound, float lowerBound) {
        super(p, imagePath);              // Call parent constructor to load image
        this.x = x;                       // Set X position
        this.y = y;                       // Set Y position
        this.upperBound = upperBound;     // Set top vertical bound
        this.lowerBound = lowerBound;     // Set bottom vertical bound
        this.image.resize(160, 160);      // Enlarge boss sun image to make it more prominent
    }

    /**
     * Updates the position of the boss sun.
     * Moves vertically and reverses direction when hitting movement bounds.
     */
    public void update() {
        y += speed; // Apply vertical movement

        // If the boss reaches top or bottom bound, reverse direction
        if (y > lowerBound || y < upperBound) {
            speed *= -1;
        }
    }

    /**
     * Displays the boss sun image on the screen at its current location.
     */
    public void display() {
        p.imageMode(PApplet.CENTER); // Center the image around (x, y)
        p.image(image, x, y);        // Draw image
    }

    /**
     * Applies damage to the boss sun, reducing its health.
     * 
     * @param damage the amount of damage to inflict
     */
    public void takeDamage(int damage) {
        health -= damage;
        if (health < 0) health = 0;  // Prevent health from going negative
    }

    /**
     * Returns the current health of the boss sun.
     * 
     * @return health value
     */
    public int getHealth() {
        return health;
    }

    /**
     * Gets the current X position of the boss sun.
     * 
     * @return X coordinate
     */
    public float getX() {
        return x;
    }

    /**
     * Gets the current Y position of the boss sun.
     * 
     * @return Y coordinate
     */
    public float getY() {
        return y;
    }
}