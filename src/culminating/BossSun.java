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
    // Vertical movement limits for the boss sun
    private float upperBound, lowerBound;
    // Speed at which the boss sun moves vertically
    private float speed = 2;
    // Health points of the boss sun
    private int health = 100;

    // Constructor for BossSun with position and movement boundaries
    public BossSun(PApplet p, String imagePath, float x, float y, float upperBound, float lowerBound) {
        super(p, imagePath);  // Call superclass constructor to load image
        this.x = x;           // Set initial horizontal position
        this.y = y;           // Set initial vertical position
        this.upperBound = upperBound; // Set upper vertical boundary
        this.lowerBound = lowerBound; // Set lower vertical boundary
        this.image.resize(160, 160);  // Resize image to make the boss sun larger
    }

    // Update method to move the boss sun vertically and reverse direction at bounds
    public void update() {
        y += speed;                      // Move the sun vertically by speed
        if (y > lowerBound || y < upperBound) {  // Check if out of vertical bounds
            speed *= -1;                 // Reverse direction when hitting bounds
        }
    }

    // Display the boss sun image centered at (x, y)
    public void display() {
        p.imageMode(PApplet.CENTER);     // Set image mode to center
        p.image(image, x, y);            // Draw the image at current position
    }

    // Reduce health by a given damage amount
    public void takeDamage(int damage) {
        health -= damage;                // Subtract damage from health
        if (health < 0) health = 0;     // Clamp health to minimum 0
    }

    // Return the current health of the boss sun
    public int getHealth() {
        return health;
    }
}