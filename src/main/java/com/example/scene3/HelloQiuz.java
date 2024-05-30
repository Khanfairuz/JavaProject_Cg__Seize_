package com.example.scene3;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
public class HelloQiuz extends Application {
    private static final int SCENE_WIDTH = 800;
    private static final int SCENE_HEIGHT = 600;
    private static final int SCROLL_SPEED = 4;
    private static final int ROAD_HEIGHT = 40;
    private static final int ROAD_Y = SCENE_HEIGHT - ROAD_HEIGHT;
    private static final int HERO_WIDTH = 200; // Adjust width as needed
    private static final int HERO_HEIGHT = 200; // Adjust height as needed
    private static final int HERO_Y = ROAD_Y - HERO_HEIGHT; // Adjust Y position as needed
    private static final int HERO_ANIMATION_INTERVAL = 10; // Interval between hero image changes in frames
    private static final int COIN_WIDTH = 50; // Adjust the width of the coin as needed
    private static final int COIN_HEIGHT = 50; // Adjust the height of the coin as needed

    private Pane root;
    private ImageView heroView;
    private Image[] heroImages;
    private int currentHeroImageIndex = 0;
    private int frameCount = 0;
    private List<ImageView> coins = new ArrayList<>();
    private int points = 0;
    private javafx.scene.text.Text pointsLabel;
    // Define a separate label for bonus points
    private javafx.scene.text.Text bonusPointsLabel;

    private Random random = new Random(); // Declare the random variable as a class-level field
    private Image coinImage;
    private boolean isJumping = false;
    private static final int ENEMY_WIDTH = 300; // Adjust width as needed
    private static final int ENEMY_HEIGHT = 300; // Adjust height as needed
    private Image enemyImage;
    private boolean isAttacking = false; // Flag to track if the hero is attacking
    // Define the enemies list and bonus points
    private List<ImageView> enemies = new ArrayList<>();
    private static final int BONUS_POINTS = 50; // Define the bonus points value
    // Modify the checkCoinCollisions method to update bonus points separately
    private int bonusPoints = 0;
    private boolean isDownKeyPressed = false; // Declare isDownKeyPressed at the class level



    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        // Load background image
        ImageView backgroundView1 = new ImageView("alien.jpg");
        ImageView backgroundView2 = new ImageView("alien.jpg");


        // Set initial positions of background views
        backgroundView1.setLayoutX(0);
        backgroundView2.setLayoutX(backgroundView1.getImage().getWidth());
        enemyImage = new Image(getClass().getResource("/Enemy/enemyImage.gif").toExternalForm());

        // Create a Pane to hold the background image views
        root = new Pane(backgroundView1, backgroundView2);
        // FileInputStream coinFileStream = new FileInputStream("C:\\Users\\HP\\Music\\coin.png");
        coinImage = new Image(getClass().getResource("/Gold/coin.png").toExternalForm()); // Initialize the coinImage variable

        // Create road segments
        Rectangle[] roadSegments = new Rectangle[4]; // Adjust the number of segments as needed
        Rectangle[] parallelRoadSegments1 = new Rectangle[4]; // Adjust the number of segments as needed
        Rectangle[] parallelRoadSegments2 = new Rectangle[4]; // Adjust the number of segments as needed
        for (int i = 0; i < roadSegments.length; i++) {
            roadSegments[i] = new Rectangle(SCENE_WIDTH, ROAD_HEIGHT, Color.BLUEVIOLET);
            roadSegments[i].setLayoutY(ROAD_Y);
            roadSegments[i].setLayoutX(i * SCENE_WIDTH);
            root.getChildren().add(roadSegments[i]);

            parallelRoadSegments1[i] = new Rectangle(SCENE_WIDTH, ROAD_HEIGHT, Color.BLUEVIOLET);
            parallelRoadSegments1[i].setLayoutY(ROAD_Y - ROAD_HEIGHT - 20);
            parallelRoadSegments1[i].setLayoutX(i * SCENE_WIDTH);
            root.getChildren().add(parallelRoadSegments1[i]);

            parallelRoadSegments2[i] = new Rectangle(SCENE_WIDTH, ROAD_HEIGHT, Color.BLUEVIOLET);
            parallelRoadSegments2[i].setLayoutY(ROAD_Y + ROAD_HEIGHT + 20);
            parallelRoadSegments2[i].setLayoutX(i * SCENE_WIDTH);
            root.getChildren().add(parallelRoadSegments2[i]);
        }

