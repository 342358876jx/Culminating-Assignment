/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * MySketch class for culminating assignment game.
 * Implements the game logic for the Sun shooting game using Processing.
 * 
 * @author jojox
 * @version 1.0 1st version of MySketch.java for culminating assignment
 * @date 06/15/2025
 */
package culminating;

import processing.core.PApplet;
import processing.core.PImage;
import java.io.*;
import java.util.*;

public class MySketch extends PApplet {

    // Array of Sun objects representing the targets
    private Sun[] suns = new Sun[10];
    // Array of angles for each sun's position (used for rotation around center)
    private float[] angles = new float[10];
    // Speed of rotation for each sun
    private float[] sunSpeeds = new float[10];
    // Scores awarded for hitting each sun
    private int[] sunScores = new int[10];
    // Current index of the sun being targeted
    private int currentSunIndex = 0;
    public static int totalSunsCreated = 0; // static of suns created initially

    // Player's arrow object for aiming and shooting
    private Arrow arrow;
    // Images for character's upper and lower body (for visual effects)
    private PImage upperBody;
    private PImage lowerBody;

    // Game state variables
    private int arrowCount = 15;       // Total arrows player starts with
    private int successfulHits = 0;    // Count of suns successfully hit
    private int score = 0;             // Current score
    private int highScore = 0;         // Highest score saved from previous runs
    private boolean gameEnded = false; // Flag for game over state

    // Stage and screen flags for controlling game flow
    private boolean stageOneCompleted = false;
    private boolean waitingForStageTwo = false;

    private boolean showingIntro = true;       // Whether intro screen is shown
    private boolean showingPractice = false;   // Whether practice stage 1 is active
    private boolean practiceCompleted = false;
    private boolean transitioningToGame = false;  // Transition animation flag

    private boolean showingPractice2 = false;  // NEW flag for Practice 2 stage

    private long transitionStartTime = 0;  // Time tracking for transitions

    // Coordinates and dimensions for various UI buttons
    private int buttonX, buttonY, buttonW, buttonH;
    private int logoutX, logoutY, logoutW, logoutH;
    private int continueX, continueY, continueW, continueH;

    // Variables related to boss battle stage
    private boolean bossBattle = false;
    private boolean finalBossTriggered = false;
    private boolean bossDefeated = false;
    private boolean playerDefeated = false;

    private int bossHealth;
    private int playerHealth;

    // BossSun object representing the boss enemy
    private BossSun bossSun;
    // Collection of MiniSun objects for Practice 2 mini-game
    private ArrayList<MiniSun> miniSuns = new ArrayList<>();
    private int miniSunLifespan = 3000; // lifespan of mini suns in milliseconds

    // For Practice 2: track how many mini suns have been clicked
    private int miniSunsClickedPractice2 = 0;
    private int miniSunsNeededPractice2 = 3;

    // Setup the canvas size for the Processing sketch
    public void settings() {
        size(1000, 600);
    }

    // Setup method initializes game objects and loads resources
    public void setup() {
        textFont(createFont("Arial", 16));
        setupGame();
        loadHighScore();  // Load saved high score from file
        noLoop();         // Stop continuous drawing until needed
    }

