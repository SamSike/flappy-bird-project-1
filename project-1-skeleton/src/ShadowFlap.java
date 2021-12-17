import bagel.*;
import bagel.Font;
import bagel.Image;
import bagel.Window;
import bagel.util.Point;

import java.awt.*;

/**
 * Final Code for SWEN20003 Project 1, Semester 2, 2021
 *
 * @author Sameer Sikka - 1169800
 */
public class ShadowFlap extends AbstractGame {

    private final Image background;
    private final Image birdWingDown;
    private final Image birdWingUp;
    private final Font font;
    private final Image pipe;

    private final int SCORE_X_Y = 100;
    private final int FONT_SIZE = 48;
    private final double GRAVITY = 0.4;
    private final int SPEED = 5;
    private final double MAX_ACCELERATION = -10;
    private final double FLAP_REFRESH_RATE = 10;

    private double verticalAcceleration = 0;
    private final double x = 200;
    private double y = 350;

    private double pipeX = Window.getWidth();
    private final int PIPE_PAIR_DISTANCE = 768 + 168;
    private final double topPipeY = Window.getHeight() / -5.0;
    private final double bottomPipeY = topPipeY + PIPE_PAIR_DISTANCE;

    private bagel.util.Rectangle bird;
    private bagel.util.Rectangle topPipe;
    private bagel.util.Rectangle bottomPipe;

    private Point birdCenter;
    private Point bottomPipeCenter;
    private Point topPipeCenter;

    private int flag = 0;
    private int score = 0;
    private boolean isGameOver = false;

    /**
     * Constructor to initialise game window and images
     */
    public ShadowFlap() {
        super(1024, 768, "Flappy Bird");
        background = new Image("res/background.png");
        birdWingDown = new Image("res/birdWingDown.png");
        birdWingUp = new Image("res/birdWingUp.png");
        pipe = new Image("res/pipe.png");
        font = new Font("res/slkscr.ttf", FONT_SIZE);
        //window = new bagel.util.Rectangle(0, 0, 1024, 768);
    }

    /**
     * Start of Game
     */
    private void startGame(){
        font.drawString("Press Space to Start", Window.getWidth() / 5.0, Window.getHeight() / 2.0);
    }

    /**
     * Draw bird in correct wing position
     */
    private void drawBird(double y){
        if(flag % FLAP_REFRESH_RATE == 0)
            birdWingUp.draw(x, y);
        else
            birdWingDown.draw(x, y);
    }

    /**
     * Change in motion when bird flaps wings
     */
    private void fly(){
        verticalAcceleration = 6;
    }

    /**
     * Updates Bird and Pipe positions for every frame
     */
    private void updateFrame(){
        birdCenter = new Point(x, y);
        topPipeCenter = new Point(pipeX, topPipeY);
        bottomPipeCenter = new Point(pipeX, bottomPipeY);

        bird = birdWingDown.getBoundingBoxAt(birdCenter);
        topPipe = pipe.getBoundingBoxAt(topPipeCenter);
        bottomPipe = pipe.getBoundingBoxAt(bottomPipeCenter);

        // Draw Bird and Pipes
        drawBird(y);

        pipe.draw(pipeX, topPipeY);
        DrawOptions flip = new DrawOptions();
        pipe.draw(pipeX, bottomPipeY, flip.setRotation(Math.PI));
    }

    /**
     * Out of Bounds Detection of Bird and Pipes
     */
    private void checkEndGame(){
        if(y < 0.0 || y > Window.getHeight())
            loss();
        if(pipeX < 0.0) {
            score=1;
            victory();
        }
    }

    /**
     * Detects collision of Bird with pipes
     */
    private void collisionDetection(){
        if(bird.intersects(topPipe) || bird.intersects(bottomPipe))
            loss();
    }

    /**
     * Loss Screen with Final Score
     */
    private void loss(){
        isGameOver = true;
        font.drawString("GAME OVER", font.getWidth("GAME OVER")*1.2,Window.getHeight() / 2.0);
        font.drawString("FINAL SCORE: " + score, font.getWidth("FINAL SCORE: " + score)*0.75, 75 + Window.getHeight() / 2.0);
    }

    /**
     * Victory Screen with Final Score
     */
    private void victory(){
        isGameOver = true;
        font.drawString("CONGRATULATIONS!", font.getWidth("CONGRATULATIONS!")*0.5,Window.getHeight() / 2.0);
        font.drawString("FINAL SCORE: " + score, font.getWidth("FINAL SCORE: " + score)*0.75, 75 + Window.getHeight() / 2.0);
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowFlap game = new ShadowFlap();
        game.run();
    }

    /**
     * Performs a state update.
     * allows the game to exit when the escape key is pressed.
     */
    @Override
    public void update(Input input) {

        background.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);
        if(input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }

        if(flag == 0 && input.isUp(Keys.SPACE)) {
            startGame();
            return;
        }

        if(!isGameOver) {

            font.drawString("Score: " + score, SCORE_X_Y, SCORE_X_Y);
            flag++;
            if (input.isDown(Keys.SPACE)) {
                fly();
            }

            // Update Bird and Pipes
            verticalAcceleration = (verticalAcceleration < MAX_ACCELERATION) ? MAX_ACCELERATION : verticalAcceleration - GRAVITY;
            y -= verticalAcceleration;
            updateFrame();
            pipeX -= SPEED;
        }
        // Check for End of Game
        collisionDetection();
        checkEndGame();


    }

}
