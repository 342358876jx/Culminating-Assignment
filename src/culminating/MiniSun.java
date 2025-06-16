/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * MiniSun represents a small sun object that moves randomly on the screen.
 * It is used during boss battles and practice stages as a target for the player to click.
 * Each MiniSun has a position, velocity, and timer to track its lifespan.
 *
 * @author jojox
 * @version 1.0 1st version of MiniSun.java for culminating assignment
 * @date 06/15/2025
 */

package culminating;

import processing.core.PApplet;
import java.util.Random;

public class MiniSun {
    // Reference to the PApplet to draw and access Processing functions
    private PApplet p;

    // Position of the mini sun
    float x, y;

    // Velocity of the mini sun (random movement)
    float vx, vy;

    // Timer to track how long the sun has existed
    int timer;

    /**
     * Constructs a MiniSun object at a position near the given center.
     * Randomly assigns velocity and slight offset from the center.
     * 
     * @param p the PApplet object (typically "this" from main sketch)
     * @param centerX the x-coordinate near which the mini sun spawns
     * @param centerY the y-coordinate near which the mini sun spawns
     */
    public MiniSun(PApplet p, float centerX, float centerY) {
        this.p = p;

        Random rand = new Random();

        // Random offset near the center point
        x = centerX + rand.nextInt(100) - 50;
        y = centerY + rand.nextInt(100) - 50;

        // Random velocity between -1 and 1
        vx = rand.nextFloat() * 2 - 1;
        vy = rand.nextFloat() * 2 - 1;

        timer = 0;
    }

    /**
     * Updates the MiniSun's position and timer.
     * Should be called once per frame.
     */
    public void update() {
        x += vx;
        y += vy;
        timer++;
    }

    /**
     * Draws the MiniSun as a yellow-orange circle.
     */
    public void display() {
        p.fill(255, 200, 0);  // Sun-like color
        p.noStroke();
        p.ellipse(x, y, 30, 30);  // Draw circle with 30px diameter
    }

    /**
     * Checks whether the mini sun has been clicked by the player.
     * 
     * @param mx the mouse X position
     * @param my the mouse Y position
     * @return true if mouse click is within 20px of the sun’s center
     */
    public boolean isHit(float mx, float my) {
        return PApplet.dist(mx, my, x, y) < 20;
    }

    /**
     * Gets the current X position of the mini sun.
     * 
     * @return the X coordinate
     */
    public float getX() {
        return x;
    }

    /**
     * Gets the current Y position of the mini sun.
     * 
     * @return the Y coordinate
     */
    public float getY() {
        return y;
    }
}