    // Initialize/reset game variables and objects to start or restart the game
    public void setupGame() {
        background(255);

        // Create arrow object with image path
        arrow = new Arrow(this, "images/arrow.png");

        // Initialize suns, angles, speeds, and scores for each sun target
        for (int i = 0; i < suns.length; i++) {
            suns[i] = new Sun(this, "images/sun" + ((i % 6) + 1) + ".png");
            angles[i] = 0;
            sunSpeeds[i] = 0.5f + 0.2f * i;  // Different speed for each sun
            sunScores[i] = 10 * (i + 1);      // Increasing score per sun index
        }

        // Load and resize character body images
        upperBody = loadImage("images/upperbody.png");
        upperBody.resize(200, 200);
        lowerBody = loadImage("images/lowerbody.png");
        lowerBody.resize(170, 200);

        // Initialize boss sun and health values
        bossSun = new BossSun(this, "images/sun6.png", 750, height / 2, height / 2 - 100, height / 2 + 100);
        bossHealth = 100;
        playerHealth = 100;

        // Reset game state counters and flags
        arrowCount = 15;
        successfulHits = 0;
        currentSunIndex = 0;
        score = 0;
        gameEnded = false;
        stageOneCompleted = false;
        waitingForStageTwo = false;
        bossBattle = false;
        finalBossTriggered = false;
        bossDefeated = false;
        playerDefeated = false;
        miniSuns.clear();

        // Reset Practice 2 variables
        miniSunsClickedPractice2 = 0;
        showingPractice2 = false;

        loop();  // Resume the draw loop after setup

        // Reset UI and stage flags
        showingIntro = true;
        showingPractice = false;
        practiceCompleted = false;
        transitioningToGame = false;

        // Setup button dimensions and positions centered horizontally
        buttonW = 200;
        buttonH = 50;
        buttonX = width / 2 - buttonW / 2;
        buttonY = height / 2 + 100;

        logoutW = 200;
        logoutH = 50;
        logoutX = width / 2 - logoutW / 2;
        logoutY = height / 2 + 170;

        continueW = 200;
        continueH = 50;
        continueX = width / 2 - continueW / 2;
        continueY = height / 2 + 100;
    }

    // The main draw loop for rendering the game frame-by-frame
    public void draw() {
        background(175, 214, 255); // Light blue background

        if (showingIntro) {
            drawIntro();  // Display intro text and instructions
            return;
        }

        if (showingPractice) {
            drawPractice1();  // Draw Practice Stage 1
            return;
        }

        if (showingPractice2) {
            drawPractice2();  // Draw Practice Stage 2
            return;
        }

        if (transitioningToGame) {
            // Show "Get Ready..." text for 3 seconds before starting main game
            if (millis() - transitionStartTime > 3000) {
                transitioningToGame = false;
                loop();
            } else {
                fill(0);
                textSize(32);
                textAlign(CENTER, CENTER);
                text("Get Ready...", width / 2f, height / 2f);
                return;
            }
        }

        // Show game over screen if game or boss battle ended
        if (gameEnded || bossDefeated || playerDefeated) {
            drawGameOver();
            return;
        }

        // Show boss battle screen if boss battle is active
        if (bossBattle) {
            drawFinalBossBattle();
            return;
        }

        // Display game stats
        fill(0);
        textSize(20);
        text("Arrows left: " + arrowCount, 82, 30);
        text("Suns Hit: " + successfulHits + "/10", 78, 60);
        text("Score: " + score, 55, 90);

        if (gameEnded) {
            drawGameOver();
            return;
        }

        if (waitingForStageTwo) {
            drawStagePassed();  // Show stage completion message
            return;
        }

        // End the game if no arrows remain or all suns have been targeted
        if (arrowCount <= 0 || currentSunIndex == 10) {
            gameEnded = true;
            checkAndSaveHighScore();
            return;
        }

        // Trigger final boss battle after 3 successful hits and reaching the 9th sun
        if ((successfulHits >= 3 && arrowCount >= 0 && currentSunIndex == 9) && !finalBossTriggered) {
            finalBossTriggered = true;
            bossBattle = true;  // Start boss battle on next frame
            return;
        }

        // Calculate sun's current position in a circular path
        float radius = 500;
        float centerX = width / 2f;
        float centerY = height / 2f;
        float sunAngle = angles[currentSunIndex];
        float sunX = centerX - cos(radians(sunAngle)) * radius;
        float sunY = centerY - sin(radians(sunAngle)) * radius + 250;

        pushMatrix();
        translate(sunX, sunY);
        rotate(radians(sunAngle));
        imageMode(CENTER);
        image(suns[currentSunIndex].getImage(), 0, 0);
        popMatrix();

        // Update sun's angle for rotation movement
        angles[currentSunIndex] += sunSpeeds[currentSunIndex];

        // Move to next sun if the current sun rotates beyond 180 degrees
        if (angles[currentSunIndex] >= 180) {
            currentSunIndex++;
            arrow.reset();
        }

        // Draw UI and character body
        fill(222, 200, 159);
        rect(54, 325, 29, 30);

        drawUpperBody();

        // Update and draw arrow
        arrow.update();
        arrow.display();

        // Check collision between flying arrow and sun
        if (arrow.isFlying() && arrow.isColliding(sunX, sunY)) {
            arrow.reset();
            score += sunScores[currentSunIndex];
            successfulHits++;
            currentSunIndex++;
        }

        // Draw lower part of the character on bottom of screen
        fill(51, 0, 0);
        rect(0, 400, 1000, 400);
        image(lowerBody, 78, 413);
    }
    
