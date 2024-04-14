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
        midTimeline.setCycleCount(1);
        midTimeline.play();

    }





    private void moveMid(Pane root, ImageView coin, Rectangle segment) {
        cnt++;
        midTransition = new TranslateTransition(Duration.seconds(6), coin);

        // Set the X position of the coin to be outside the scene on the right side
        coin.setLayoutX(HelloController.SCENE_WIDTH);

        // Set the Y position of the coin to be within the segment's Y range random.nextDouble()
        double y = segment.getLayoutY() + 1 * segment.getHeight() - MID_HEIGHT;
        coin.setLayoutY(y);

        // Calculate the distance to move the coin across the screen
        double distance = HelloController.SCENE_WIDTH + coin.getBoundsInLocal().getWidth();

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



        // Start the coin transition
        midTransition.play();


    }


    public void checkCoinCollisions(Pane root, ImageView heroView) {
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
