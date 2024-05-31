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
import java.io.FileNotFoundException;
import java.util.Arrays;
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
public class HelloQiuz extends Application {
    private static final int SCENE_WIDTH = 800;
    private static final int SCENE_HEIGHT = 600;
    private static final int SCROLL_SPEED = 10;
    private static final int ROAD_HEIGHT = 40;
    private static final int ROAD_Y = SCENE_HEIGHT - ROAD_HEIGHT;
    private static final int HERO_WIDTH = 200; // Adjust width as needed
    private static final int HERO_HEIGHT = 200; // Adjust height as needed
    private static final int HERO_Y = ROAD_Y - HERO_HEIGHT; // Adjust Y position as needed
    private static final int HERO_ANIMATION_INTERVAL = 10; // Interval between hero image changes in frames
    private static final int COIN_WIDTH = 50; // Adjust the width of the coin as needed
    private static final int COIN_HEIGHT = 50;
    private static final int ENEMY_WIDTH = 100; // Adjust width as needed
    private static final int ENEMY_HEIGHT = 100;
    private static final int QUIZ_WIDTH = 200; // Adjust these dimensions as needed
    private static final int QUIZ_HEIGHT = 200;
    private Pane root;
    private ImageView heroView;
    private Image[] heroImages;
    private int currentHeroImageIndex = 0;
    private int frameCount = 0;
    private List<ImageView> coins = new ArrayList<>();
    private List<ImageView> quizzes = new ArrayList<>();
    private int points = 0;
    private javafx.scene.text.Text pointsLabel;
    // Define a separate label for bonus points
    private javafx.scene.text.Text bonusPointsLabel;

    private Random random = new Random(); // Declare the random variable as a class-level field
    private Image coinImage;
    private boolean isJumping = false;

    private Image enemyImage;
    private Image quizImage;
    private boolean isAttacking = false; // Flag to track if the hero is attacking
    // Define the enemies list and bonus points
    private List<ImageView> enemies = new ArrayList<>();
    private static final int BONUS_POINTS = 50; // Define the bonus points value
    // Modify the checkCoinCollisions method to update bonus points separately
    private int bonusPoints = 0;
    private boolean isDownKeyPressed = false; // Declare isDownKeyPressed at the class level
    private Image obstacleImage; // Image for obstacle (if using images)
    private List<Obstacles> obstacles = new ArrayList<>();
    private int damagePoints = 0; // Initialize damage points
    private javafx.scene.text.Text damagePointsLabel;
    private static final int DAMAGE_VALUE = 10;




    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        // Load background image
        ImageView backgroundView1 = new ImageView("alien.jpg");
        ImageView backgroundView2 = new ImageView("alien.jpg");


        // Set initial positions of background views
        backgroundView1.setLayoutX(0);
        backgroundView2.setLayoutX(backgroundView1.getImage().getWidth());
        enemyImage = new Image(getClass().getResource("/Enemy/wow.png").toExternalForm());
        //quizImage = new Image(getClass().getResource("/Gold/QUIZ.gif").toExternalForm());

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
        //generateEnemy(roadSegmentList,  parallelRoadSegmentList1,  parallelRoadSegmentList2, random,enemyImage);
        generateObstacles(roadSegmentList, parallelRoadSegmentList1, parallelRoadSegmentList2, random);
        generateEnemy( random,enemyImage);

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
                checkObstacleCollisions();
                checkEnemyCollisions();
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
                //checkEnemyCollisions(); // Check for collisions when the down key is pressed

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
        // Add this code in the start method after initializing the points label
        damagePointsLabel = new javafx.scene.text.Text("Damage Points: " + damagePoints);
        damagePointsLabel.setFill(Color.WHITE);
        damagePointsLabel.setStyle("-fx-font-size: 55px; -fx-text-fill: white;");
        damagePointsLabel.setLayoutX(SCENE_WIDTH );
        damagePointsLabel.setLayoutY(50);
        root.getChildren().add(damagePointsLabel);
        //checkEnemyCollisions();


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
        Timeline quizTimeline = new Timeline(
                //new KeyFrame(Duration.minutes(2).add(Duration.seconds(30)),
                new KeyFrame(Duration.seconds(1),event -> {
                    addQuizToRoads(quizImage, roadSegments);
                })
        );
        quizTimeline.play();
        // Pause coin generation for a certain time
        Duration pauseDuration = Duration.seconds(2); // Adjust the pause duration as needed
        coinTimeline.setDelay(pauseDuration);

        // Set the cycle count to INDEFINITE so the timeline continues indefinitely
        coinTimeline.setCycleCount(Timeline.INDEFINITE);

