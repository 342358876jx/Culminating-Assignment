/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * This is the Sun class, representing the rotating sun targets in the game.
 * Each Sun has an image and a position and can be displayed or checked for clicks (or collisions).
 * @author jojox
 * @version 1.0 1st version of Sun.java for culminating assignment
 * @date 06/15/2025
 */

    package culminating;

    // Import Processing libraries for graphics and images
    import processing.core.PApplet;
    import processing.core.PImage;

    // Define the Sun class
    public class Sun {
        // Reference to the main PApplet instance (Processing environment)
        protected PApplet p;

        // Image representing the sun
        protected PImage image;

        // Position coordinates (center) for drawing the sun
        protected float x, y;

        /**
         * Constructor to create a Sun object.
         * @param p The PApplet reference (used to access Processing functions).
         * @param path The path to the sun image file.
         */
        public Sun(PApplet p, String path) {
            this.p = p;
            // Load the image from the given path
            this.image = p.loadImage(path);

            // Resize the image to 70x70 pixels if successfully loaded
            if (this.image != null) {
                this.image.resize(70, 70); // Avoids null pointer exception if image failed to load
            }
        }

        /**
         * Getter method to return the sun's image.
         * @return The PImage representing the sun.
         */
        public PImage getImage() {
            return image;
        }

        /**
         * Sets the (x, y) position of the sun.
         * @param x X-coordinate of the sun.
         * @param y Y-coordinate of the sun.
         */
        public void setPosition(float x, float y) {
            this.x = x;
            this.y = y;
        }

        /**
         * Determines if a given mouse click is within the bounds of the sun image.
         * Used for detecting interactions.
         * @param mouseX The x-coordinate of the mouse click.
         * @param mouseY The y-coordinate of the mouse click.
         * @return true if the click is inside the sun image; false otherwise.
         */
        public boolean isClicked(float mouseX, float mouseY) {
            if (image == null) return false; // Safety check

            // Get half the width and height to calculate bounds from center
            float halfWidth = image.width / 2f;
            float halfHeight = image.height / 2f;

            // Check if the click is within the image bounds
            return mouseX >= x - halfWidth && mouseX <= x + halfWidth &&
                   mouseY >= y - halfHeight && mouseY <= y + halfHeight;
        }

        /**
         * Draws the sun image at its (x, y) position.
         */
        public void display() {
            if (image != null) {
                p.imageMode(PApplet.CENTER);  // Draw image from its center point
                p.image(image, x, y);         // Display the sun image at (x, y)
            }
        }
    }
