package com.example.scene3;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Coin extends Pane{
    private static final int COIN_WIDTH = 50;
    private static final int COIN_HEIGHT = 50;
    private List<ImageView> coins = new ArrayList<>();
    private Image coinImage;
    private Timeline coinTimeline;
    public boolean isCoinFinished=false;
    private int cnt=0;


    public Coin()
    {
        coinImage = new Image(getClass().getResource("/Gold/coin.png").toExternalForm()); // Initialize the coinImage variable
        //coinImageView=new ImageView(coinImage);
    }
    public void generateCoins(Pane root, List<Rectangle> roadSegments, List<Rectangle> parallelRoadSegments1, List<Rectangle> parallelRoadSegments2, Random random) {
        // Create a timeline to continuously generate coins

        cnt=0;
        coinTimeline = new Timeline(
                new KeyFrame(Duration.seconds(0.6), event -> { // Decrease the duration to generate coins more frequently
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
                    ImageView coin = new ImageView(coinImage);

                    coin.setFitWidth(COIN_WIDTH);
                    coin.setFitHeight(COIN_HEIGHT);

                    // Add the coin to the root and coins list
                    //change korsi
                    root.getChildren().add(coin);
                    //coin.setVisible(true);
                    coins.add(coin);
                    System.out.println("inside of coin gen");

                    // Move the coin
                    moveCoin(root, coin, segment);


                })
        );
        Duration pauseDuration = Duration.seconds(3); // Adjust the pause duration as needed
        coinTimeline.setDelay(pauseDuration);
        coinTimeline.setCycleCount(Timeline.INDEFINITE);
        coinTimeline.play();

    }





    private void moveCoin(Pane root, ImageView coin, Rectangle segment) {
        cnt++;
        TranslateTransition coinTransition = new TranslateTransition(Duration.seconds(5), coin);

        // Set the X position of the coin to be outside the scene on the right side
        coin.setLayoutX(HelloController.SCENE_WIDTH);

        // Set the Y position of the coin to be within the segment's Y range random.nextDouble()
        double y = segment.getLayoutY() + 1 * segment.getHeight() - COIN_HEIGHT;
        coin.setLayoutY(y);
        //System.out.println(coin.getLayoutX());
        // Calculate the distance to move the coin across the screen
        double distance = HelloController.SCENE_WIDTH + coin.getBoundsInLocal().getWidth();

        // Move the coin from right to left
        coinTransition.setFromX(HelloController.SCENE_WIDTH);
        coinTransition.setToX(-distance);
       // root.getChildren().add(coin);

        if(cnt==3)//20)
        {
            System.out.println("coin "+cnt);
            coinTransition.stop();
            coinTimeline.stop();
            isCoinFinished=true;
            System.out.println("coin ase? "+isCoinFinished);
        }

        coinTransition.setOnFinished(event -> {

            root.getChildren().remove(coin);
            coins.remove(coin);
            //System.out.println("NOOOOO");

        });

        // Start the coin transition
        coinTransition.play();

    }


    public void checkCoinCollisions(Pane root, ImageView heroView) {
        // Calculate the bounds for the lower quarter of the hero's image
        double heroLowerY = heroView.getLayoutY() + Hero.HERO_HEIGHT * 0.90;
        double heroLowerHeight = Hero.HERO_HEIGHT * 0.10;

        for (ImageView coin : new ArrayList<>(coins)) {
           // double coinLowerY = coin.getLayoutY() + COIN_HEIGHT * 0.90;
           // double coinLowerHeight = COIN_HEIGHT * 0.10;
            // Check if the lower quarter of the hero's image intersects with the coin
            if ((coin.getBoundsInParent() ).intersects(heroView.getLayoutX(), heroLowerY, Hero.HERO_WIDTH, heroLowerHeight)) {
                // Collision detected between hero and coin
                root.getChildren().remove(coin); // Remove the coin from the scene
                coins.remove(coin); // Remove the coin from the list
                // Increment points and update points label
                HelloController.points++;
                HelloController.pointsLabel.setText("Points: " + HelloController.points);
            }
        }
    }
    public void checkCoinCollisions1(Pane root, ImageView heroView) {
        // Calculate the bounds for the lower quarter of the hero's image
        double heroLowerY = heroView.getLayoutY() + Hero.HERO_HEIGHT * 0.90;
        double heroLowerHeight = Hero.HERO_HEIGHT * 0.10;

        for (ImageView coin : new ArrayList<>(coins)) {
            // double coinLowerY = coin.getLayoutY() + COIN_HEIGHT * 0.90;
            // double coinLowerHeight = COIN_HEIGHT * 0.10;
            // Check if the lower quarter of the hero's image intersects with the coin
            if ((coin.getBoundsInParent() ).intersects(heroView.getLayoutX(), heroLowerY, Hero.HERO_WIDTH, heroLowerHeight)) {
                // Collision detected between hero and coin
                root.getChildren().remove(coin); // Remove the coin from the scene
                coins.remove(coin); // Remove the coin from the list
                // Increment points and update points label
                HelloController2.points++;
                HelloController2.pointsLabel.setText("Points: " + HelloController2.points);
            }
        }
    }

    //3rd class
    public void checkCoinCollisions2(Pane root, ImageView heroView) {
        // Calculate the bounds for the lower quarter of the hero's image
        double heroLowerY = heroView.getLayoutY() + Hero.HERO_HEIGHT * 0.90;
        double heroLowerHeight = Hero.HERO_HEIGHT * 0.10;

        for (ImageView coin : new ArrayList<>(coins)) {
            // double coinLowerY = coin.getLayoutY() + COIN_HEIGHT * 0.90;
            // double coinLowerHeight = COIN_HEIGHT * 0.10;
            // Check if the lower quarter of the hero's image intersects with the coin
            if ((coin.getBoundsInParent() ).intersects(heroView.getLayoutX(), heroLowerY, Hero.HERO_WIDTH, heroLowerHeight)) {
                // Collision detected between hero and coin
                root.getChildren().remove(coin); // Remove the coin from the scene
                coins.remove(coin); // Remove the coin from the list
                // Increment points and update points label
                HelloController3.points++;
                HelloController3.pointsLabel.setText("Points: " + HelloController2.points);
            }
        }
    }

}
