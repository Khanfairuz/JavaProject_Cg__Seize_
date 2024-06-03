package com.example.scene3;

import javafx.animation.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BigObstacle  {
    private ImageView BigobstacleImage;
    private double layoutX;
    private double layoutY;
    private double obstacle_width = 1000;
    private double obstacle_height = 60;
    private Image image;
    public static List<ImageView> Bigobstacles = new ArrayList<>();
    public Timeline BigobstacleTimeline;
    public TranslateTransition obstacleTransition;
    public TranslateTransition obstacleTransition2;
    public TranslateTransition obstacleTransition3;

    public static ImageView newImg;
    public boolean isBigObstacleFinished=false;
    private int cnt=0;





    public BigObstacle(double layoutX, double layoutY, String obsname) {
        image = new Image(getClass().getResource("/Obstacle/"+obsname+".png").toExternalForm());
        this.BigobstacleImage = new ImageView(image);
        BigobstacleImage.setFitWidth(obstacle_width); // Adjust width as needed
        BigobstacleImage.setFitHeight(obstacle_height); // Adjust height as needed
        this.layoutX = layoutX;
        this.layoutY = layoutY;
        BigobstacleImage.setLayoutX(layoutX);
        BigobstacleImage.setLayoutY(layoutY);

        newImg = new ImageView(image);


    }
    public BigObstacle(double layoutX, double layoutY, String obsname, int height, int width) {
        image = new Image(getClass().getResource("/Obstacle/"+obsname+".png").toExternalForm());
        this.BigobstacleImage = new ImageView(image);
        BigobstacleImage.setFitWidth(obstacle_width); // Adjust width as needed
        BigobstacleImage.setFitHeight(obstacle_height); // Adjust height as needed
        this.layoutX = layoutX;
        this.layoutY = layoutY;
        BigobstacleImage.setLayoutX(layoutX);
        BigobstacleImage.setLayoutY(layoutY);
        obstacle_height=height;
        obstacle_width=width;

        newImg = new ImageView(image);


    }


    public void generateBigCoins(Pane root, List<Rectangle> roadSegments, List<Rectangle> parallelRoadSegments1, List<Rectangle> parallelRoadSegments2, Random random) {
        // Create a timeline to continuously generate coins

        BigobstacleTimeline = new Timeline(
                new KeyFrame(Duration.seconds(3), event -> { // Decrease the duration to generate coins more frequently
                    // Randomly select a road segment


                    Rectangle segment1 = roadSegments.get(random.nextInt(roadSegments.size()));
                    Rectangle segment2 = parallelRoadSegments1.get(random.nextInt(parallelRoadSegments1.size()));
                    Rectangle segment3 = parallelRoadSegments2.get(random.nextInt(parallelRoadSegments2.size()));


//                    int randomIndex = random.nextInt(roadSegments.size() + parallelRoadSegments1.size() + parallelRoadSegments2.size());
//                    if (randomIndex < roadSegments.size()) { // Select middle road
//                        segment1 = roadSegments.get(randomIndex);
//                    }
//                    if (randomIndex < roadSegments.size() + parallelRoadSegments1.size()) { // Select upper parallel road
//                        segment2 = parallelRoadSegments1.get(randomIndex - roadSegments.size());
//                    }
//                     // Select lower parallel road
//                        segment3 = parallelRoadSegments2.get(randomIndex - roadSegments.size() - parallelRoadSegments1.size());

//                    Rectangle segment1=roadSegments.get(randomIndex);
//                    Rectangle segment2=parallelRoadSegments1.get(randomIndex - roadSegments.size());
//                    Rectangle segment3=parallelRoadSegments2.get(randomIndex - roadSegments.size() - parallelRoadSegments1.size());
//                    // Create the coin ImageView
                    ImageView obs1 = new ImageView(image);
                    obs1.setFitWidth(obstacle_width);
                    obs1.setFitHeight(obstacle_height);

                    ImageView obs2 = new ImageView(image);
                    obs2.setFitWidth(obstacle_width);
                    obs2.setFitHeight(obstacle_height);

                    ImageView obs3 = new ImageView(image);
                    obs3.setFitWidth(obstacle_width);
                    obs3.setFitHeight(obstacle_height);

                    // Add the coin to the root and coins list
                    //root.getChildren().add(obstacleImage);
                    //coins.add(coin);
                    root.getChildren().addAll(obs1, obs2, obs3);
                    Bigobstacles.add(obs1);
                    Bigobstacles.add(obs2);
                    Bigobstacles.add(obs3);
                    System.out.println(Bigobstacles.size()+" size of bigobstac;e");
                    // Move the coin
                    //moveObstacle(root, obs, segment);
                    moveObstacle(root, obs1, segment1);
                    moveObstacle2(root, obs2, segment2);
                    moveObstacle3(root, obs3, segment3);


                    //Obstacle obstacle = new Obstacle( SCENE_WIDTH, segment.getLayoutY() + 1 * segment.getHeight() - COIN_HEIGHT);
                    //obstacles.add(obs);

                })
        );
        BigobstacleTimeline.setCycleCount(1);
        BigobstacleTimeline.play();


    }

    private void moveObstacle(Pane root, ImageView obstacle, Rectangle segment) {
        cnt++;
        obstacleTransition = new TranslateTransition(Duration.seconds(10), obstacle);

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

//        if(cnt==3)
//        {
//            //System.out.println("???"+cnt);
//            obstacleTransition.stop();
//            obstacleTimeline.stop();
//            isObstacleFinished=true;
//        }
        // Remove the coin from the root and coins list when the transition finishes
        obstacleTransition.setOnFinished(event -> {

            root.getChildren().remove(obstacle);
            Bigobstacles.remove(obstacle);
            //System.out.println("NOOOOO");

        });

        // Start the coin transition
        obstacleTransition.play();

    }
    private void moveObstacle2(Pane root, ImageView obstacle, Rectangle segment) {
        cnt++;
        obstacleTransition2 = new TranslateTransition(Duration.seconds(10), obstacle);

        // Set the X position of the coin to be outside the scene on the right side
        obstacle.setLayoutX(HelloController.SCENE_WIDTH);

        // Set the Y position of the coin to be within the segment's Y range random.nextDouble()
        double y = segment.getLayoutY() + 1 * segment.getHeight() - obstacle_height;
        obstacle.setLayoutY(y);

        // Calculate the distance to move the coin across the screen
        double distance = HelloController.SCENE_WIDTH + obstacle.getBoundsInLocal().getWidth();

        // Move the coin from right to left
        obstacleTransition2.setFromX(HelloController.SCENE_WIDTH);
        obstacleTransition2.setToX(-distance);

//        if(cnt==3)
//        {
//            //System.out.println("???"+cnt);
//            obstacleTransition.stop();
//            obstacleTimeline.stop();
//            isObstacleFinished=true;
//        }
        // Remove the coin from the root and coins list when the transition finishes
        obstacleTransition2.setOnFinished(event -> {

            root.getChildren().remove(obstacle);
            Bigobstacles.remove(obstacle);
            //System.out.println("NOOOOO");

        });

        // Start the coin transition
        obstacleTransition2.play();

    }

    private void moveObstacle3(Pane root, ImageView obstacle, Rectangle segment) {
        cnt++;
        obstacleTransition3 = new TranslateTransition(Duration.seconds(10), obstacle);

        // Set the X position of the coin to be outside the scene on the right side
        obstacle.setLayoutX(HelloController.SCENE_WIDTH);

        // Set the Y position of the coin to be within the segment's Y range random.nextDouble()
        double y = segment.getLayoutY() + 1 * segment.getHeight() - obstacle_height;
        obstacle.setLayoutY(y);

        // Calculate the distance to move the coin across the screen
        double distance = HelloController.SCENE_WIDTH + obstacle.getBoundsInLocal().getWidth();

        // Move the coin from right to left
        obstacleTransition3.setFromX(HelloController.SCENE_WIDTH);
        obstacleTransition3.setToX(-distance);

        if(cnt==3)
        {
            //System.out.println("???"+cnt);
//            obstacleTransition.stop();
//            obstacleTimeline.stop();
            isBigObstacleFinished=true;
        }
        // Remove the coin from the root and coins list when the transition finishes
        obstacleTransition3.setOnFinished(event -> {

            root.getChildren().remove(obstacle);
            Bigobstacles.remove(obstacle);
            //System.out.println("NOOOOO");

        });

        // Start the coin transition
        obstacleTransition3.play();

    }
    public void checkBigObstacleCollisions(Pane root, ImageView heroView, boolean heroIsJump,HelloController hc ) {
        // Calculate the bounds for the lower quarter of the hero's image
        double heroLowerY = heroView.getLayoutY() + Hero.HERO_HEIGHT * 0.90;
        double heroLowerHeight = Hero.HERO_HEIGHT * 0.10;

        boolean collided = false;
        for (ImageView obstacle : new ArrayList<>(Bigobstacles)) {
            // Check if the lower quarter of the hero's image intersects with the coin
           // if (!collided) {
            double obstacleY = obstacle.getLayoutY() + obstacle_height * 0.450;
            double obsLowerHeight = obstacle_height * 0.50;
           // System.out.println("Big obstacle "+obstacle.getLayoutY());
                if (obstacle.getBoundsInParent().intersects(heroView.getBoundsInParent())) {
           // if (heroView.getBoundsInParent().intersects(obstacle.getLayoutX(), obstacleY, obstacle_width, obsLowerHeight)) {
                System.out.println("bigggggggggg collides");

                        obstacleTransition.pause();
                        obstacleTransition2.pause();
                        obstacleTransition3.pause();
                        //obstacleTransition.stop();
                        HelloController.timer.stop();
                        HelloController.isTimerRunning=false;


                         BigobstacleTimeline.pause();
                        System.out.println("GAME OVER");



                    Bigobstacles.remove(obstacle);
                    ShowCOll(root);
                    // Create the button
                    Button button = new Button("Next");

                    // Set button size if needed
                    button.setPrefSize(100, 50);
                    button.setFont(new Font(25));
                    button.setStyle("-fx-background-color: indigo;");

                    // Set button action using a lambda function
                    button.setOnAction(e -> {
                        // Your lambda function code here
                        hc.call_hero_lose();
                    });
                    // Manually trigger the layout listeners to position the button initially
                    button.setLayoutX(root.getWidth() - button.getPrefWidth() - 20);
                    button.setLayoutY(10);
                    // Add the button to the pane
                    root.getChildren().add(button);








                    //


                }
                //break;
            }

    }

    public void checkBigObstacleCollisions1(Pane root, ImageView heroView, boolean heroIsJump , HelloController2 hc) {
        // Calculate the bounds for the lower quarter of the hero's image
        double heroLowerY = heroView.getLayoutY() + Hero.HERO_HEIGHT * 0.90;
        double heroLowerHeight = Hero.HERO_HEIGHT * 0.10;

        boolean collided = false;
        for (ImageView obstacle : new ArrayList<>(Bigobstacles)) {
            // Check if the lower quarter of the hero's image intersects with the coin
            // if (!collided) {
            if (obstacle.getBoundsInParent().intersects(heroView.getBoundsInParent())) {

                obstacleTransition.pause();
                obstacleTransition2.pause();
                obstacleTransition3.pause();
                //obstacleTransition.stop();
                HelloController2.timer.stop();
                HelloController2.isTimerRunning=false;


                BigobstacleTimeline.pause();
                System.out.println("GAME OVER");


                Bigobstacles.remove(obstacle);
                ShowCOll(root);
                // Create the button
                Button button = new Button("Next");

                // Set button size if needed
                button.setPrefSize(100, 50);
                button.setFont(new Font(25));
                button.setStyle("-fx-background-color: #C70039 ;");


                // Set button action using a lambda function
                button.setOnAction(e -> {
                    // Your lambda function code here
                    hc.call_hero_lose();
                });
                // Manually trigger the layout listeners to position the button initially
                button.setLayoutX(root.getWidth() - button.getPrefWidth() - 10);
                button.setLayoutY(10);
                // Add the button to the pane
                root.getChildren().add(button);


            }
            //break;
        }

    }
    public void checkBigObstacleCollisions2(Pane root, ImageView heroView, boolean heroIsJump , HelloController3 hc, int bonusPoints ) {
        // Calculate the bounds for the lower quarter of the hero's image
        double heroLowerY = heroView.getLayoutY() + Hero.HERO_HEIGHT * 0.90;
        double heroLowerHeight = Hero.HERO_HEIGHT * 0.10;

        boolean collided = false;
        for (ImageView obstacle : new ArrayList<>(Bigobstacles)) {
            // Check if the lower quarter of the hero's image intersects with the coin
            // if (!collided) {
            if (obstacle.getBoundsInParent().intersects(heroView.getBoundsInParent())) {
                if (bonusPoints >= 12) {
                    // Display "Life retrieved" message
                    Label lifeRetrievedLabel = new Label("Life retrieved as bonus point>15");
                    //lifeRetrievedLabel.setFont(new Font(25));
                    lifeRetrievedLabel.setStyle("-fx-background-color: #a67b5b; -fx-border-color: black; -fx-font-size: 25; -fx-text-fill: black; -fx-size: 40 1500; -fx-border-style: dashed solid dashed solid;");

                    root.getChildren().add(lifeRetrievedLabel);

                    // Center the label on the screen
                    lifeRetrievedLabel.setLayoutX(((root.getWidth() - lifeRetrievedLabel.getWidth()) / 2)-120);
                    lifeRetrievedLabel.setLayoutY(((root.getHeight() - lifeRetrievedLabel.getHeight()) / 2)-120);

                    // Create a Timeline to remove the label after 2 seconds
                    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
                        root.getChildren().remove(lifeRetrievedLabel);
                    }));
                    timeline.setCycleCount(1);
                    timeline.play();

                    break;
                }

                else if (bonusPoints<12) {
                    obstacleTransition.pause();
                    obstacleTransition2.pause();
                    obstacleTransition3.pause();
                    //obstacleTransition.stop();
                    HelloController3.timer.stop();
                    HelloController3.isTimerRunning = false;


                    BigobstacleTimeline.pause();
                    System.out.println("GAME OVER");

                    Bigobstacles.remove(obstacle);
                    ShowCOll(root);
                    // Create the button
                    Button button = new Button("Next");

                    // Set button size if needed
                    button.setPrefSize(100, 50);
                    button.setFont(new Font(25));
                    button.setStyle("-fx-background-color: indigo;");


                    // Set button action using a lambda function
                    button.setOnAction(e -> {
                        // Your lambda function code here
                        hc.call_hero_lose();
                    });
                    // Manually trigger the layout listeners to position the button initially
                    button.setLayoutX(root.getWidth() - button.getPrefWidth() - 10);
                    button.setLayoutY(10);
                    // Add the button to the pane
                    root.getChildren().add(button);
                }
               /* else
                {
                        Timeline Level = new Timeline(
                                new KeyFrame(Duration.seconds(3`), actionEvent -> {
                                    ShowLifeline(root);
                                })
                        );
                        Level.play();


                }*/

            }
            //break;
        }


    }
    public void ShowCOll(Pane root)
    {
        Label collDone=new Label();
        collDone.setText("GAME OVER!!");
        collDone.setLayoutX(350);
        collDone.setLayoutY(200);
        collDone.setStyle("-fx-background-color: black; -fx-border-color:#424242; -fx-font-size: 100; -fx-text-fill: red; -fx-size: 120 400; -fx-border-style: dashed solid dashed solid;");



        System.out.println("HOISE?");
       // PauseTransition pauseMid=new PauseTransition(Duration.seconds());
       // pauseMid.setOnFinished(e->root.getChildren().remove(collDone));

        root.getChildren().add(collDone);
       // pauseMid.play();
    }
    public void ShowLifeline(Pane root)
    {
        Label collDone=new Label();
        collDone.setText("Life renewed for bonus points");
        collDone.setLayoutX(300);
        collDone.setLayoutY(200);
        collDone.setStyle("-fx-background-color: black; -fx-border-color:#424242; -fx-font-size: 100; -fx-text-fill: red; -fx-size: 60 200; -fx-border-style: dashed solid dashed solid;");



        System.out.println("HOISE?");
        // PauseTransition pauseMid=new PauseTransition(Duration.seconds());
        // pauseMid.setOnFinished(e->root.getChildren().remove(collDone));

        root.getChildren().add(collDone);
        // pauseMid.play();
    }
    public static ImageView collisionY(Pane root, ImageView heroView) {
        // Calculate the bounds for the lower quarter of the hero's image
        double heroLowerY = heroView.getLayoutY() + Hero.HERO_HEIGHT * 0.90;
        double heroLowerHeight = Hero.HERO_HEIGHT * 0.10;

        System.out.println("big obstacle collision check shuru hoise ");

        boolean collided = false;
        for (ImageView obstacle : new ArrayList<>(Bigobstacles)) {
            // Check if the lower quarter of the hero's image intersects with the coin

            System.out.println("Big obstacle ");
            if (!collided) {
                if (obstacle.getBoundsInParent().intersects(heroView.getLayoutX(), heroLowerY, Hero.HERO_WIDTH, heroLowerHeight)) {
                    // Collision detected between hero and coin

                    System.out.println("Big obstacle collide");
                    collided = true;
                    return obstacle;
                    //obstacles.remove(obstacle);
                }
                //break;
            }
        }
        return null;

    }

}
