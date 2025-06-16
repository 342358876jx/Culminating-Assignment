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
import java.io.*;
import java.util.*;

public class MySketch extends PApplet {
    private Sun[] suns = new Sun[10];
    private float[] angles = new float[10];
    private float[] sunSpeeds = new float[10];
    private int[] sunScores = new int[10];
    private int currentSunIndex = 0;
    private Arrow arrow;
    private PImage upperBody;
    private PImage lowerBody;

    private int arrowCount = 15;
    private int successfulHits = 0;
    private int score = 0;
    private int highScore = 0;
    private boolean gameEnded = false;

    private boolean stageOneCompleted = false;
    private boolean waitingForStageTwo = false;

    private boolean showingIntro = true;
    private boolean showingPractice = false;
    private boolean practiceCompleted = false;
    private boolean transitioningToGame = false;
    private long transitionStartTime = 0;

    private int buttonX, buttonY, buttonW, buttonH;
    private int logoutX, logoutY, logoutW, logoutH;
    private int continueX, continueY, continueW, continueH;

    public void settings() {
        size(1000, 600);
    }

    public void setup() {
        textFont(createFont("Arial", 16));
        setupGame();
        loadHighScore();
        noLoop();
    }

    public void setupGame() {
        background(255);
        arrow = new Arrow(this, "images/arrow.png");

        for (int i = 0; i < suns.length; i++) {
            suns[i] = new Sun(this, "images/sun" + ((i % 6) + 1) + ".png");
            angles[i] = 0;
            sunSpeeds[i] = 0.5f + 0.2f * i;
            sunScores[i] = 10 * (i + 1);
        }

        upperBody = loadImage("images/upperbody.png");
        upperBody.resize(200, 200);
        lowerBody = loadImage("images/lowerbody.png");
        lowerBody.resize(170, 200);

        arrowCount = 15;
        successfulHits = 0;
        currentSunIndex = 0;
        score = 0;
        gameEnded = false;
        stageOneCompleted = false;
        waitingForStageTwo = false;

        showingIntro = true;
        showingPractice = false;
        practiceCompleted = false;
        transitioningToGame = false;

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

    public void draw() {
        background(175, 214, 255);

        if (showingIntro) {
            drawIntro();
            return;
        }

        if (showingPractice) {
            drawPractice();
            return;
        }

        if (transitioningToGame) {
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

        fill(0);
        textSize(20);
        text("Arrows left: " + arrowCount, 40, 30);
        text("Suns Hit: " + successfulHits + "/10", 40, 60);
        text("Score: " + score, 40, 90);

        if (gameEnded) {
            drawGameOver();
            return;
        }

        if (waitingForStageTwo) {
            drawStagePassed();
            return;
        }

        if (arrowCount <= 0 || currentSunIndex == 10) {
            gameEnded = true;
            checkAndSaveHighScore();
            return;
        }

        if (currentSunIndex > suns.length && !stageOneCompleted) {
            if (arrowCount >= 0 && successfulHits >= 3) {
                stageOneCompleted = true;
                waitingForStageTwo = true;
                noLoop();
                return;
            } else {
                gameEnded = true;
                checkAndSaveHighScore();
                return;
            }
        }

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

        angles[currentSunIndex] += sunSpeeds[currentSunIndex];

        if (angles[currentSunIndex] >= 180) {
            currentSunIndex++;
            arrow.reset();
        }

        fill(222, 200, 159);
        rect(54, 325, 29, 30);

        drawUpperBody();
        arrow.update();
        arrow.display();

        if (arrow.isFlying() && arrow.isColliding(sunX, sunY)) {
            arrow.reset();
            score += sunScores[currentSunIndex];
            successfulHits++;
            currentSunIndex++;
        }

        fill(51, 0, 0);
        rect(0, 400, 1000, 400);
        image(lowerBody, 78, 413);
    }

    private void drawIntro() {
        fill(0);
        textAlign(CENTER, CENTER);
        textSize(30);
        text("In the age of myths, 10 suns rose into the sky, scorching the Earth.", width / 2f, height / 2f - 60);
        text("You are Hou Yi, the divine archer. Your mission is to restore balance.", width / 2f, height / 2f - 20);
        text("Press [SPACE] or [ENTER] to begin a short practice.", width / 2f, height / 2f + 40);
    }

    private void drawPractice() {
        fill(0);
        textSize(20);
        text("Practice: Hit the sun to continue!", 40, 30);

        float radius = 500;
        float centerX = width / 2f;
        float centerY = height / 2f;
        float sunAngle = angles[0];
        float sunX = centerX - cos(radians(sunAngle)) * radius;
        float sunY = centerY - sin(radians(sunAngle)) * radius + 250;

        pushMatrix();
        translate(sunX, sunY);
        rotate(radians(sunAngle));
        imageMode(CENTER);
        image(suns[0].getImage(), 0, 0);
        popMatrix();

        angles[0] += sunSpeeds[0];

        if (angles[0] >= 180) {
            angles[0] = 0;
            arrow.reset();
        }

        fill(222, 200, 159);
        rect(54, 325, 29, 30);
        
        drawUpperBody();
        arrow.update();
        arrow.display();

        if (arrow.isFlying() && arrow.isColliding(sunX, sunY)) {
            arrow.reset();
            practiceCompleted = true;
            showingPractice = false;
            transitioningToGame = true;
            transitionStartTime = millis();

            setupGame();
            showingIntro = false;
            showingPractice = false;
            transitioningToGame = true;
            transitionStartTime = millis();
        }

        fill(51, 0, 0);
        rect(0, 400, 1000, 400);
        image(lowerBody, 78, 413);
    }

    private void drawUpperBody() {
        pushMatrix();
        translate(arrow.getX(), arrow.getY());
        rotate(radians(arrow.getAngle()));
        imageMode(CENTER);
        image(upperBody, -10, 10);
        popMatrix();
    }

    public void keyPressed() {
        if (showingIntro) {
            if (key == ' ' || key == ENTER || keyCode == RETURN) {
                showingIntro = false;
                showingPractice = true;
                loop();
            }
            return;
        }

        if (showingPractice) {
            if (keyCode == LEFT || keyCode == UP) {
                arrow.rotateAndTranslate(-1, -1, -1);
            } else if (keyCode == RIGHT || keyCode == DOWN) {
                arrow.rotateAndTranslate(1, 1, 1);
            } else if (key == ' ') {
                arrow.shoot();
            }
            return;
        }

        if (!arrow.isFlying() && !gameEnded && !waitingForStageTwo && !transitioningToGame) {
            if (keyCode == LEFT || keyCode == UP) {
                arrow.rotateAndTranslate(-1, -1, -1);
            } else if (keyCode == RIGHT || keyCode == DOWN) {
                arrow.rotateAndTranslate(1, 1, 1);
            } else if (key == ' ') {
                arrow.shoot();
                arrowCount--;
            }
        }
    }

    public void mousePressed() {
        if (gameEnded) {
            if (mouseX >= buttonX && mouseX <= buttonX + buttonW &&
                mouseY >= buttonY && mouseY <= buttonY + buttonH) {
                setupGame();
                redraw();
            }

            if (mouseX >= logoutX && mouseX <= logoutX + logoutW &&
                mouseY >= logoutY && mouseY <= logoutY + logoutH) {
                checkAndSaveHighScore();
                exit();
            }
        }

        if (waitingForStageTwo) {
            if (mouseX >= continueX && mouseX <= continueX + continueW &&
                mouseY >= continueY && mouseY <= continueY + continueH) {
                waitingForStageTwo = false;
                for (int i = 0; i < suns.length; i++) {
                    angles[i] = 0;
                    sunSpeeds[i] += 0.1f;
                    sunScores[i] += 5;
                }
                currentSunIndex = 0;
                loop();
            }
        }
    }

    private void loadHighScore() {
        try {
            File file = new File("scores.txt");
            if (file.exists()) {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    int savedScore = Integer.parseInt(scanner.nextLine().trim());
                    if (savedScore > highScore) {
                        highScore = savedScore;
                    }
                }
                scanner.close();
            }
        } catch (Exception e) {
            println("Failed to read scores.txt: " + e.getMessage());
        }
    }

    private void checkAndSaveHighScore() {
        if (score > highScore) {
            highScore = score;
        }

        try (FileWriter fw = new FileWriter("scores.txt", true)) {
            fw.write(score + "\n");
        } catch (IOException e) {
            println("Failed to write to scores.txt: " + e.getMessage());
        }
    }

    private void drawGameOver() {
        fill(0);
        textSize(40);
        textAlign(CENTER, CENTER);
        text("Game Over", width / 2f, height / 2f - 40);
        textSize(30);
        text("Your Score: " + score, width / 2f, height / 2f + 10);
        text("High Score: " + highScore, width / 2f, height / 2f + 50);

        fill(100, 200, 255);
        rect(buttonX, buttonY, buttonW, buttonH, 10);
        fill(0);
        textSize(20);
        text("Play Again", width / 2f, buttonY + 30);

        fill(255, 100, 100);
        rect(logoutX, logoutY, logoutW, logoutH, 10);
        fill(0);
        text("Logout", width / 2f, logoutY + 30);
        noLoop();
    }

    private void drawStagePassed() {
        fill(0);
        textSize(36);
        textAlign(CENTER, CENTER);
        text("Passed Stage 1!", width / 2f, height / 2f - 60);
        textSize(24);
        text("Your Score: " + score, width / 2f, height / 2f - 20);
        text("High Score: " + highScore, width / 2f, height / 2f + 10);
        text("Arrows Left: " + arrowCount, width / 2f, height / 2f + 40);

        fill(0, 255, 100);
        rect(continueX, continueY, continueW, continueH, 10);
        fill(0);
        textSize(20);
        text("Continue", width / 2f, continueY + 30);
    }
}