    // Draw the upper body of the character rotated to match arrow angle
    private void drawUpperBody() {
        pushMatrix();
        translate(arrow.getX(), arrow.getY());
        rotate(radians(arrow.getAngle()));
        imageMode(CENTER);
        image(upperBody, -10, 10);
        popMatrix();
    }

    // Draw the intro screen text with instructions
    private void drawIntro() {
        fill(0);
        textAlign(CENTER, CENTER);
        textSize(30);
        text("In the age of myths, 10 suns rose into the sky, scorching the Earth.", width / 2f, height / 2f - 60);
        text("You are Hou Yi, the divine archer. Your mission is to restore balance.", width / 2f, height / 2f - 20);
        text("Press [1] to start Practice Stage 1.", width / 2f, height / 2f + 40);
        text("Press [2] to start Practice Stage 2.", width / 2f, height / 2f + 80);
    }
    
    /**
     * Draws the practice stage 1 screen where the player aims and shoots an arrow at the sun.
     */
    private void drawPractice1() {
        fill(0);
        textSize(20);
        // Instructions to the player
        text("Practice: Hit the sun to continue!", 170, 20);
        text("Press [SPACE] to launch the arrows", 185, 50);
        text("Press [UP] or [LEFT] to aim the arrow upwards.", 235, 80);
        text("Press [DOWN] or [RIGHT] to aim the arrow downwards.", 272, 110);

        // Calculate sun position on a circular path with a large radius
        float radius = 500;
        float centerX = width / 2f;
        float centerY = height / 2f;
        float sunAngle = angles[0];  // Current angle of sun along path
        float sunX = centerX - cos(radians(sunAngle)) * radius;  // Sun's X coordinate
        float sunY = centerY - sin(radians(sunAngle)) * radius + 250;  // Sun's Y coordinate (offset by 250)

        // Draw the sun image rotated at the calculated position
        pushMatrix();
        translate(sunX, sunY);
        rotate(radians(sunAngle));
        imageMode(CENTER);
        image(suns[0].getImage(), 0, 0);
        popMatrix();

        // Increment the sun’s angle by its speed to animate movement
        angles[0] += sunSpeeds[0];

        // Reset sun position and arrow after sun completes half-circle (180 degrees)
        if (angles[0] >= 180) {
            angles[0] = 0;
            arrow.reset();
        }

        // Draw a rectangle background for the player area or UI element
        fill(222, 200, 159);
        rect(54, 325, 29, 30);

        drawUpperBody();    // Draw player's upper body graphics

        arrow.update();     // Update arrow position if flying
        arrow.display();    // Draw arrow on screen

        // Check if arrow hits the sun
        if (arrow.isFlying() && arrow.isColliding(sunX, sunY)) {
            arrow.reset();            // Reset arrow
            practiceCompleted = true; // Mark practice stage as complete
            showingPractice = false;  // Hide practice screen
            transitioningToGame = true;  // Start transition to main game
            transitionStartTime = millis(); // Record transition start time

            setupGame();              // Initialize main game variables and setup

            // These flags reinforce the transition state
            showingIntro = false;
            showingPractice = false;
            transitioningToGame = true;
            transitionStartTime = millis();
        }

        // Draw player area bottom background
        fill(51, 0, 0);
        rect(0, 400, 1000, 400);
        image(lowerBody, 78, 413);  // Draw player's lower body graphic
    }

