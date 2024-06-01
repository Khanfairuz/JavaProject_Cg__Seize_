package com.example.scene3;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Enemyquiz {
    private Pane root;
    private double SCENE_WIDTH=800;
    private double SCENE_HEIGHT=600;
    private double ENEMY_WIDTH=100;
    private double ENEMY_HEIGHT=100;
    private double QUIZ_WIDTH=30;
    private double QUIZ_HEIGHT=30;
    private boolean isDownKeyPressed;
    private boolean isJumping;
    public boolean isEnemyFinished=false;
    private ImageView heroView;
    private double HERO_HEIGHT;
    private double HERO_WIDTH;
    private int bonusPoints;
   // private Label bonusPointsLabel=new Label();
    private Image enemyImage;
    private Image quizImage;
    private List<ImageView> enemies = new ArrayList<>();
    private List<ImageView> quizzes = new ArrayList<>();

    private Timeline enemyTimeline;

    Random random=new Random();

    public Enemyquiz(Pane root, Hero hero, int bonusPoints) {
        this.root = root;
        this.isDownKeyPressed = hero.isDownKeyPressed;
        this.isJumping = hero.isJumping;
        this.heroView = hero.heroView;
        this.HERO_HEIGHT = hero.HERO_HEIGHT;
        this.HERO_WIDTH = hero.HERO_WIDTH;
        this.bonusPoints = bonusPoints;

        enemyImage = new Image(getClass().getResource("/Enemy/wow.png").toExternalForm());
        quizImage = new Image(getClass().getResource("/Gold/QUIZ.gif").toExternalForm());
    }

    public  int Cnt=0;

    public void generateEnemy() {

        // Define an offset value to position enemies above the road segments
        double yOffset = 300; // Adjust this value as needed
        double maxY = (SCENE_HEIGHT-50) - yOffset; // Max height for enemy generation

        // Create a timeline to continuously generate enemies
        enemyTimeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> { // Adjust the duration as needed
                    // Randomly select a Y position above all road segments with an offset
                    double enemyY = random.nextDouble() * maxY;

                    // Create the enemy ImageView
                    ImageView enemy = new ImageView(enemyImage);
                    enemy.setFitWidth(ENEMY_WIDTH);
                    enemy.setFitHeight(ENEMY_HEIGHT);

                    // Set the initial position of the enemy outside the right side of the window
                    enemy.setLayoutX(SCENE_WIDTH);
                    enemy.setLayoutY(enemyY);

                    // Add the enemy to the root and enemies list
                    root.getChildren().add(enemy);
                    enemies.add(enemy);

                    // Move the enemy from right to left
                    moveEnemy(enemy);
                })
        );
        Duration pauseDuration = Duration.seconds(3); // Adjust the pause duration as needed
        enemyTimeline.setDelay(pauseDuration);

        // Set the cycle count to INDEFINITE so the timeline continues indefinitely
        enemyTimeline.setCycleCount(Timeline.INDEFINITE);

        // Start the timeline
        enemyTimeline.play();
    }



    private void moveEnemy(ImageView enemy) {
        Cnt++;
        TranslateTransition enemyTransition = new TranslateTransition(Duration.seconds(5), enemy);

        // Calculate the distance to move the enemy across the screen
        double distance = SCENE_WIDTH + enemy.getBoundsInLocal().getWidth();

        // Move the enemy from right to left
        enemyTransition.setFromX(SCENE_WIDTH);
        enemyTransition.setToX(-distance);

        if(Cnt==1)
        {
            //System.out.println("???"+cnt);
            enemyTransition.stop();
            enemyTimeline.stop();
            isEnemyFinished=true;
        }
        // Remove the enemy from the root and enemies list when the transition finishes
        enemyTransition.setOnFinished(event -> {
            root.getChildren().remove(enemy);
            enemies.remove(enemy);
        });

        // Start the enemy transition
        enemyTransition.play();
    }

    public void checkEnemyCollisions(Boolean isDownKeyPressed, javafx.scene.text.Text bonusPointsLabel) {
        // Calculate the bounds for the lower quarter of the hero's image
        double heroLowerY = heroView.getLayoutY() + HERO_HEIGHT * 0;
        double heroLowerHeight = HERO_HEIGHT * 1;

       // System.out.println("chinsoooooooooooo");
        // Check if the down key is pressed
        if (isDownKeyPressed||((isJumping==true)&&isDownKeyPressed)) {
            // Iterate through each enemy

            for (ImageView enemy : new ArrayList<>(enemies)) {
                System.out.println("inside of check collision enemy");
                // Check if the lower quarter of the hero's image intersects with the enemy
                if (enemy.getBoundsInParent().intersects(heroView.getBoundsInParent())){//.getLayoutX(), heroLowerY, HERO_WIDTH, heroLowerHeight)) {
                    // Collision detected between hero and enemy
                    System.out.println("collide kkorse yay");
                    root.getChildren().remove(enemy); // Remove the enemy from the scene
                    enemies.remove(enemy); // Remove the enemy from the list
                    // Increment points and update points label
                    bonusPoints++;
                    bonusPointsLabel.setText("Bonus: " + bonusPoints);
                }
            }
        }
    }

    private void addQuizToRoads(Image quizImage, List<Rectangle> roadSegments) {
        for (Rectangle segment : roadSegments) {
            ImageView quiz = new ImageView(quizImage);
            quiz.setFitWidth(QUIZ_WIDTH);
            quiz.setFitHeight(QUIZ_HEIGHT);
            quiz.setLayoutX(segment.getLayoutX() + segment.getWidth() / 2 - QUIZ_WIDTH / 2);
            quiz.setLayoutY(segment.getLayoutY() + segment.getHeight() / 2 - QUIZ_HEIGHT / 2);
            root.getChildren().add(quiz);
            quizzes.add(quiz);
        }
    }
}
//eefe