package com.example.scene3;

import javafx.animation.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Obstacle extends Pane {
    private ImageView obstacleImage;
    private double layoutX;
    private double layoutY;
    private double obstacle_width = 100;
    private double obstacle_height = 100;
    private Image image;
    private int scoreNeg = 0;
    public static List<ImageView> obstacles = new ArrayList<>();
    public Timeline obstacleTimeline;
    public TranslateTransition obstacleTransition;
    public static ImageView newImg;
    public boolean isObstacleFinished=false;
    private int cnt=0;

    public Obstacle(double layoutX, double layoutY) {
        image = new Image(getClass().getResource("/Obstacle/obstacle.png").toExternalForm());
        this.obstacleImage = new ImageView(image);
        obstacleImage.setFitWidth(obstacle_width); // Adjust width as needed
        obstacleImage.setFitHeight(obstacle_height); // Adjust height as needed
        this.layoutX = layoutX;
        this.layoutY = layoutY;
        obstacleImage.setLayoutX(layoutX);
        obstacleImage.setLayoutY(layoutY);

        newImg = new ImageView(image);


    }

    public ImageView getImageView() {
        return obstacleImage;
    }

    //    public double getLayoutX() {
//        return layoutX;
//    }
//
//    public double getLayoutY() {
//        return layoutY;
//    }
    public void generateCoins(Pane root, List<Rectangle> roadSegments, List<Rectangle> parallelRoadSegments1, List<Rectangle> parallelRoadSegments2, Random random) {
        // Create a timeline to continuously generate coins

        obstacleTimeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> { // Decrease the duration to generate coins more frequently
                    // Randomly select a road segment
                    Rectangle segment;
                    int randomIndex = random.nextInt(roadSegments.size() + parallelRoadSegments1.size() + parallelRoadSegments2.size());
                    if (randomIndex < roadSegments.size()) { // Select middle road
                        segment = roadSegments.get(randomIndex);
                    } else if (randomIndex < roadSegments.size() + parallelRoadSegments1.size()) { // Select upper parallel road
                        segment = parallelRoadSegments1.get(randomIndex - roadSegments.size());
                    } else { // Select lower parallel road
                        segment = parallelRoadSegments2.get(randomIndex - roadSegments.size() - parallelRoadSegments1.size());
                    }

                    // Create the coin ImageView
                    ImageView obs = new ImageView(image);
                    obs.setFitWidth(obstacle_width);
                    obs.setFitHeight(obstacle_height);

                    // Add the coin to the root and coins list
                    //root.getChildren().add(obstacleImage);
                    //coins.add(coin);
                    root.getChildren().add(obs);

                    obstacles.add(obs);
                    // Move the coin
                    moveObstacle(root, obs, segment);


                    //Obstacle obstacle = new Obstacle( SCENE_WIDTH, segment.getLayoutY() + 1 * segment.getHeight() - COIN_HEIGHT);
                    //obstacles.add(obs);

                })
        );
        obstacleTimeline.setCycleCount(Timeline.INDEFINITE);
        obstacleTimeline.play();


    }

    private void moveObstacle(Pane root, ImageView obstacle, Rectangle segment) {
        cnt++;
        obstacleTransition = new TranslateTransition(Duration.seconds(5), obstacle);

        // Set the X position of the coin to be outside the scene on the right side
        obstacle.setLayoutX(HelloController.SCENE_WIDTH);

        // Set the Y position of the coin to be within the segment's Y range random.nextDouble()
        double y = segment.getLayoutY() + 1 * segment.getHeight() - obstacle_height;
        obstacle.setLayoutY(y);

        // Calculate the distance to move the coin across the screen
        double distance = HelloController.SCENE_WIDTH + obstacle.getBoundsInLocal().getWidth();

        // Move the coin from right to left
        obstacleTransition.setFromX(HelloController.SCENE_WIDTH);
        obstacleTransition.setToX(-distance);

        if(cnt==3)
        {
            System.out.println("???"+cnt);
            obstacleTransition.stop();
            obstacleTimeline.stop();
            isObstacleFinished=true;
        }
        // Remove the coin from the root and coins list when the transition finishes
        obstacleTransition.setOnFinished(event -> {

            root.getChildren().remove(obstacle);
            obstacles.remove(obstacle);
            //System.out.println("NOOOOO");

        });

        // Start the coin transition
        obstacleTransition.play();

    }

    public void checkObstacleCollisions(Pane root, ImageView heroView, boolean heroIsJump) {
        // Calculate the bounds for the lower quarter of the hero's image
        double heroLowerY = heroView.getLayoutY() + Hero.HERO_HEIGHT * 0.90;
        double heroLowerHeight = Hero.HERO_HEIGHT * 0.10;

        boolean collided = false;
        //System.out.println("choto obstacle");
        for (ImageView obstacle : new ArrayList<>(obstacles)) {
            // Check if the lower quarter of the hero's image intersects with the coin
            if (!collided) {
                if (obstacle.getBoundsInParent().intersects(heroView.getLayoutX(), heroLowerY, Hero.HERO_WIDTH, heroLowerHeight)) {
                    // Collision detected between hero and coin
                    if (heroIsJump == false) {
                        // obstacle.setX(heroView.getLayoutX());


//                        newImg.setX(heroView.getLayoutX()+150);
//                        newImg.setY(obstacle.getLayoutY());
//                        newImg.setFitWidth(obstacle_width); // Adjust width as needed
//                        newImg.setFitHeight(obstacle_height); // Adjust height as needed
//
//
//                        obstacle.setVisible(false);
//                        root.getChildren().add(newImg);
//
//                        obstacleTransition.pause();
//                        //obstacleTransition.stop();
//                        HelloController.timer.stop();
//                        obstacleTimeline.pause();
                        scoreNeg++;
                        if (scoreNeg == 3) {
                            HelloController.points--;
                            HelloController.pointsLabel.setText("Points: " + HelloController.points);
                            scoreNeg = 0;
                        }
                    }


                    collided = true;
                    obstacles.remove(obstacle);
                    //break;
                    //heroView.setImage("/Run/");
                    //System.out.println("KENO");
                    // root.getChildren().remove(obstacle); // Remove the coin from the scene
                    // obstacles.remove(obstacle); // Remove the coin from the list
                    // Increment points and update points label
                }
                break;
            }
        }
//        if(heroIsJump)
//        {
//            if(obstacleTransition.getStatus()== Animation.Status.PAUSED) {
//                obstacleTransition.play();
//                HelloController.timer.start();
//            }
//            if(obstacleTransition.getStatus()== Animation.Status.STOPPED)
//
//                obstacleTimeline.play();
//            newImg.setVisible(false);
//        }
    }

    public static ImageView collisionY(Pane root, ImageView heroView) {
        // Calculate the bounds for the lower quarter of the hero's image
        double heroLowerY = heroView.getLayoutY() + Hero.HERO_HEIGHT * 0.90;
        double heroLowerHeight = Hero.HERO_HEIGHT * 0.10;

        boolean collided = false;
        for (ImageView obstacle : new ArrayList<>(obstacles)) {
            // Check if the lower quarter of the hero's image intersects with the coin
            if (!collided) {
                if (obstacle.getBoundsInParent().intersects(heroView.getLayoutX(), heroLowerY, Hero.HERO_WIDTH, heroLowerHeight)) {
                    // Collision detected between hero and coin

                    collided = true;
                    return obstacle;
                    //obstacles.remove(obstacle);
                }
                break;
            }
        }
        return null;

    }

}
