package com.example.scene3;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
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
public class HelloController extends Application {
    public static final int SCENE_WIDTH = 800;
    private static final int SCENE_HEIGHT = 600;
    private static final int SCROLL_SPEED = 4;
    private static final int ROAD_HEIGHT = 40;
    public static final int ROAD_Y = SCENE_HEIGHT - ROAD_HEIGHT-30;
    private static final int HERO_HEIGHT = 200; // heror height

    boolean isObstacleGen=false;
    boolean isBigObstacleGen=false;
    public static boolean isTimerRunning=true;

    public static Pane root=new Pane();
    private int frameCount = 0;
    public static int points = 0;
    private int bonusPoints = 0;
    private int damagePoints = 0; // Initialize damage points
    public static javafx.scene.text.Text pointsLabel;
    // Define a separate label for bonus points
    private javafx.scene.text.Text bonusPointsLabel;
    private javafx.scene.text.Text damagePointsLabel;
    private Random random = new Random();
    private Image coinImage;
    private Hero hero=new Hero(root);
    public static Obstacle obstacles = new Obstacle(20,20);
    public static Coin coin=new Coin();
    public static Mid mid=new Mid();
    public Enemyquiz quiz;
    public static QuizObstacle quizObstacle=new QuizObstacle();
    public static AnimationTimer timer;
    public static BigObstacle bigObstacle=new BigObstacle(60, 20);
    public boolean quizDown=true;  //quiz r mid er down key er jonno


    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        // Load background image
        ImageView backgroundView1 = new ImageView("alien.jpg");
        ImageView backgroundView2 = new ImageView("alien.jpg");

        backgroundView1.setLayoutX(0);
        backgroundView2.setLayoutX(backgroundView1.getImage().getWidth());

        // Create a Pane to hold everything
        root.getChildren().addAll(backgroundView1, backgroundView2);

        // FileInputStream coinFileStream = new FileInputStream("C:\\Users\\HP\\Music\\coin.png");
        coinImage = new Image(getClass().getResource("/Gold/coin.png").toExternalForm()); // Initialize the coinImage variable

        // Create road segments
        Rectangle[] roadSegments = new Rectangle[4];
        Rectangle[] parallelRoadSegments1 = new Rectangle[4];
        Rectangle[] parallelRoadSegments2 = new Rectangle[4];
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
        coin.generateCoins(root, roadSegmentList, parallelRoadSegmentList1, parallelRoadSegmentList2, random);
        quiz=new Enemyquiz(root, hero, bonusPoints);
        quiz.generateEnemy();
        //generateCoins(roadSegmentList, parallelRoadSegmentList1, parallelRoadSegmentList2, random, coinImage);
        // obstacles.generateCoins(root, roadSegmentList, parallelRoadSegmentList1, parallelRoadSegmentList2, random);

        root.getChildren().add(hero.heroView);
        //hero.heroView.toFront();

        // Set up animation timer to scroll the background, road, and animate the hero
        timer = new AnimationTimer() {
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
                frameCount=hero.frameHero(frameCount);

                // Check if any background view is completely out of the scene, then reposition it
                if (backgroundView1.getLayoutX() <= -backgroundView1.getImage().getWidth()) {
                    backgroundView1.setLayoutX(backgroundView2.getLayoutX() + backgroundView2.getImage().getWidth());
                }
                if (backgroundView2.getLayoutX() <= -backgroundView2.getImage().getWidth()) {
                    backgroundView2.setLayoutX(backgroundView1.getLayoutX() + backgroundView1.getImage().getWidth());
                }

                // Check for coin collisions
                // checkCoinCollisions();
                coin.checkCoinCollisions(root, hero.heroView);
                quiz.checkEnemyCollisions(hero.isDownKeyPressed, bonusPointsLabel);
                if(coin.isCoinFinished && quiz.isEnemyFinished)
                {
                    obstacles.generateCoins(root, roadSegmentList, parallelRoadSegmentList1, parallelRoadSegmentList2, random);
                    coin.isCoinFinished=false;
                    isObstacleGen=true;
                }
                obstacles.checkObstacleCollisions( root, hero.heroView, hero.isJumping);
                if(obstacles.isObstacleFinished)
                {
                    bigObstacle.generateBigCoins(root, roadSegmentList, parallelRoadSegmentList1, parallelRoadSegmentList2, random);
                    //mid.generateMid(root, roadSegmentList, parallelRoadSegmentList1, parallelRoadSegmentList2, random);
                    obstacles.isObstacleFinished=false;
                    isBigObstacleGen=true;
                }
                bigObstacle.checkBigObstacleCollisions(root, hero.heroView, hero.isJumping);

                //if(!isTimerRunning) {return;}
                if(bigObstacle.isBigObstacleFinished && isTimerRunning) {
                    System.out.println("Why");
                    mid.generateMid(root, roadSegmentList, parallelRoadSegmentList1, parallelRoadSegmentList2, random);
                    bigObstacle.isBigObstacleFinished=false;
                }

                mid.checkCoinCollisions(root,hero.heroView,roadSegmentList, parallelRoadSegmentList1, parallelRoadSegmentList2);
                //quizObstacle.generateObstacles(root, roadSegmentList, parallelRoadSegmentList1, parallelRoadSegmentList2, random);

                // }
            }
        };
        timer.start();

        // Set up keyboard input handling
        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.LEFT) {

                //new
                if(hero.heroView.getLayoutY()== Hero.HERO_Y)
                {
                    hero.heroView.setLayoutY(Hero.HERO_Y -ROAD_HEIGHT-20);
                }
                else if(hero.heroView.getLayoutY()== Hero.HERO_Y +ROAD_HEIGHT+20)
                {
                    hero.heroView.setLayoutY(Hero.HERO_Y);
                }

            } else if (event.getCode() == KeyCode.RIGHT) {

                if(hero.heroView.getLayoutY()==hero.HERO_Y)
                {
                    hero.heroView.setLayoutY(hero.HERO_Y+ROAD_HEIGHT+20);
                }
                else if(hero.heroView.getLayoutY()==hero.HERO_Y-ROAD_HEIGHT-20)
                {
                    hero.heroView.setLayoutY(hero.HERO_Y);
                }
            }
            if (event.getCode() == KeyCode.UP && !hero.isJumping) {
                //hero.jumpHero();
//                if(isObstacleGen)
//                {
//                    obstacles.checkObstacleCollisions( root, hero.heroView, (!hero.isJumping));
//                }
               // hero.jump(root);
                hero.quizjump();
                //hero.moveY((int)hero.heroVelocity.getY());
            }
            if((event.getCode() == KeyCode.DOWN) && isBigObstacleGen)
            {
                hero.ground();
            }
            //mrim
            if((event.getCode() == KeyCode.DOWN))// && quizDown)
            {
                hero.DownKeyQuiz();
            }
            if (scene != null) {
                if (event.getCode() == KeyCode.UP) {
                    // hero.jumpHero();
                    //hero.jump(root);
                    hero.quizjump();
                }
            }
        });


        pointsLabel = new javafx.scene.text.Text("Points: " + points);
        pointsLabel.setFill(Color.WHITE);
        pointsLabel.setStyle("-fx-font-size: 55px; -fx-text-fill: white;");
        pointsLabel.setLayoutX(20);
        pointsLabel.setLayoutY(50);
        root.getChildren().add(pointsLabel);

        //mrim
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


        primaryStage.setScene(scene);
        primaryStage.setTitle("Scrolling Background with Continuous Road and Animated Hero");
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}


