package com.example.scene3;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
class Obstacles {
    private Rectangle shape; // Shape of the obstacle
    private double speed; // Movement speed of the obstacle

    public Obstacles(double minWidth, double maxWidth, double height, Color color, int speed) {
        double width = Math.random() * (maxWidth - minWidth) + minWidth; // Generate a random width within the range
        this.shape = new Rectangle(width, height, color);
        this.speed = speed;
    }



    public Rectangle getShape() {
        return shape;
    }

    public double getSpeed() {
        return speed;
    }
}

public class QuizObstacle {
    private static final int SCENE_WIDTH = 800;
    private static final int SCENE_HEIGHT = 600;
    private static final int SCROLL_SPEED = 10;
    private static final int ROAD_HEIGHT = 40;
    private static final int ROAD_Y = SCENE_HEIGHT - ROAD_HEIGHT;
    private static final int HERO_WIDTH = 200; // Adjust width as needed
    private static final int HERO_HEIGHT = 200; // Adjust height as needed
    private int damagePoints = 0; // Initialize damage points

    private List<Obstacles> obstacles = new ArrayList<>();
    private static final int DAMAGE_VALUE = 10;
    public boolean isQuizObstacleFinished=false;
    private int cnt=0;
    public static Timeline obstacleTimeline;
    public static Timeline obstacleTimelineLogo;
    public void generateObstacles(Pane root, List<Rectangle> roadSegments, List<Rectangle> parallelRoadSegments1, List<Rectangle> parallelRoadSegments2, Random random) {
        obstacleTimeline = new Timeline(
                new KeyFrame(Duration.seconds(3), event -> {
                    Rectangle segment;
                    int randomIndex = random.nextInt(roadSegments.size() + parallelRoadSegments1.size() + parallelRoadSegments2.size());
                    if (randomIndex < roadSegments.size()) {
                        segment = roadSegments.get(randomIndex);
                    } else if (randomIndex < roadSegments.size() + parallelRoadSegments1.size()) {
                        segment = parallelRoadSegments1.get(randomIndex - roadSegments.size());
                    } else {
                        segment = parallelRoadSegments2.get(randomIndex - roadSegments.size() - parallelRoadSegments1.size());
                    }
                    int minWidth = 30;
                    int maxWidth = 100;
                    double width = Math.random() * (maxWidth - minWidth) + minWidth;

                    Obstacles obstacle = new Obstacles(width,width, 60, Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256)), 40);
                    double offsetY = segment.getHeight() - 50; // Adjust as needed
                    root.getChildren().add(obstacle.getShape());
                    obstacles.add(obstacle);
                    moveObstacle(root, obstacle, segment, offsetY);
                })
        );
        Duration pauseDuration = Duration.seconds(3); // Adjust the pause duration as needed
        obstacleTimeline.setDelay(pauseDuration);
        // Set timeline properties...
        obstacleTimeline.setCycleCount(Timeline.INDEFINITE); // Make the timeline repeat indefinitely
        obstacleTimeline.play(); // Start the timeline
    }

    // Move obstacles across the screen
    // Move obstacles across the screen
    // Move obstacles across the screen with the specified offsetY
    private void moveObstacle(Pane root, Obstacles obstacle, Rectangle segment, double offsetY) {
        TranslateTransition obstacleTransition = new TranslateTransition(Duration.seconds(5), obstacle.getShape());
        obstacleTransition.setFromX(SCENE_WIDTH);
        obstacleTransition.setToX(-obstacle.getShape().getBoundsInLocal().getWidth());
        // Adjust the Y-coordinate to position the obstacle lower on the Y-axis
        obstacleTransition.setFromY(segment.getLayoutY() + offsetY);
        obstacleTransition.setToY(segment.getLayoutY() + offsetY);
        cnt++;

        if(cnt==8)
        {
            System.out.println("quizobs ");
            obstacleTransition.stop();
            obstacleTimeline.stop();
            isQuizObstacleFinished=true;
            System.out.println("choto quiz: "+ isQuizObstacleFinished);
        }
        obstacleTransition.setOnFinished(event -> root.getChildren().remove(obstacle.getShape()));
        obstacleTransition.play();
    }
    public void checkObstacleCollisions(Pane root, ImageView heroView,  javafx.scene.text.Text damagePointsLabel, boolean isTimerRunning) {
        // Calculate the bounds for the lower quarter of the hero's image
        double heroLowerY = heroView.getLayoutY() + HERO_HEIGHT * 0.90;
        double heroLowerHeight = HERO_HEIGHT * 0.10;

        if(!isTimerRunning && obstacleTimeline!=null)
        {
            System.out.println("Quiz obstacle timeline detected");
            obstacleTimeline.stop();
            for (Obstacles obstacle : new ArrayList<>(obstacles)) {
                root.getChildren().remove(obstacle.getShape());
                obstacles.remove(obstacle);
            }
        }
        // Iterate through each obstacle
        for (Obstacles obstacle : new ArrayList<>(obstacles)) {
            // Get the bounds of the obstacle
            Bounds obstacleBounds = obstacle.getShape().getBoundsInParent();

            // Calculate the bounds for the specific part of the hero's body you want to consider
            double heroPartX = heroView.getLayoutX(); // Adjust as needed

            // Check if the specific part of the hero's body intersects with the obstacle
            if (obstacleBounds.intersects(heroPartX, heroLowerY, HERO_WIDTH, heroLowerHeight)) {
                // Collision detected between hero and obstacle
                // Remove the obstacle from the scene and the list
                root.getChildren().remove(obstacle.getShape());
                obstacles.remove(obstacle);

                // Decrement health points and update health points label
                damagePoints -= DAMAGE_VALUE; // Adjust damage value as needed
                System.out.println(damagePoints);
                damagePointsLabel.setText("Damage Points: " + damagePoints);
            }
        }
    }
    private List<ImageView> Obstacles = new ArrayList<>();



    public void LogogenerateObstacles(Pane root, List<Rectangle> roadSegments, List<Rectangle> parallelRoadSegments1, List<Rectangle> parallelRoadSegments2, Random random) {
        // Load the images
        Image[] images = new Image[9];
        for (int i = 0; i < 9; i++) {
            images[i] = new Image(getClass().getResourceAsStream("/SocialMedia/logo" + (i + 1) + ".png"));
            if (images[i].isError()) {
                System.out.println("Error loading image: /SocialMedia/logo" + (i + 1) + ".png");
            }
        }

        obstacleTimelineLogo = new Timeline(
                new KeyFrame(Duration.seconds(3), event -> {
                    Rectangle segment;
                    int randomIndex = random.nextInt(roadSegments.size() + parallelRoadSegments1.size() + parallelRoadSegments2.size());
                    if (randomIndex < roadSegments.size()) {
                        segment = roadSegments.get(randomIndex);
                    } else if (randomIndex < roadSegments.size() + parallelRoadSegments1.size()) {
                        segment = parallelRoadSegments1.get(randomIndex - roadSegments.size());
                    } else {
                        segment = parallelRoadSegments2.get(randomIndex - roadSegments.size() - parallelRoadSegments1.size());
                    }

                    // Select a random image
                    Image image = images[random.nextInt(images.length)];
                    ImageView obstacleImageView = new ImageView(image);

                    // Set a fixed size for the image
                    double fixedWidth = 60; // Adjust as needed
                    double fixedHeight = 60; // Adjust as needed
                    obstacleImageView.setFitWidth(fixedWidth);
                    obstacleImageView.setFitHeight(fixedHeight);

                    double offsetY = segment.getHeight() - fixedHeight; // Adjust as needed

                    // Position the obstacle
                    obstacleImageView.setLayoutX(SCENE_WIDTH);
                    obstacleImageView.setLayoutY(segment.getLayoutY() + offsetY);

                    // Add the obstacle to the scene
                    root.getChildren().add(obstacleImageView);

                    // Debugging: Print statements
                    System.out.println("Added obstacle at X: " + obstacleImageView.getLayoutX() + " Y: " + obstacleImageView.getLayoutY());

                    // Add to obstacles list if needed
                    Obstacles.add(obstacleImageView);

                    // Move the obstacle
                    LogomoveObstacle(root, obstacleImageView, segment, offsetY);
                })
        );

        Duration pauseDuration = Duration.seconds(3); // Adjust the pause duration as needed
        obstacleTimelineLogo.setDelay(pauseDuration);
        obstacleTimelineLogo.setCycleCount(Timeline.INDEFINITE); // Make the timeline repeat indefinitely
        obstacleTimelineLogo.play(); // Start the timeline
    }

    private void LogomoveObstacle(Pane root, ImageView obstacle, Rectangle segment, double offsetY) {
        // Create the TranslateTransition for the obstacle
        TranslateTransition obstacleTransition = new TranslateTransition(Duration.seconds(5), obstacle);

        // Set the initial and final positions for the X coordinate
        obstacleTransition.setFromX(SCENE_WIDTH);
        obstacleTransition.setToX(-14*obstacle.getFitWidth());  // Move completely off the left side

        // Adjust the Y-coordinate to position the obstacle lower on the Y-axis
        obstacle.setLayoutY(segment.getLayoutY() + offsetY);

        cnt++;

        if(cnt==8)
        {
            System.out.println("quizobs ");
            obstacleTransition.stop();
            obstacleTimelineLogo.stop();
            isQuizObstacleFinished=true;
            System.out.println("choto quiz: "+ isQuizObstacleFinished);
        }
        // Debugging: Print statements
        System.out.println("Moving obstacle from X: " + obstacleTransition.getFromX() + " to X: " + obstacleTransition.getToX());

        // Set the action to be performed when the transition finishes
        obstacleTransition.setOnFinished(event -> {
            root.getChildren().remove(obstacle);
            Obstacles.remove(obstacle);
            System.out.println("Obstacle removed");
        });

        // Play the transition
        obstacleTransition.play();
    }

    public void LogocheckObstacleCollisions(Pane root, ImageView heroView, javafx.scene.text.Text damagePointsLabel, boolean isTimerRunning ) {
        // Calculate the bounds for the lower quarter of the hero's image
        double heroLowerY = heroView.getLayoutY() + HERO_HEIGHT * 0.95;
        double heroLowerHeight = HERO_HEIGHT * 0.05;
        if(!isTimerRunning && obstacleTimelineLogo!=null)
        {
            System.out.println("Quiz obstacle timeline detected");
            obstacleTimelineLogo.stop();
            for (Obstacles obstacle : new ArrayList<>(obstacles)) {
                root.getChildren().remove(obstacle.getShape());
                obstacles.remove(obstacle);
            }
        }

        // Iterate through each obstacle
        for (ImageView obstacle : new ArrayList<>(Obstacles)) {
            // Get the bounds of the obstacle
            Bounds obstacleBounds = obstacle.getBoundsInParent();

            // Calculate the bounds for the specific part of the hero's body you want to consider
            double heroPartX = heroView.getLayoutX(); // Adjust as needed
            double heroPartY = heroLowerY; // Adjust as needed
            double heroPartWidth = HERO_WIDTH; // Adjust as needed
            double heroPartHeight = heroLowerHeight; // Adjust as needed

            // Check if the specific part of the hero's body intersects with the obstacle
            if (obstacleBounds.intersects(heroPartX, heroPartY, heroPartWidth, heroPartHeight)) {
                // Collision detected between hero and obstacle
                // Remove the obstacle from the scene and the list
                root.getChildren().remove(obstacle);
                Obstacles.remove(obstacle);

                // Decrement health points and update health points label
                damagePoints -= DAMAGE_VALUE; // Adjust damage value as needed
                damagePointsLabel.setText("Damage Points: " + damagePoints);

                System.out.println("Collision detected. Damage points: " + damagePoints);
            }
        }
    }
}
