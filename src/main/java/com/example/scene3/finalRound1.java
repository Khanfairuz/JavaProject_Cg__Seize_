/*package com.example.scene3;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class finalRound1 extends Application {

    private static final int SCENE_WIDTH = 800;
    private static final int SCENE_HEIGHT = 600;
    private static final int SCROLL_SPEED = 4;
    private  static  final  int MONSTER_SCROLL_SPEED=2;
    private static final int ROAD_HEIGHT = 40;
    private static final int ROAD_Y = SCENE_HEIGHT - ROAD_HEIGHT;
    private static final int HERO_WIDTH = 200; // Adjust width as needed
    private static final int HERO_HEIGHT = 200; // Adjust height as needed

    private  static  final int  MONSTER_WIDTH=250;
    private  static  final int  MONSTER_HEIGHT=250;
    private static final int HERO_Y = ROAD_Y - HERO_HEIGHT; // Adjust Y position as needed
    private  static  final  int MONSTER_Y=ROAD_Y-MONSTER_HEIGHT;
    private static  int HERO_ANIMATION_INTERVAL = 10; // Interval between hero image changes in frames
    private  static int MONSTER_ANIMATION_INTERVAL=5;

    private static final int COIN_WIDTH = 50; // Adjust the width of the coin as needed
    private static final int COIN_HEIGHT = 50; // Adjust the height of the coin as needed

    private Pane root;

    //
    private  Image[] Monster;
    //
    private ImageView heroView;
    private  ImageView monsterView;
    private Image[] heroImages;
    private int currentHeroImageIndex = 0;
    private  int  currentMonsterIndex=0;
    private int frameCount = 0;
    private  int frameCountM=0;

    private List<ImageView> coins = new ArrayList<>();
    private int points = 0;
    private javafx.scene.text.Text pointsLabel;
    // Define a separate label for bonus points
    private javafx.scene.text.Text bonusPointsLabel;
    private javafx.scene.text.Text questionLabel;
    //////////////////////////////////
    public  boolean track_zombie_kill=false;
    public  boolean track_hero_kill=true;
    ////////////////////

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
    //Question
    //QUESTION AND ANSWER
    private database_connection dc;
    private  String question;
    private  String optionA;
    private  String optionB;
    private  String optionC;
    private  String optionD;
    private  String correctAns;
    private long startTime;
    private  boolean  oneKill=false;
    private  boolean stopMonster=false;
    private  boolean stopHero=false;
    private  boolean  flag_hero_gone=false;
    private  AnimationTimer timer;
    ////PLay music
     public  audio_play audio=new audio_play();

    //question show
    @FXML
    private Label label1;
    @FXML
    private CheckBox A;
    @FXML
    private CheckBox B;
    @FXML
    private CheckBox C;
    @FXML
    private CheckBox D;


    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        // Load background image
        ImageView backgroundView1 = new ImageView("alien.jpg");
        ImageView backgroundView2 = new ImageView("alien.jpg");


        // Set initial positions of background views
        backgroundView1.setLayoutX(0);
        backgroundView2.setLayoutX(backgroundView1.getImage().getWidth());


        // Create a Pane to hold the background image views
        root = new Pane(backgroundView1, backgroundView2);
        //FileInputStream coinFileStream = new FileInputStream("C:\\Users\\HP\\Music\\coin.png");
        coinImage = new Image(getClass().getResource("/Gold/coin.png").toExternalForm()); // Initialize the coinImage variable
        audio.play_music_zombie(root);
        //load monster image
        Monster = new Image[4];
        for (int i = 0; i < Monster.length; i++) {
            // FileInputStream inputStream = new FileInputStream("C:\\Users\\HP\\Music\\run3\\run3-0" + i + ".png");
            Monster[i] = new Image(getClass().getResource("/monster1walk/monster1walk-" + i + ".png").toExternalForm());
        }
        // Create ImageView for hero and set initial position
        monsterView = new ImageView(Monster[0]);
        monsterView.setFitWidth(MONSTER_WIDTH);
        monsterView.setFitHeight(MONSTER_HEIGHT);
        monsterView.setLayoutX(Screen.getPrimary().getVisualBounds().getWidth());
        // Get the last rectangle from the combined list of road segments
        monsterView.setLayoutY(MONSTER_Y);
        root.getChildren().add(monsterView);

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



        // generateEnemy(roadSegmentList,  parallelRoadSegmentList1,  parallelRoadSegmentList2, random,enemyImage);

        // Load hero images
        heroImages = new Image[6];
        for (int i = 0; i < heroImages.length; i++) {
            // FileInputStream inputStream = new FileInputStream("C:\\Users\\HP\\Music\\run3\\run3-0" + i + ".png");
            heroImages[i] = new Image(getClass().getResource("/Run/run3-0" + i + ".png").toExternalForm());
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






        //load monster image
        Monster = new Image[4];
        for (int i = 0; i < Monster.length; i++) {
            // FileInputStream inputStream = new FileInputStream("C:\\Users\\HP\\Music\\run3\\run3-0" + i + ".png");
            Monster[i] = new Image(getClass().getResource("/monster1walk/monster1walk-" + i + ".png").toExternalForm());
        }
        // Create ImageView for hero and set initial position
        monsterView = new ImageView(Monster[0]);
        monsterView.setFitWidth(MONSTER_WIDTH);
        monsterView.setFitHeight(MONSTER_HEIGHT);
        monsterView.setLayoutX(Screen.getPrimary().getVisualBounds().getWidth());

            monsterView.setLayoutY(MONSTER_Y);


        root.getChildren().add(monsterView);
        startTime = System.nanoTime();

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
                // Update monster position
                monsterView.setLayoutX(monsterView.getLayoutX() - MONSTER_SCROLL_SPEED);
                // Animate hero
                frameCount++;


                if (frameCount >= HERO_ANIMATION_INTERVAL &&stopHero==false) {
                    frameCount = 0;
                    currentHeroImageIndex = (currentHeroImageIndex + 1) % heroImages.length;
                    heroView.setImage(heroImages[currentHeroImageIndex]);
                    if(oneKill==true && currentHeroImageIndex==5 &&   flag_hero_gone==true )
                    {
                        heroView.setVisible(false);
                        stopHero=true;
                    }
                }

                frameCountM++;
                if (frameCountM >= MONSTER_ANIMATION_INTERVAL && stopMonster==false) {
                    frameCountM = 0;
                    currentMonsterIndex= (currentMonsterIndex+ 1) % Monster.length;
                    monsterView.setImage(Monster[currentMonsterIndex]);
                    if(oneKill==true && currentMonsterIndex==3 )
                    {
                        monsterView.setVisible(false);
                        stopMonster=true;
                    }

                }

                // Check if any background view is completely out of the scene, then reposition it
                if (backgroundView1.getLayoutX() <= -backgroundView1.getImage().getWidth()) {
                    backgroundView1.setLayoutX(backgroundView2.getLayoutX() + backgroundView2.getImage().getWidth());
                }
                if (backgroundView2.getLayoutX() <= -backgroundView2.getImage().getWidth()) {
                    backgroundView2.setLayoutX(backgroundView1.getLayoutX() + backgroundView1.getImage().getWidth());
                }

                // Calculate the elapsed time in seconds
                long elapsedTimeNano = now - startTime;
                double elapsedTimeSeconds = elapsedTimeNano / 1_000_000_000.0;

                // Update the label with the elapsed time
               // System.out.println("Elapsed Time: %.1f seconds "+elapsedTimeSeconds);
                //time pore chenge kora jabe
                if(elapsedTimeSeconds>10.5 && elapsedTimeSeconds<11 && oneKill==false)
                {
                    oneKill=true;
                     if(track_zombie_kill==true)
                     {
                         //
                         for (int i = 0; i < Monster.length; i++) {
                             // FileInputStream inputStream = new FileInputStream("C:\\Users\\HP\\Music\\run3\\run3-0" + i + ".png");
                             Monster[i] = new Image(getClass().getResource("/monster1kill/monster1kill-" + i + ".png").toExternalForm());

                         }
                         frameCountM=0;
                         currentMonsterIndex=0;
                         MONSTER_ANIMATION_INTERVAL=12;
                     }
                     else
                     {
                          flag_hero_gone=true;
                         //
                         for (int i = 0; i < Monster.length; i++) {
                             // FileInputStream inputStream = new FileInputStream("C:\\Users\\HP\\Music\\run3\\run3-0" + i + ".png");
                             Monster[i] = new Image(getClass().getResource("/monster1killhero/monster1killhero-" + i + ".png").toExternalForm());

                         }
                         frameCountM=0;
                         currentMonsterIndex=0;
                         MONSTER_ANIMATION_INTERVAL=8;


                         for (int i = 0; i < heroImages.length; i++) {
                             // FileInputStream inputStream = new FileInputStream("C:\\Users\\HP\\Music\\run3\\run3-0" + i + ".png");
                             heroImages[i] = new Image(getClass().getResource("/hero1kill/hero1kill-" + i + ".png").toExternalForm());
                         }
                         frameCount=0;
                         currentHeroImageIndex=0;


                     }

                }

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
                // jump();
            }
            if (scene != null) {
                if (event.getCode() == KeyCode.UP) {
                    // jump();
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



        connect_database();
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

        //Question
        questionLabel=new javafx.scene.text.Text(question);
        questionLabel.setFill(Color.WHITE);
        questionLabel.setStyle("-fx-font-size: 40px; -fx-text-fill: white;");
        questionLabel.setX(500);
        questionLabel.setY((50+ questionLabel.getLayoutBounds().getHeight()) / 2);


        // Set the width and height of the root pane based on text dimensions
        root.setPrefWidth(questionLabel.getLayoutBounds().getWidth());
        root.setPrefHeight(questionLabel.getLayoutBounds().getHeight());
        root.getChildren().add(questionLabel);
        // checkBox
        // Create an HBox to hold the checkboxes
        VBox checkBoxes = new VBox();
        checkBoxes.setAlignment(Pos.TOP_LEFT);
        checkBoxes.setSpacing(10); // Set spacing between checkboxes

        checkBoxes.setLayoutX(500); // Position relative to the question label
        checkBoxes.setLayoutY(questionLabel.getY() + questionLabel.getBoundsInLocal().getHeight() + 20); // Position below the question label

        // Create four checkboxes
        CheckBox checkBoxA = new CheckBox(optionA);
        CheckBox checkBoxB = new CheckBox(optionB);
        CheckBox checkBoxC = new CheckBox(optionC);
        CheckBox checkBoxD = new CheckBox(optionD);
        // Apply font style to checkboxes
        checkBoxA.setStyle("-fx-font-family: Arial; -fx-font-size: 25px; -fx-text-fill: white;");
        checkBoxB.setStyle("-fx-font-family: Arial; -fx-font-size: 25px; -fx-text-fill: white;");
        checkBoxC.setStyle("-fx-font-family: Arial; -fx-font-size: 25px; -fx-text-fill: white;");
        checkBoxD.setStyle("-fx-font-family: Arial; -fx-font-size: 25px; -fx-text-fill: white;");
        // Add the checkboxes to the HBox
        checkBoxes.getChildren().addAll(checkBoxA, checkBoxB, checkBoxC, checkBoxD);

        // Add the HBox to the root pane
        root.getChildren().add(checkBoxes);

        checkBoxA.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                // Invoke function when checkBoxA is selected
                checkA();
                System.out.println("HLLLLL");
            } else {
                // Optionally handle when checkBoxA is deselected

            }
        });
        checkBoxB.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                // Invoke function when checkBoxA is selected
                checkB();
            } else {
                // Optionally handle when checkBoxA is deselected

            }
        });
        checkBoxC.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                // Invoke function when checkBoxA is selected
                checkC();
            } else {
                // Optionally handle when checkBoxA is deselected

            }
        });
        checkBoxD.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                // Invoke function when checkBoxA is selected
                checkD();
            } else {
                // Optionally handle when checkBoxA is deselected

            }
        });

        // Set the stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("Scrolling Background with Continuous Road and Animated Hero");
        primaryStage.show();
        primaryStage.setFullScreen(true);
    }
    public  void connect_database()
    {
        dc = new database_connection();
        dc.connection("objectorientedprogramming" ,finalRound1.this);
        System.out.println("Correct ANS :"+correctAns);


    }
    //databse......
    public void setQuestion(String question) {
        this.question = question;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public void setCorrectAns(String correctAns) {
        this.correctAns = correctAns;
    }

    @FXML
    public  void  checkA()
    {
        if(correctAns.equals("A"))
        {
           track_zombie_kill=true;
        }
        else
        {
            track_hero_kill=true;
        }
        System.out.println("Checked A");

    }
    @FXML
    public  void  checkB()
    {
        if(correctAns.equals("B"))
        {
            track_zombie_kill=true;
        }
        else
        {
            track_hero_kill=true;
        }
        System.out.println("Checked B");

    }
    @FXML
    public  void  checkC()
    {
        if(correctAns.equals("C"))
        {
            track_zombie_kill=true;
        }
        else
        {
            track_hero_kill=true;
        }
        System.out.println("Checked C");
    }
    @FXML
    public  void  checkD()
    {
        if(correctAns.equals("D"))
        {
            track_zombie_kill=true;
        }
        else
        {
            track_hero_kill=true;
        }
        System.out.println("Checked D");
    }


    public static void main(String[] args) {
        launch(args);
    }
}*/