        // Start the timeline
        coinTimeline.play();
        Duration stopDuration = Duration.minutes(2).add(Duration.seconds(28));

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
                /*if (points >= 5) {
                    // Launch  after collecting 5 points
                    Platform.runLater(() -> {
                        try {
                            // Create a new instance of HelloController and start it
                            HelloController helloController = new HelloController();
                            helloController.start(new Stage());
                            // Close the current stage (HelloQiuz)
                            ((Stage) root.getScene().getWindow()).close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }*/

            }
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

    // Generate obstacles similar to coins
    private void generateObstacles(List<Rectangle> roadSegments, List<Rectangle> parallelRoadSegments1, List<Rectangle> parallelRoadSegments2, Random random) {
        Timeline obstacleTimeline = new Timeline(
                new KeyFrame(Duration.seconds(6), event -> {
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
                    //Color randomColor = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
                    //Obstacles obstacle = new Obstacles(width, 50, randomColor, 10); // Adjust other parameters as needed
                    //root.getChildren().add(obstacle.getShape());
                    Obstacles obstacle = new Obstacles(width,width, 60, Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256)), 40);
                    double offsetY = segment.getHeight() - 50; // Adjust as needed
                    root.getChildren().add(obstacle.getShape());
                    obstacles.add(obstacle);
                    moveObstacle(obstacle, segment, offsetY);
                })
        );
        Duration pauseDuration = Duration.seconds(7); // Adjust the pause duration as needed
        obstacleTimeline.setDelay(pauseDuration);
        // Set timeline properties...
        obstacleTimeline.setCycleCount(Timeline.INDEFINITE); // Make the timeline repeat indefinitely
        obstacleTimeline.play(); // Start the timeline
    }

    // Move obstacles across the screen
    // Move obstacles across the screen
    // Move obstacles across the screen with the specified offsetY
    private void moveObstacle(Obstacles obstacle, Rectangle segment, double offsetY) {
        TranslateTransition obstacleTransition = new TranslateTransition(Duration.seconds(5), obstacle.getShape());
        obstacleTransition.setFromX(SCENE_WIDTH);
        obstacleTransition.setToX(-obstacle.getShape().getBoundsInLocal().getWidth());
        // Adjust the Y-coordinate to position the obstacle lower on the Y-axis
        obstacleTransition.setFromY(segment.getLayoutY() + offsetY);
        obstacleTransition.setToY(segment.getLayoutY() + offsetY);
        obstacleTransition.setOnFinished(event -> root.getChildren().remove(obstacle.getShape()));
        obstacleTransition.play();
    }
    private void checkObstacleCollisions() {
        // Calculate the bounds for the lower quarter of the hero's image
        double heroLowerY = heroView.getLayoutY() + HERO_HEIGHT * 0.95;
        double heroLowerHeight = HERO_HEIGHT * 0.05;

        // Iterate through each obstacle
        for (Obstacles obstacle : new ArrayList<>(obstacles)) {
            // Get the bounds of the obstacle
            Bounds obstacleBounds = obstacle.getShape().getBoundsInParent();

            // Calculate the bounds for the specific part of the hero's body you want to consider
            double heroPartX = heroView.getLayoutX(); // Adjust as needed
            double heroPartY = heroLowerY; // Adjust as needed
            double heroPartWidth = HERO_WIDTH; // Adjust as needed
            double heroPartHeight = heroLowerHeight; // Adjust as needed

            // Check if the specific part of the hero's body intersects with the obstacle
            if (obstacleBounds.intersects(heroPartX, heroPartY, heroPartWidth, heroPartHeight)) {
                // Collision detected between hero and obstacle
                // Remove the obstacle from the scene and the list
                root.getChildren().remove(obstacle.getShape());
                obstacles.remove(obstacle);

                // Decrement health points and update health points label
                damagePoints -= DAMAGE_VALUE; // Adjust damage value as needed
                damagePointsLabel.setText("Damage Points: " + damagePoints);
            }
        }
    }

    public static int Cnt=0;

    private void generateEnemy(Random random, Image enemyImage) {
        // Define an offset value to position enemies above the road segments
        double yOffset = 200; // Adjust this value as needed
        double maxY = (SCENE_HEIGHT-50) - yOffset; // Max height for enemy generation

        // Create a timeline to continuously generate enemies
        Timeline enemyTimeline = new Timeline(
                new KeyFrame(Duration.seconds(8), event -> { // Adjust the duration as needed
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
        Duration pauseDuration = Duration.seconds(10); // Adjust the pause duration as needed
        enemyTimeline.setDelay(pauseDuration);

        // Set the cycle count to INDEFINITE so the timeline continues indefinitely
        enemyTimeline.setCycleCount(Timeline.INDEFINITE);

        // Start the timeline
        enemyTimeline.play();
    }



    private void moveEnemy(ImageView enemy) {
        TranslateTransition enemyTransition = new TranslateTransition(Duration.seconds(5), enemy);

        // Calculate the distance to move the enemy across the screen
        double distance = SCENE_WIDTH + enemy.getBoundsInLocal().getWidth();

        // Move the enemy from right to left
        enemyTransition.setFromX(SCENE_WIDTH);
        enemyTransition.setToX(-distance);

        // Remove the enemy from the root and enemies list when the transition finishes
        enemyTransition.setOnFinished(event -> {
            root.getChildren().remove(enemy);
            enemies.remove(enemy);
        });

        // Start the enemy transition
        enemyTransition.play();
    }

    private void checkEnemyCollisions() {
        // Calculate the bounds for the lower quarter of the hero's image
        double heroLowerY = heroView.getLayoutY() + HERO_HEIGHT * 0;
        double heroLowerHeight = HERO_HEIGHT * 1;

        // Check if the down key is pressed
        if (isDownKeyPressed||((isJumping==true))&&isDownKeyPressed) {
            // Iterate through each enemy
            for (ImageView enemy : new ArrayList<>(enemies)) {
                // Check if the lower quarter of the hero's image intersects with the enemy
                if (enemy.getBoundsInParent().intersects(heroView.getLayoutX(), heroLowerY, HERO_WIDTH, heroLowerHeight)) {
                    // Collision detected between hero and enemy
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

    public static void main(String[] args) {
        launch(args);
    }
}