    /**
     * Draws the final boss battle screen with boss sun, mini suns, player and boss health bars.
     */
    private void drawFinalBossBattle() {
        background(175, 214, 255);  // Light blue background for boss battle

        bossSun.update();   // Update boss sun's vertical movement
        bossSun.display();  // Draw the boss sun

        // Update and draw each mini sun; remove if lifespan exceeded
        for (int i = miniSuns.size() - 1; i >= 0; i--) {
            MiniSun m = miniSuns.get(i);
            m.update();
            m.display();

            if (m.timer > miniSunLifespan) {
                playerHealth -= 10;  // Penalize player health when mini sun expires
                miniSuns.remove(i);  // Remove expired mini sun

                if (playerHealth <= 0) {
                    playerDefeated = true;    // Mark player as defeated
                    checkAndSaveHighScore();  // Save high score if applicable
                    return;                   // End drawing to stop game
                }
            }
        }

        // Spawn new mini suns every 90 frames if less than 3 exist
        if (frameCount % 90 == 0 && miniSuns.size() < 3) {
            miniSuns.add(new MiniSun(this, bossSun.getX(), bossSun.getY()));
        }

        // Draw player area background behind the player sprite
        fill(222, 200, 159);
        rect(54, 325, 29, 30);

        drawUpperBody();     // Draw player's upper body graphic

        arrow.update();      // Update arrow position
        arrow.display();     // Draw arrow

        // Draw player health bar (red)
        fill(255, 0, 0);
        rect(50, 30, playerHealth * 2, 20);
        fill(0);
        text("Player", 50, 25);

        // Draw boss health bar (orange)
        fill(255, 165, 0);
        rect(700, 30, bossHealth * 2, 20);
        fill(0);
        text("Boss", 700, 25);

        // If boss defeated, mark and save score
        if (bossHealth <= 0) {
            bossDefeated = true;
            checkAndSaveHighScore();
        }

        // Draw player area bottom background and lower body image
        fill(51, 0, 0);
        rect(0, 400, 1000, 400);
        image(lowerBody, 78, 413);
    }
    
    /**
     * Draws the Practice 2 screen where the player fights the boss sun
     * by clicking mini suns before they reach the player's upper body.
     */
    private void drawPractice2() {
        fill(0);
        textSize(20);
        text("Practice: Click the mini suns to fight back the threats of the sun!", 315, 20);
        text("When paused Press [1]", 140, 50);

        // Update and display the boss sun
        bossSun.update();
        bossSun.display();

        // Spawn mini suns periodically (every 90 frames), max 3 on screen,
        // aiming roughly at the player's upper body position.
        if (frameCount % 90 == 0 && miniSuns.size() < 3) {
            float targetX = 68;  // Approximate upper body X
            float targetY = 325; // Approximate upper body Y
            miniSuns.add(new MiniSun(this, bossSun.getX(), bossSun.getY(), targetX, targetY));
        }

        // Loop backward through miniSuns to update, display, and check collisions or expiration
        for (int i = miniSuns.size() - 1; i >= 0; i--) {
            MiniSun m = miniSuns.get(i);
            m.update();
            m.display();

            if (m.hitsTarget()) {
                playerHealth -= 10;   // Player loses health when miniSun hits
                miniSuns.remove(i);   // Remove this miniSun
                continue;             // Skip rest of loop for this miniSun
            }

            // Remove miniSun if it times out without hitting target (no penalty in practice)
            if (m.timer > miniSunLifespan) {
                miniSuns.remove(i);
            }
        }

        // Draw player's upper body rectangle and additional details
        fill(222, 200, 159);
        rect(54, 325, 29, 30);
        drawUpperBody();

        // Update and draw the arrow (player's projectile)
        arrow.update();
        arrow.display();

        // Check if player has clicked enough miniSuns to complete practice 2
        if (miniSunsClickedPractice2 >= miniSunsNeededPractice2) {
            showingPractice2 = false;   // End practice 2 screen
            practiceCompleted = true;   // Mark practice completed
            showingIntro = true;        // Return to intro or next transition
            miniSunsClickedPractice2 = 0; // Reset click count
            miniSuns.clear();           // Clear remaining miniSuns
            noLoop();                   // Pause draw loop until next action
        }

        // Draw lower body background and image
        fill(51, 0, 0);
        rect(0, 400, 1000, 400);
        image(lowerBody, 78, 413);
    }

