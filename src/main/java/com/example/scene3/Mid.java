package com.example.scene3;

import com.example.scene3.HelloController;
import com.example.scene3.Hero;
import javafx.animation.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Mid extends Pane {
    private static final int MID_WIDTH = 150; // Adjust the width of the coin as needed
    private static final int MID_HEIGHT = MID_WIDTH; // Adjust the height of the coin as needed
    private List<ImageView> mids = new ArrayList<>();
    public static int points = 0;
    public static javafx.scene.text.Text pointsLabel;
    private Random random = new Random(); // Declare the random variable as a class-level field
    private Image midImage;
    private ImageView mid;
    private Timeline midTimeline;
    public boolean isMidFinished=false;
    private int cnt=0;
    boolean collided=false;
    private TranslateTransition midTransition;
    private Label midWin=new Label();

    public Mid()
    {
        this.midImage = new Image(getClass().getResource("/Gold/MID.png").toExternalForm()); // Initialize the coinImage variable
        mid=new ImageView(midImage);
    }
    public void generateMid(Pane root, List<Rectangle> roadSegments, List<Rectangle> parallelRoadSegments1, List<Rectangle> parallelRoadSegments2, Random random) {
        // Create a timeline to continuously generate coins


        midTimeline = new Timeline(
                new KeyFrame(Duration.seconds(6), event -> { // Decrease the duration to generate coins more frequently
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
                    //System.out.println("Ki hocche bhai");
                    // Create the coin ImageView
                    ImageView mid = new ImageView(midImage);

                    mid.setFitWidth(MID_WIDTH);
                    mid.setFitHeight(MID_HEIGHT);

                    // Add the coin to the root and coins list
                    HelloController.root.getChildren().add(mid);
                    //coin.setVisible(true);
                    mids.add(mid);

                    // Move the coin
                    moveMid(root, mid, segment);


                })
        );
        Duration pauseDuration = Duration.seconds(1); // Adjust the pause duration as needed
        midTimeline.setDelay(pauseDuration);
        //midTimeline.setCycleCount(1);
        midTimeline.play();

    }





    private void moveMid(Pane root, ImageView mid, Rectangle segment) {
        cnt++;
        System.out.println("In movemid");
        midTransition = new TranslateTransition(Duration.seconds(6), mid);

        // Set the X position of the coin to be outside the scene on the right side
        mid.setLayoutX(HelloController.SCENE_WIDTH);

        // Set the Y position of the coin to be within the segment's Y range random.nextDouble()
        double y = segment.getLayoutY() + 1 * segment.getHeight() - MID_HEIGHT;
        mid.setLayoutY(y);

        // Calculate the distance to move the coin across the screen
        double distance = HelloController.SCENE_WIDTH + mid.getBoundsInLocal().getWidth();

        // Move the coin from right to left
        midTransition.setFromX(HelloController.SCENE_WIDTH);
        midTransition.setToX(-distance);
        // root.getChildren().add(coin);

        if(cnt==5)
        {
            System.out.println("mid"+cnt);
            midTransition.stop();
            midTimeline.stop();
            isMidFinished=true;
        }

        midTransition.setOnFinished(event->
                root.getChildren().remove(mid));

        // Start the coin transition
        midTransition.play();


    }


    /*public void checkCoinCollisions(Pane root, ImageView heroView) {
        // Calculate the bounds for the lower quarter of the hero's image
        double heroLowerY = heroView.getLayoutY() + Hero.HERO_HEIGHT * 0.90;
        double heroLowerHeight = Hero.HERO_HEIGHT * 0.10;
        if(!HelloController.isTimerRunning)
        {
            System.out.println("ASHSO?");
            midTimeline.stop();
            //midTransition.stop();
        }

        for (ImageView mid : new ArrayList<>(mids)) {
            if (!collided) {
                // Check if the lower quarter of the hero's image intersects with the coin
                if (mid.getBoundsInParent().intersects(heroView.getLayoutX(), heroLowerY, Hero.HERO_WIDTH, heroLowerHeight)) {
                    // Collision detected between hero and coin
                    root.getChildren().remove(mid); // Remove the coin from the scene
                    midTransition.stop();
                    mids.remove(mid); // Remove the coin from the list
                    // Increment points and update points label
                    System.out.println("MID DONE");
                    ShowMidWin(root);
                    HelloController.points += 75;
                    HelloController.pointsLabel.setText("Points: " + HelloController.points);
                    collided = true;

                }

            }
        }
    }*/
    public void checkCoinCollisions(Pane root, ImageView heroView, List<Rectangle> roadSegments, List<Rectangle> parallelRoadSegments1, List<Rectangle> parallelRoadSegments2) {
        // Calculate the bounds for the lower quarter of the hero's image
        double heroLowerY = heroView.getLayoutY() + Hero.HERO_HEIGHT * 0.90;
        double heroLowerHeight = Hero.HERO_HEIGHT * 0.10;

        if (!HelloController.isTimerRunning) {
            System.out.println("Timer stopped");
            midTimeline.stop();
        }

        for (ImageView mid : new ArrayList<>(mids)) {
            if (!collided) {
                // Check if the mid coin and hero are on the same road segment
                Rectangle heroSegment = getHeroSegment(heroView, roadSegments, parallelRoadSegments1, parallelRoadSegments2);
                Rectangle midSegment = getMidSegment(mid, roadSegments, parallelRoadSegments1, parallelRoadSegments2);

                if (heroSegment != null && midSegment != null && heroSegment.equals(midSegment)) {
                    System.out.println("Hero and mid are on the same segment");

                    // Check if the lower quarter of the hero's image intersects with the coin
                    if (mid.getBoundsInParent().intersects(heroView.getLayoutX(), heroLowerY, Hero.HERO_WIDTH, heroLowerHeight)) {
                        System.out.println("Collision detected");
                        // Collision detected between hero and coin
                        root.getChildren().remove(mid); // Remove the coin from the scene
                        midTransition.stop();
                        mids.remove(mid); // Remove the coin from the list
                        // Increment points and update points label
                        ShowMidWin(root);
                        HelloController.points += 75;
                        HelloController.pointsLabel.setText("Points: " + HelloController.points);
                        collided = true;
                    }
                } else {
                    System.out.println("Hero and mid are not on the same segment");
                }
            }
        }
    }

    // Helper method to find the road segment the hero is on
    private Rectangle getHeroSegment(ImageView heroView, List<Rectangle> roadSegments, List<Rectangle> parallelRoadSegments1, List<Rectangle> parallelRoadSegments2) {
        double heroY = heroView.getLayoutY() + Hero.HERO_HEIGHT / 2;
        System.out.println("Hero Y: " + heroY);

        for (Rectangle segment : roadSegments) {
            if (isWithinSegment(heroY, segment)) {
                return segment;
            }
        }

        for (Rectangle segment : parallelRoadSegments1) {
            if (isWithinSegment(heroY, segment)) {
                return segment;
            }
        }

        for (Rectangle segment : parallelRoadSegments2) {
            if (isWithinSegment(heroY, segment)) {
                return segment;
            }
        }

        return null;
    }

    // Helper method to find the road segment the mid coin is on
    private Rectangle getMidSegment(ImageView mid, List<Rectangle> roadSegments, List<Rectangle> parallelRoadSegments1, List<Rectangle> parallelRoadSegments2) {
        double midY = mid.getLayoutY() + MID_HEIGHT / 2;
        System.out.println("Mid Y: " + midY);

        for (Rectangle segment : roadSegments) {
            if (isWithinSegment(midY, segment)) {
                return segment;
            }
        }

        for (Rectangle segment : parallelRoadSegments1) {
            if (isWithinSegment(midY, segment)) {
                return segment;
            }
        }

        for (Rectangle segment : parallelRoadSegments2) {
            if (isWithinSegment(midY, segment)) {
                return segment;
            }
        }

        return null;
    }

    // Helper method to check if a Y coordinate is within a segment
    private boolean isWithinSegment(double y, Rectangle segment) {
        boolean result = y >= segment.getLayoutY() && y <= (segment.getLayoutY() + segment.getHeight());
        System.out.println("Checking segment: " + segment + " with Y: " + y + " - Result: " + result);
        return result;
    }




    public void ShowMidWin(Pane root)
    {
        midWin.setText("MID COMPLETE!!");
        midWin.setLayoutX(500);
        midWin.setLayoutY(300);
        midWin.setStyle("-fx-background-color: brown; -fx-border-color:#424242; -fx-font-size: 40; -fx-size: 120 400; -fx-border-style: dashed solid dashed solid;");



        System.out.println("HOISE?");
        PauseTransition pauseMid=new PauseTransition(Duration.seconds(3));
        pauseMid.setOnFinished(e->root.getChildren().remove(midWin));

        root.getChildren().add(midWin);
        pauseMid.play();
    }


}
