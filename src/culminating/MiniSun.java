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
import java.util.Random;

public class MiniSun {
    private PApplet p;  // Reference to PApplet for drawing and utility functions
    float x, y;         // Current position of the mini sun
    float vx, vy;       // Velocity components for movement in x and y directions
    int timer;          // Timer to track how long the mini sun has been active

    // Constructor initializes position near center with random velocity
    public MiniSun(PApplet p, float centerX, float centerY) {
        this.p = p;  // Store reference to PApplet
        Random rand = new Random();  // Create Random object for generating random values
        
        // Initialize x position within ±50 pixels around centerX
        x = centerX + rand.nextInt(100) - 50;
        // Initialize y position within ±50 pixels around centerY
        y = centerY + rand.nextInt(100) - 50;
        
        // Initialize horizontal velocity randomly between -1 and 1
        vx = rand.nextFloat() * 2 - 1;
        // Initialize vertical velocity randomly between -1 and 1
        vy = rand.nextFloat() * 2 - 1;
        
        timer = 0;  // Start timer at 0
    }

    // Update the position based on velocity and increment the timer
    public void update() {
        x += vx;   // Move horizontally by vx
        y += vy;   // Move vertically by vy
        timer++;   // Increment timer by 1 frame
    }

    // Draw the mini sun as a yellow-orange circle without stroke
    public void display() {
        p.fill(255, 200, 0);  // Set fill color to yellow-orange
        p.noStroke();         // Disable outline
        p.ellipse(x, y, 30, 30);  // Draw circle centered at (x,y) with diameter 30
    }

    // Check if a point (mx, my) hits the mini sun (within radius 20)
    public boolean isHit(float mx, float my) {
        // Calculate distance between point and mini sun center, return true if less than 20
        return PApplet.dist(mx, my, x, y) < 20;
    }
}