    /**
     * Handles mouse click interactions depending on game state.
     */
    public void mousePressed() {
        // If game ended or player/boss defeated, handle buttons for restart or logout
        if (gameEnded || playerDefeated || bossDefeated) {
            if (mouseX >= buttonX && mouseX <= buttonX + buttonW &&
                mouseY >= buttonY && mouseY <= buttonY + buttonH) {
                setupGame();  // Restart game on button click
            }

            if (mouseX >= logoutX && mouseX <= logoutX + logoutW &&
                mouseY >= logoutY && mouseY <= logoutY + logoutH) {
                checkAndSaveHighScore(); // Save score on logout
                exit();                  // Exit application
            }
        }

        // During practice 2, check if player clicked any miniSun to remove it and count clicks
        if (showingPractice2) {
            for (int i = miniSuns.size() - 1; i >= 0; i--) {
                MiniSun m = miniSuns.get(i);
                if (m.isHit(mouseX, mouseY)) {
                    miniSuns.remove(i);
                    miniSunsClickedPractice2++;  // Increment successful clicks
                    break;  // Only remove one per click
                }
            }
            return; // Skip further click handling during practice 2
        }

        // During boss battle, clicking miniSuns damages boss, increases score, and removes miniSun
        if (bossBattle && !bossDefeated && !playerDefeated) {
            for (int i = miniSuns.size() - 1; i >= 0; i--) {
                MiniSun m = miniSuns.get(i);
                if (m.isHit(mouseX, mouseY)) {
                    bossHealth -= 20;  // Damage boss health
                    miniSuns.remove(i);
                    score += 20;       // Increase player score
                    break;             // One miniSun removed per click
                }
            }
        }

        // Shoot arrow toward mouse if allowed (arrow not flying, game ongoing)
        if (!arrow.isFlying() && !gameEnded && !bossDefeated && !playerDefeated) {
            arrow.shootTowards(mouseX, mouseY);
        }

        // If waiting to start stage 2, check if Continue button clicked
        if (waitingForStageTwo) {
            if (mouseX >= continueX && mouseX <= continueX + continueW &&
                mouseY >= continueY && mouseY <= continueY + continueH) {
                waitingForStageTwo = false; // Proceed to next stage
                // Reset sun angles, increase speed and score bonuses for next stage
                for (int i = 0; i < suns.length; i++) {
                    angles[i] = 0;
                    sunSpeeds[i] += 0.1f;
                    sunScores[i] += 5;
                }
                currentSunIndex = 0;
                loop();  // Resume draw loop
            }
        }
    }

    /**
     * Draws the "Stage Passed" screen showing score and a continue button.
     */
    private void drawStagePassed() {
        fill(0);
        textSize(36);
        textAlign(CENTER, CENTER);
        text("Passed Stage 1!", width / 2f, height / 2f - 60);

        textSize(24);
        text("Your Score: " + score, width / 2f, height / 2f - 20);
        text("High Score: " + highScore, width / 2f, height / 2f + 10);
        text("Arrows Left: " + arrowCount, width / 2f, height / 2f + 40);

        // Draw the Continue button
        fill(0, 255, 100);
        rect(continueX, continueY, continueW, continueH, 10);

        fill(0);
        textSize(20);
        text("Continue", width / 2f, continueY + 30);
    }

