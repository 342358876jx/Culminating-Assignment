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

    private boolean showingPractice2 = false;  // NEW flag for Practice 2 stage

    private long transitionStartTime = 0;

    private int buttonX, buttonY, buttonW, buttonH;
    private int logoutX, logoutY, logoutW, logoutH;
    private int continueX, continueY, continueW, continueH;

    // Boss battle variables
    private boolean bossBattle = false;
    private boolean finalBossTriggered = false;
    private boolean bossDefeated = false;
    private boolean playerDefeated = false;

    private int bossHealth;
    private int playerHealth;

    private BossSun bossSun;
    private ArrayList<MiniSun> miniSuns = new ArrayList<>();
    private int miniSunLifespan = 3000; // milliseconds for mini suns lifespan

    // For practice2: count how many mini suns clicked
    private int miniSunsClickedPractice2 = 0;
    private int miniSunsNeededPractice2 = 3;

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

        bossSun = new BossSun(this, "images/sun6.png", 750, height / 2, height / 2 - 100, height / 2 + 100);
        bossHealth = 100;
        playerHealth = 100;

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

        miniSunsClickedPractice2 = 0;  // Reset for practice2
        showingPractice2 = false;      // Reset flag

        loop();

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
            drawPractice1();
            return;
        }

        if (showingPractice2) {
            drawPractice2();
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

        // Endgame screen
        if (gameEnded || bossDefeated || playerDefeated) {
            drawGameOver();
            return;
        }

        // Boss battle stage 2
        if (bossBattle) {
            drawFinalBossBattle();
            return;
        }

        // Normal gameplay stage 1
        drawStage1();

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

        if ((successfulHits >= 3 && arrowCount >= 0 && currentSunIndex == 9) && !finalBossTriggered) {
            finalBossTriggered = true;
            bossBattle = true; // transition immediately next frame
            return;
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

    private void drawPractice2() {
        // Practice 2 screen: simulate boss sun + miniSuns; player clicks 3 miniSuns to complete

        background(175, 214, 255);

        bossSun.update();
        bossSun.display();

        // Spawn miniSuns periodically (up to 3 max)
        if (frameCount % 90 == 0 && miniSuns.size() < 3) {
            miniSuns.add(new MiniSun(this, bossSun.getX(), bossSun.getY()));
        }

        // Update and display miniSuns
        for (int i = miniSuns.size() - 1; i >= 0; i--) {
            MiniSun m = miniSuns.get(i);
            m.update();
            m.display();

            // Remove miniSuns if timer expired but no health penalty here in practice
            if (m.timer > miniSunLifespan) {
                miniSuns.remove(i);
            }
        }

        drawUpperBody();
        arrow.display();

        fill(0);
        textSize(24);
        textAlign(CENTER, CENTER);
        text("Practice Stage 2: Click 3 mini suns to continue!", width / 2f, 50);
        text("Mini Suns hit: " + miniSunsClickedPractice2 + " / " + miniSunsNeededPractice2, width / 2f, 80);

        // When player clicked 3 mini suns, finish practice and reset game or go to intro or next step
        if (miniSunsClickedPractice2 >= miniSunsNeededPractice2) {
            showingPractice2 = false;
            practiceCompleted = true;
            showingIntro = true;  // or transition to main game as you like
            miniSunsClickedPractice2 = 0;
            miniSuns.clear();
            noLoop();
        }
    }

    private void drawIntro() {
        fill(0);
        textAlign(CENTER, CENTER);
        textSize(30);
        text("In the age of myths, 10 suns rose into the sky, scorching the Earth.", width / 2f, height / 2f - 60);
        text("You are Hou Yi, the divine archer. Your mission is to restore balance.", width / 2f, height / 2f - 20);
        text("Press [SPACE] or [ENTER] to begin a short practice.", width / 2f, height / 2f + 40);
        text("Press [P] to start Practice Stage 2.", width / 2f, height / 2f + 80);  // hint for user
    }

    private void drawPractice1() {
        fill(0);
        textSize(20);
        text("Practice: Hit the sun to continue!", 170, 20);
        text("Press [SPACE] to launch the arrows", 185, 50);
        text("Press [UP] or [LEFT] to aim the arrow upwards.", 235, 80);
        text("Press [DOWN] or [RIGHT] to aim the arrow downwards.", 270, 110);

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

    private void drawFinalBossBattle() {
        background(175, 214, 255);

        bossSun.update();
        bossSun.display();

        for (int i = miniSuns.size() - 1; i >= 0; i--) {
            MiniSun m = miniSuns.get(i);
            m.update();
            m.display();

            if (m.timer > miniSunLifespan) {
                playerHealth -= 20;
                miniSuns.remove(i);
                if (playerHealth <= 0) {
                    playerDefeated = true;
                    checkAndSaveHighScore();
                    return;
                }
            }
        }

        if (frameCount % 90 == 0 && miniSuns.size() < 3) {
            miniSuns.add(new MiniSun(this, bossSun.getX(), bossSun.getY()));
        }

        drawUpperBody();
        arrow.display();

        fill(255, 0, 0);
        rect(50, 30, playerHealth * 2, 20);
        fill(0);
        text("Player", 50, 25);

        fill(255, 165, 0);
        rect(700, 30, bossHealth * 2, 20);
        fill(0);
        text("Boss", 700, 25);

        if (bossHealth <= 0) {
            bossDefeated = true;
            checkAndSaveHighScore();
        }
    }

    private void drawUpperBody() {
        pushMatrix();
        translate(arrow.getX(), arrow.getY());
        rotate(radians(arrow.getAngle()));
        imageMode(CENTER);
        image(upperBody, -10, 10);
        popMatrix();
    }

    private void drawGameOver() {
        background(0);
        fill(255);
        textAlign(CENTER);
        textSize(36);

        if (bossDefeated) text("YOU WIN!", width / 2, height / 2 - 60);
        else text("GAME OVER", width / 2, height / 2 - 60);

        text("Score: " + score, width / 2, height / 2);
        text("High Score: " + highScore, width / 2, height / 2 + 40);

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

    private void drawStage1() {
        // Could add more stage 1 specific drawing here if needed
    }

    public void keyPressed() {
        if (showingIntro) {
            if (key == ' ' || key == ENTER || keyCode == RETURN) {
                showingIntro = false;
                showingPractice = true;
                loop();
            }
            else if (key == 'P' || key == 'p') {
                // Start practice stage 2
                showingIntro = false;
                showingPractice2 = true;
                miniSunsClickedPractice2 = 0;
                miniSuns.clear();
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

        if (showingPractice2) {
            // No keyboard controls needed in practice 2 for now
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
        if (gameEnded || playerDefeated || bossDefeated) {
            if (mouseX >= buttonX && mouseX <= buttonX + buttonW &&
                mouseY >= buttonY && mouseY <= buttonY + buttonH) {
                setupGame();
            }

            if (mouseX >= logoutX && mouseX <= logoutX + logoutW &&
                mouseY >= logoutY && mouseY <= logoutY + logoutH) {
                checkAndSaveHighScore();
                exit();
            }
        }

        if (bossBattle && !bossDefeated && !playerDefeated) {
            for (int i = miniSuns.size() - 1; i >= 0; i--) {
                MiniSun m = miniSuns.get(i);
                if (m.isHit(mouseX, mouseY)) {
                    bossHealth -= 20;
                    miniSuns.remove(i);
                    break;
                }
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
}
