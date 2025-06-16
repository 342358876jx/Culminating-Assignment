/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package culminating;

import processing.core.PApplet;
import java.util.Random;

/**
 * MiniSun represents a small sun object that moves randomly or toward a target on the screen.
 * It is used during boss battles and practice stages as a target for the player to click.
 * Each MiniSun has a position, velocity, a timer to track its lifespan, and optional target movement.
 * 
 * This class inherits from Sun and overrides some behaviors for movement and display.
 * 
 * @author jojox
 * @version 1.0 1st version of MiniSun.java for culminating assignment
 * @date 06/16/2025
 */
public class MiniSun extends Sun {
    
    /** Horizontal velocity for random movement */
    private float vx, vy;

    /** Timer to track how long the MiniSun has existed (in frames) */
    int timer;

    /** X-coordinate of the target position (if moving toward target) */
    private float targetX;

    /** Y-coordinate of the target position (if moving toward target) */
    private float targetY;

    /** Speed of movement when moving toward target */
    private float speed = 3;

    /** Flag indicating whether MiniSun moves toward a target or randomly */
    private boolean movesToTarget;

    /**
     * Constructor to create a MiniSun with random movement near a center position.
     * 
     * @param p The PApplet reference for Processing drawing and functions.
     * @param centerX The X coordinate near which the MiniSun will spawn.
     * @param centerY The Y coordinate near which the MiniSun will spawn.
     */
    public MiniSun(PApplet p, float centerX, float centerY) {
        super(p, "path/to/minisun/image.png");  // Load a smaller sun image or placeholder
        
        Random rand = new Random();

        // Set initial position randomly offset from the center point (within ±50 pixels)
        this.x = centerX + rand.nextInt(100) - 50;
        this.y = centerY + rand.nextInt(100) - 50;

        // Initialize random velocities between -1 and 1 for smooth drifting
        this.vx = rand.nextFloat() * 2 - 1;
        this.vy = rand.nextFloat() * 2 - 1;

        this.timer = 0;
        this.movesToTarget = false;  // Will move randomly
    }

    /**
     * Constructor to create a MiniSun that moves toward a specified target position.
     * 
     * @param p The PApplet reference for Processing drawing and functions.
     * @param startX The starting X position of the MiniSun.
     * @param startY The starting Y position of the MiniSun.
     * @param targetX The X coordinate of the target to move toward.
     * @param targetY The Y coordinate of the target to move toward.
     */
    public MiniSun(PApplet p, float startX, float startY, float targetX, float targetY) {
        super(p, "path/to/minisun/image.png");  // Load image for the MiniSun
        
        // Initialize starting position
        this.x = startX;
        this.y = startY;

        // Set target position
        this.targetX = targetX;
        this.targetY = targetY;

        this.timer = 0;
        this.movesToTarget = true;   // Will move toward target
    }

    /**
     * Updates the MiniSun's position and timer. Should be called once per frame.
     * Moves either randomly or smoothly toward the target position.
     */
    public void update() {
        if (movesToTarget) {
            // Calculate vector from current position to target
            float dx = targetX - x;
            float dy = targetY - y;
            float distance = PApplet.sqrt(dx * dx + dy * dy);

            if (distance > speed) {
                // Normalize direction and move towards target at fixed speed
                x += (dx / distance) * speed;
                y += (dy / distance) * speed;
            } else {
                // Snap to target if close enough
                x = targetX;
                y = targetY;
            }
        } else {
            // Update position by velocity for random drifting movement
            x += vx;
            y += vy;
        }
        timer++;  // Increment lifespan timer (in frames)
    }

    /**
     * Draws the MiniSun on screen.
     * Uses the image from the Sun superclass if available; otherwise, draws a colored circle.
     */
    @Override
    public void display() {
        if (image != null) {
            super.display();  // Use image drawing from Sun class
        } else {
            // Draw a simple yellow-orange circle if no image available
            p.fill(255, 200, 0);
            p.noStroke();
            p.ellipse(x, y, 30, 30);
        }
    }

    /**
     * Checks if the MiniSun has been hit (clicked) by the player.
     * 
     * @param mx The X coordinate of the mouse click.
     * @param my The Y coordinate of the mouse click.
     * @return true if the click is within 20 pixels of the MiniSun's center.
     */
    public boolean isHit(float mx, float my) {
        return PApplet.dist(mx, my, x, y) < 20;
    }

    /**
     * Checks if the MiniSun has reached its target position.
     * Only applicable if movesToTarget is true.
     * 
     * @return true if MiniSun is within 10 pixels of the target position; false otherwise.
     */
    public boolean hitsTarget() {
        if (!movesToTarget) return false;
        return PApplet.dist(x, y, targetX, targetY) < 10;
    }
}