    /**
     * Draws the Game Over screen with score and options to play again or logout.
     */
    private void drawGameOver() {
        background(0);
        fill(255);
        textAlign(CENTER);
        textSize(36);

        if (bossDefeated) {
            text("YOU WIN!", width / 2, height / 2 - 60);
        } else {
            text("GAME OVER", width / 2, height / 2 - 60);
        }

        text("Score: " + score, width / 2, height / 2);
        text("High Score: " + highScore, width / 2, height / 2 + 40);

        // Play Again button
        fill(100, 200, 255);
        rect(buttonX, buttonY, buttonW, buttonH, 10);
        fill(0);
        textSize(20);
        text("Play Again", width / 2f, buttonY + 30);

        // Logout button
        fill(255, 100, 100);
        rect(logoutX, logoutY, logoutW, logoutH, 10);
        fill(0);
        text("Logout", width / 2f, logoutY + 30);

        noLoop();  // Stop draw loop until next action
    }


    /**
    * Handles keyboard input during the game.
    * Controls transitions between screens and arrow movement/shooting.
    */
   public void keyPressed() {
       // If the intro screen is showing, handle input to start practice stages
       if (showingIntro) {
           if (key == '1') {  // Start Practice Stage 1
               showingIntro = false;
               showingPractice = true;
               loop();        // Resume draw loop for game action
           }
           else if (key == '2') {  // Start Practice Stage 2
               showingIntro = false;
               showingPractice2 = true;
               miniSunsClickedPractice2 = 0;  // Reset miniSuns click count
               miniSuns.clear();              // Clear any existing miniSuns
               loop();                       // Resume draw loop
           }
           return; // Skip remaining checks while intro is active
       }

       // Controls for Practice Stage 1
       if (showingPractice) {
           if (keyCode == LEFT || keyCode == UP) {
               // Rotate arrow slightly counter-clockwise and move left/up
               arrow.rotateAndTranslate(-1, -1, -1);
           } else if (keyCode == RIGHT || keyCode == DOWN) {
               // Rotate arrow slightly clockwise and move right/down
               arrow.rotateAndTranslate(1, 1, 1);
           } else if (key == ' ') {
               // Shoot the arrow
               arrow.shoot();
           }
           return; // Skip other input handling during practice 1
       }

       // Practice Stage 2 has no keyboard controls for now
       if (showingPractice2) {
           return;
       }

       // General game controls when arrow is not flying and game is active
       if (!arrow.isFlying() && !gameEnded && !waitingForStageTwo && !transitioningToGame) {
           if (keyCode == LEFT || keyCode == UP) {
               arrow.rotateAndTranslate(-1, -1, -1);
           } else if (keyCode == RIGHT || keyCode == DOWN) {
               arrow.rotateAndTranslate(1, 1, 1);
           } else if (key == ' ') {
               arrow.shoot();
               arrowCount--;  // Decrease available arrows count after shooting
           }
       }
   }

   /**
    * Loads the highest score saved from previous game sessions.
    * Reads "scores.txt" file line by line and finds the maximum score.
    */
   private void loadHighScore() {
       try {
           File file = new File("scores.txt");
           if (file.exists()) {
               Scanner scanner = new Scanner(file);
               while (scanner.hasNextLine()) {
                   int savedScore = Integer.parseInt(scanner.nextLine().trim());
                   if (savedScore > highScore) {
                       highScore = savedScore;  // Keep the max score found
                   }
               }
               scanner.close();
           }
       } catch (Exception e) {
           println("Failed to read scores.txt: " + e.getMessage());
       }
   }

   /**
    * Checks if the current score is higher than the saved high score.
    * If so, updates highScore and appends the current score to "scores.txt".
    */
   private void checkAndSaveHighScore() {
       if (score > highScore) {
           highScore = score;  // Update the high score
       }

       try (FileWriter fw = new FileWriter("scores.txt", true)) {
           fw.write(score + "\n");  // Append current score on a new line
       } catch (IOException e) {
           println("Failed to write to scores.txt: " + e.getMessage());
       }
   }
}