        List<Rectangle> roadSegmentList = new ArrayList<>(Arrays.asList(roadSegments));
        List<Rectangle> parallelRoadSegmentList1 = new ArrayList<>(Arrays.asList(parallelRoadSegments1));
        List<Rectangle> parallelRoadSegmentList2 = new ArrayList<>(Arrays.asList(parallelRoadSegments2));
        Random random = new Random();
        generateCoins(roadSegmentList, parallelRoadSegmentList1, parallelRoadSegmentList2, random, coinImage);
        generateEnemy(roadSegmentList,  parallelRoadSegmentList1,  parallelRoadSegmentList2, random,enemyImage);

        // Load hero images
        heroImages = new Image[6];
        for (int i = 0; i < heroImages.length; i++) {
            // FileInputStream inputStream = new FileInputStream("C:\\Users\\HP\\Music\\run3\\run3-0" + i + ".png");
            heroImages[i] = new Image(getClass().getResource("/Run/run3-0"+ i + ".png").toExternalForm());
        }

        // Create ImageView for hero and set initial position
        heroView = new ImageView(heroImages[0]);
        heroView.setFitWidth(HERO_WIDTH);
        heroView.setFitHeight(HERO_HEIGHT);
        heroView.setLayoutX(SCENE_WIDTH - HERO_WIDTH - 300);
        heroView.setLayoutY(HERO_Y);
        root.getChildren().add(heroView);
        Image[] heroAttackImages = new Image[4];
        for (int i = 0; i < heroAttackImages.length; i++) {
            //FileInputStream inputStream = new FileInputStream("/sword/adventurer-attack1-0" + (i + 1) + ".png");
            heroAttackImages[i] = new Image(getClass().getResource("/sword/adventurer-attack1-0" + (i + 1) + ".png").toExternalForm());
        }

        //declare korbo enemy

        // Set up animation timer to scroll the background, road, and animate the hero
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Update background positions
                backgroundView1.setLayoutX(backgroundView1.getLayoutX() - SCROLL_SPEED);
                backgroundView2.setLayoutX(backgroundView2.getLayoutX() - SCROLL_SPEED);

                // Update road segments' positions
                for (Rectangle segment : roadSegments) {
                    segment.setLayoutX(segment.getLayoutX() - SCROLL_SPEED);
                    if (segment.getLayoutX() <= -SCENE_WIDTH) {
                        segment.setLayoutX((roadSegments.length - 1) * SCENE_WIDTH);
                    }
                }

                // Update parallel road segments' positions
                for (Rectangle segment : parallelRoadSegments1) {
                    segment.setLayoutX(segment.getLayoutX() - SCROLL_SPEED);
                    if (segment.getLayoutX() <= -SCENE_WIDTH) {
                        segment.setLayoutX((parallelRoadSegments1.length - 1) * SCENE_WIDTH);
                    }
                }
                for (Rectangle segment : parallelRoadSegments2) {
                    segment.setLayoutX(segment.getLayoutX() - SCROLL_SPEED);
                    if (segment.getLayoutX() <= -SCENE_WIDTH) {
                        segment.setLayoutX((parallelRoadSegments2.length - 1) * SCENE_WIDTH);
                    }
                }

                // Animate hero
                frameCount++;
                if (frameCount >= HERO_ANIMATION_INTERVAL) {
                    frameCount = 0;
                    currentHeroImageIndex = (currentHeroImageIndex + 1) % heroImages.length;
                    heroView.setImage(heroImages[currentHeroImageIndex]);
                }

