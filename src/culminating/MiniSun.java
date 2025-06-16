/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * MiniSun represents a small sun object that moves randomly or toward a target on the screen.
 * It is used during boss battles and practice stages as a target for the player to click.
 * Each MiniSun has a position, velocity, timer to track its lifespan, and optional target movement.
 *
 * @author jojox
 * @version 1.0 1st version of MiniSun.java for culminating assignment
 * @date 06/16/2025
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

    // Target position for directed movement (optional)
    float targetX, targetY;

    // Speed for moving toward target
    float speed = 3;

    // Flag to indicate if this MiniSun moves toward a target
    boolean movesToTarget;

    /**
     * Constructor for random movement MiniSun near given center.
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
        movesToTarget = false;  // This MiniSun moves randomly
    }

    /**
     * Constructor for MiniSun that moves toward a specific target position.
     * 
     * @param p the PApplet object
     * @param startX starting x position
     * @param startY starting y position
     * @param targetX target x position to move toward
     * @param targetY target y position to move toward
     */
    public MiniSun(PApplet p, float startX, float startY, float targetX, float targetY) {
        this.p = p;
        this.x = startX;
        this.y = startY;
        this.targetX = targetX;
        this.targetY = targetY;
        this.timer = 0;
        this.speed = 3;
        this.movesToTarget = true;  // This MiniSun moves toward a target
    }

    /**
     * Updates the MiniSun's position and timer.
     * Should be called once per frame.
     */
    public void update() {
        if (movesToTarget) {
            // Move toward target position
            float dx = targetX - x;
            float dy = targetY - y;
            float distance = PApplet.sqrt(dx * dx + dy * dy);

            if (distance > speed) {
                x += (dx / distance) * speed;
                y += (dy / distance) * speed;
            } else {
                // Snap to target if close enough
                x = targetX;
                y = targetY;
            }
        } else {
            // Random movement update
            x += vx;
            y += vy;
        }
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
     * Checks if MiniSun has reached (hit) its target position.
     * Only valid if movesToTarget == true.
     * 
     * @return true if MiniSun is close enough to the target
     */
    public boolean hitsTarget() {
        if (!movesToTarget) return false;
        return PApplet.dist(x, y, targetX, targetY) < 10;
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