                // Check if any background view is completely out of the scene, then reposition it
                if (backgroundView1.getLayoutX() <= -backgroundView1.getImage().getWidth()) {
                    backgroundView1.setLayoutX(backgroundView2.getLayoutX() + backgroundView2.getImage().getWidth());
                }
                if (backgroundView2.getLayoutX() <= -backgroundView2.getImage().getWidth()) {
                    backgroundView2.setLayoutX(backgroundView1.getLayoutX() + backgroundView1.getImage().getWidth());
                }

                // Check for coin collisions
                checkCoinCollisions();
            }
        };
        timer.start();

        // Set up keyboard input handling
        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.LEFT) {
                // Move hero to the upper road if possible
                if (heroView.getLayoutY() > ROAD_Y - (2 * (ROAD_HEIGHT + 20)) - HERO_HEIGHT - 20) {
                    heroView.setLayoutY(heroView.getLayoutY() - (ROAD_HEIGHT + 20));
                }
            } else if (event.getCode() == KeyCode.RIGHT) {
                // Move hero to the lower road if possible
                if (heroView.getLayoutY() < ROAD_Y) {
                    heroView.setLayoutY(heroView.getLayoutY() + (ROAD_HEIGHT + 20));
                }
            }
            if (event.getCode() == KeyCode.UP && !isJumping) {
                jump();
            }
            if (scene != null) {
                if (event.getCode() == KeyCode.UP) {
                    jump();
                }
            }
            // Check for the down key
            if (event.getCode() == KeyCode.DOWN) {
                // Debug output to verify execution of down key event handler
                System.out.println("Down key pressed");
                isDownKeyPressed = true;
                checkEnemyCollisions(); // Check for collisions when the down key is pressed

                // Switch hero's image to attack animation
                isAttacking = true;
                new AnimationTimer() {
                    int currentAttackFrame = 0;

                    @Override
                    public void handle(long now) {
                        // Update hero's image with attack animation frames
                        heroView.setImage(heroAttackImages[currentAttackFrame]);
                        currentAttackFrame++;
                        if (currentAttackFrame >= heroAttackImages.length) {
                            this.stop(); // Stop the animation after reaching the last frame
                            // Switch back to the previous hero image after the attack animation
                            isAttacking = false;
                            heroView.setImage(heroImages[currentHeroImageIndex]);
                        }
                    }
                }.start();

            }
        });
        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.DOWN) {
                isDownKeyPressed = false;
            }
        });


        // Initialize points label
        pointsLabel = new javafx.scene.text.Text("Points: " + points);
        pointsLabel.setFill(Color.WHITE);
        pointsLabel.setStyle("-fx-font-size: 55px; -fx-text-fill: white;");
        pointsLabel.setLayoutX(20);
        pointsLabel.setLayoutY(50);
        root.getChildren().add(pointsLabel);
        // Add this code in the start method after initializing the points label
        bonusPointsLabel = new javafx.scene.text.Text("Bonus Points: 0");
        bonusPointsLabel.setFill(Color.WHITE);
        bonusPointsLabel.setStyle("-fx-font-size: 55px; -fx-text-fill: white;");
        bonusPointsLabel.setLayoutX(20);
        bonusPointsLabel.setLayoutY(120);
        root.getChildren().add(bonusPointsLabel);
        checkEnemyCollisions();


        // Set the stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("Scrolling Background with Continuous Road and Animated Hero");
        primaryStage.show();
    }

    private void generateCoins(List<Rectangle> roadSegments, List<Rectangle> parallelRoadSegments1, List<Rectangle> parallelRoadSegments2, Random random, Image coinImage) {
        // Create a timeline to continuously generate coins
        Timeline coinTimeline = new Timeline(
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
                    ImageView coin = new ImageView(coinImage);
                    coin.setFitWidth(COIN_WIDTH);
                    coin.setFitHeight(COIN_HEIGHT);

                    // Add the coin to the root and coins list
                    root.getChildren().add(coin);
                    coins.add(coin);

                    // Move the coin
                    moveCoin(coin, segment);
                })
        );
        // Pause coin generation for a certain time
        Duration pauseDuration = Duration.seconds(5); // Adjust the pause duration as needed
        coinTimeline.setDelay(pauseDuration);

        // Set the cycle count to INDEFINITE so the timeline continues indefinitely
        coinTimeline.setCycleCount(Timeline.INDEFINITE);

        // Start the timeline
        coinTimeline.play();
    }

    public static int cnt=0;
    private void moveCoin(ImageView coin, Rectangle segment) {
        cnt++;
        TranslateTransition coinTransition = new TranslateTransition(Duration.seconds(5), coin);

        // Set the X position of the coin to be outside the scene on the right side
        coin.setLayoutX(SCENE_WIDTH);

        // Set the Y position of the coin to be within the segment's Y range random.nextDouble()
        double y = segment.getLayoutY() + 1 * segment.getHeight() - COIN_HEIGHT;
        coin.setLayoutY(y);

        // Calculate the distance to move the coin across the screen
        double distance = SCENE_WIDTH + coin.getBoundsInLocal().getWidth();

        // Move the coin from right to left
        coinTransition.setFromX(SCENE_WIDTH);
        coinTransition.setToX(-distance);

        // Remove the coin from the root and coins list when the transition finishes
        coinTransition.setOnFinished(event -> {
            root.getChildren().remove(coin);
            coins.remove(coin);
        });

        // Start the coin transition
        coinTransition.play();
        Duration k=coinTransition.getDuration();
        Duration tocheck=new Duration(20);
        if(cnt==20)
        {
            System.out.println("???"+k);
            coinTransition.stop();
        }
    }
    private void jump() {
        isJumping = true;
        double initialJumpHeight = isJumping ? -200 : -100; // Initial jump height or higher jump height
        TranslateTransition jumpAnimation = new TranslateTransition(Duration.seconds(0.4), heroView);
        jumpAnimation.setByY(initialJumpHeight); // Set initial or higher jump height
        jumpAnimation.setCycleCount(1); // Only one cycle for each jump

        jumpAnimation.setOnFinished(event -> {
            isJumping = false;
            jumpToGround(); // Move the character back to the ground after the jump
        });

        jumpAnimation.play();
    }

    private void jumpToGround() {
        double currentY = heroView.getTranslateY();
        if (currentY != 0) {
            TranslateTransition jumpToGroundAnimation = new TranslateTransition(Duration.seconds(0.1), heroView);
            jumpToGroundAnimation.setToY(0); // Move the character back to the ground
            jumpToGroundAnimation.play();
            jumpToGroundAnimation.setOnFinished(event -> {
                isJumping = false; // Set isJumping to false after the jump animation finishes
                System.out.println("Jump animation finished, isJumping set to false");
            });
        }
    }
    private void generateEnemy(List<Rectangle> roadSegments, List<Rectangle> parallelRoadSegments1, List<Rectangle> parallelRoadSegments2, Random random, Image enemyImage) {
        // Create a timeline to continuously generate enemies
        Timeline enemyTimeline = new Timeline(
                new KeyFrame(Duration.seconds(3), event -> { // Adjust the duration as needed
                    // Randomly select a road segment for the enemy to land on
                    Rectangle segment;
                    int randomIndex = random.nextInt(roadSegments.size() + parallelRoadSegments1.size() + parallelRoadSegments2.size());
                    if (randomIndex < roadSegments.size()) { // Select middle road
                        segment = roadSegments.get(randomIndex);
                    } else if (randomIndex < roadSegments.size() + parallelRoadSegments1.size()) { // Select upper parallel road
                        segment = parallelRoadSegments1.get(randomIndex - roadSegments.size());
                    } else { // Select lower parallel road
                        segment = parallelRoadSegments2.get(randomIndex - roadSegments.size() - parallelRoadSegments1.size());
                    }

                    // Create the enemy ImageView
                    ImageView enemy = new ImageView(enemyImage);
                    enemy.setFitWidth(ENEMY_WIDTH);
                    enemy.setFitHeight(ENEMY_HEIGHT);

                    // Set the initial position of the enemy
                    enemy.setLayoutX(SCENE_WIDTH); // Start from the right side of the window
                    enemy.setLayoutY(segment.getLayoutY() - ENEMY_HEIGHT); // Land on top of the selected road

                    // Add the enemy to the root
                    root.getChildren().add(enemy);

                    // Move the enemy
                    double desiredLandingX=100;
                    double desiredLandingY=400;
                    moveEnemy(enemy, segment, desiredLandingX, desiredLandingY);

                })
        );
        enemyTimeline.setCycleCount(Timeline.INDEFINITE);
        enemyTimeline.play();
    }


    private void moveEnemy(ImageView enemy, Rectangle segment, double landingX, double landingY) {
        TranslateTransition enemyTransition = new TranslateTransition(Duration.seconds(2), enemy);

        // Set the initial X position of the enemy to be in the middle of the window
        enemy.setLayoutX(SCENE_WIDTH / 2 - ENEMY_WIDTH / 2);

        // Set the initial Y position of the enemy to be above the window
        enemy.setLayoutY(-ENEMY_HEIGHT);

        // Move the enemy vertically to the specified landing Y position on the selected road
        enemyTransition.setToY(segment.getLayoutY() - ENEMY_HEIGHT + landingY);

        // Move the enemy horizontally to the specified landing X position
        enemyTransition.setToX(landingX);

        // Remove the enemy from the root when the transition finishes
        enemyTransition.setOnFinished(event -> {
            root.getChildren().remove(enemy);
        });

        // Start the enemy transition
        enemyTransition.play();
    }

    private void checkCoinCollisions() {
        // Calculate the bounds for the lower quarter of the hero's image
        double heroLowerY = heroView.getLayoutY() + HERO_HEIGHT * 0.95;
        double heroLowerHeight = HERO_HEIGHT * 0.05;

        for (ImageView coin : new ArrayList<>(coins)) {
            // Check if the lower quarter of the hero's image intersects with the coin
            if (coin.getBoundsInParent().intersects(heroView.getLayoutX(), heroLowerY, HERO_WIDTH, heroLowerHeight)) {
                // Collision detected between hero and coin
                root.getChildren().remove(coin); // Remove the coin from the scene
                coins.remove(coin); // Remove the coin from the list
                // Increment points and update points label
                points++;
                pointsLabel.setText("Points: " + points);
            }
        }

    }
    private void checkEnemyCollisions() {
        // Define the additional range to increase the collision area around the hero
        final double collisionRangeX = 400; // Adjust as needed
        final double collisionRangeY = 400; // Adjust as needed

        // Get the bounds of the hero including the collision range
        Bounds heroBounds = new BoundingBox(
                heroView.getBoundsInParent().getMinX() - collisionRangeX,
                heroView.getBoundsInParent().getMinY() - collisionRangeY,
                heroView.getBoundsInParent().getWidth() + 2 * collisionRangeX,
                heroView.getBoundsInParent().getHeight() + 2 * collisionRangeY
        );

        // Iterate through each enemy
        for (ImageView enemy : new ArrayList<>(enemies)) {
            // Get the bounds of the enemy
            Bounds enemyBounds = enemy.getBoundsInParent();

            // Check if the bounds of the hero intersect with the bounds of the enemy
            if (heroBounds.intersects(enemyBounds)) {
                // Handle collision between hero and enemy
                // Remove the enemy from the scene and the list
                root.getChildren().remove(enemy);
                enemies.remove(enemy);

                // Increment bonus points and update bonus points label
                bonusPoints++;
                bonusPointsLabel.setText("Bonus points: " + bonusPoints);
            }
        }
    }


    private boolean isInSameRoad(ImageView hero, ImageView enemy) {
        double heroY = hero.getLayoutY();
        double enemyY = enemy.getLayoutY();
        double roadHeight = ROAD_HEIGHT;

        // Check if the enemy and hero are in the same road segment
        return Math.abs(heroY - enemyY) <= roadHeight;
    }


    public static void main(String[] args) {
        launch(args);
    }
}