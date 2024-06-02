package com.example.scene3;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.io.FileNotFoundException;
import java.util.Arrays;
public class HelloController3  {
    public static final int SCENE_WIDTH = 800;
    private static final int SCENE_HEIGHT = 600;
    private static final int SCROLL_SPEED = 8;
    private static final int ROAD_HEIGHT = 40;
    public static final int ROAD_Y = SCENE_HEIGHT - ROAD_HEIGHT-30;
    private static final int HERO_HEIGHT = 200; // heror height

    boolean isObstacleGen=false;
    boolean isBigObstacleGen=false;
    public static boolean isTimerRunning=true;


    private int frameCount = 0;
    public static int points = 0;
    private int bonusPoints = 0;
    private int damagePoints = 0; // Initialize damage points
    public static javafx.scene.text.Text pointsLabel;
    // Define a separate label for bonus points
    private javafx.scene.text.Text bonusPointsLabel;
    private javafx.scene.text.Text damagePointsLabel;

    private Image coinImage;

    public static Obstacle obstacles = new Obstacle(20,20, "purpleObs");
    public static Coin coin=new Coin();
    public static Coin coinAfterMId=new Coin();
    public static Mid mid=new Mid();
    public Enemyquiz quiz;
    public static QuizObstacle quizObstacle=new QuizObstacle();
    public static AnimationTimer timer;
    public  static  AnimationTimer  timer1;
    public static BigObstacle bigObstacle=new BigObstacle(60, 20, "BigObs3");

    private  int frameCountM=0;
    public  static    Pane root2 =new Pane();
    private Hero hero=new Hero(root2);
    private  static  final int  MONSTER_HEIGHT=250;
    private static final int HERO_Y = ROAD_Y - HERO_HEIGHT; // Adjust Y position as needed
    private  static  final  int MONSTER_Y=ROAD_Y-MONSTER_HEIGHT;
    private static  int HERO_ANIMATION_INTERVAL = 10; // Interval between hero image changes in frames
    private  static int MONSTER_ANIMATION_INTERVAL=5;
    private  static  final  int MONSTER_SCROLL_SPEED=2;
    public  boolean oneTimeGenerate=true;
    public  boolean thirdlevel=false;
    public  double elapsedTimeSeconds_check=0;
    private javafx.scene.text.Text questionLabel;
    //////////////////////////////////
    public  boolean track_zombie_kill=false;
    public  boolean track_hero_kill=true;
    private database_connection dc;
    ////////////////////
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
    ////PLay music
    public  audio_play audio=new audio_play();
    public static Monster monster=new Monster();
    public Fire fire=new Fire();
    // Wrapping points in an array to avoid final/effectively final issue
    final int[] bonusPointsWrapper = new int[1];
    final int[] damagePointsWrapper = new int[1];
    private HeroLose hr=new HeroLose();

    private  HeroWin hrW=new HeroWin();
    private  Stage primaryStage;


    public void start_new_3(Stage primaryStage ,int points , int bonusPoints , int damagePoints) throws FileNotFoundException {
        this.primaryStage=primaryStage;
        // Load background image
        this.bonusPoints=bonusPoints;
        this.points=points;
        this.damagePoints=damagePoints;
        ImageView backgroundView1 = new ImageView("Back3.png");
        ImageView backgroundView2 = new ImageView("Back3.png");

        backgroundView1.setLayoutX(0);
        backgroundView2.setLayoutX(backgroundView1.getImage().getWidth());

        // Create a Pane to hold everything
        root2.getChildren().addAll(backgroundView1, backgroundView2);
        //audio
        audio.play_music_normal(root2);

        // FileInputStream coinFileStream = new FileInputStream("C:\\Users\\HP\\Music\\coin.png");
        coinImage = new Image(getClass().getResource("/Gold/coin.png").toExternalForm()); // Initialize the coinImage variable

        //monster add starts


        Image[] heroAttackImages = new Image[4];
        for (int i = 0; i < heroAttackImages.length; i++) {
            //FileInputStream inputStream = new FileInputStream("/sword/adventurer-attack1-0" + (i + 1) + ".png");
            heroAttackImages[i] = new Image(getClass().getResource("/sword/adventurer-attack1-0" + (i + 1) + ".png").toExternalForm());
        }
        // Create road segments
        Rectangle[] roadSegments = new Rectangle[4];
        Rectangle[] parallelRoadSegments1 = new Rectangle[4];
        Rectangle[] parallelRoadSegments2 = new Rectangle[4];
        for (int i = 0; i < roadSegments.length; i++) {
            roadSegments[i] = new Rectangle(SCENE_WIDTH, ROAD_HEIGHT, Color.GOLD);
            roadSegments[i].setLayoutY(ROAD_Y);
            roadSegments[i].setLayoutX(i * SCENE_WIDTH);
            root2.getChildren().add(roadSegments[i]);

            parallelRoadSegments1[i] = new Rectangle(SCENE_WIDTH, ROAD_HEIGHT, Color.BLUEVIOLET);
            parallelRoadSegments1[i].setLayoutY(ROAD_Y - ROAD_HEIGHT - 20);
            parallelRoadSegments1[i].setLayoutX(i * SCENE_WIDTH);
            root2.getChildren().add(parallelRoadSegments1[i]);

            parallelRoadSegments2[i] = new Rectangle(SCENE_WIDTH, ROAD_HEIGHT, Color.BLUEVIOLET);
            parallelRoadSegments2[i].setLayoutY(ROAD_Y + ROAD_HEIGHT + 20);
            parallelRoadSegments2[i].setLayoutX(i * SCENE_WIDTH);
            root2.getChildren().add(parallelRoadSegments2[i]);
        }

        List<Rectangle> roadSegmentList = new ArrayList<>(Arrays.asList(roadSegments));
        List<Rectangle> parallelRoadSegmentList1 = new ArrayList<>(Arrays.asList(parallelRoadSegments1));
        List<Rectangle> parallelRoadSegmentList2 = new ArrayList<>(Arrays.asList(parallelRoadSegments2));
        Random random = new Random();
        coin.generateCoins(root2, roadSegmentList, parallelRoadSegmentList1, parallelRoadSegmentList2, random);
        quiz=new Enemyquiz(root2, hero, bonusPoints);
        quiz.generateEnemy();
        //generateCoins(roadSegmentList, parallelRoadSegmentList1, parallelRoadSegmentList2, random, coinImage);
        // obstacles.generateCoins(root, roadSegmentList, parallelRoadSegmentList1, parallelRoadSegmentList2, random);

        root2.getChildren().add(hero.heroView);
        //hero.heroView.toFront();
        //startTime = System.nanoTime();

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

                // Update monster position
                // monster.monsterView.setLayoutX(monster.monsterView.getLayoutX() - MONSTER_SCROLL_SPEED);

                // Animate hero
                frameCount++;
                frameCount = hero.frameHero(frameCount, stopHero, oneKill, flag_hero_gone);

                // Check if any background view is completely out of the scene, then reposition it
                if (backgroundView1.getLayoutX() <= -backgroundView1.getImage().getWidth()) {
                    backgroundView1.setLayoutX(backgroundView2.getLayoutX() + backgroundView2.getImage().getWidth());
                }
                if (backgroundView2.getLayoutX() <= -backgroundView2.getImage().getWidth()) {
                    backgroundView2.setLayoutX(backgroundView1.getLayoutX() + backgroundView1.getImage().getWidth());
                }

                // Check for coin collisions
                // checkCoinCollisions();
                coin.checkCoinCollisions2(root2, hero.heroView);
                quiz.checkEnemyCollisions(hero.isDownKeyPressed, bonusPointsLabel);
                if (coin.isCoinFinished && quiz.isEnemyFinished) {
                    System.out.println("@@@@@@@@@@@@@@@@@@");
                    obstacles.generateCoins(root2, roadSegmentList, parallelRoadSegmentList1, parallelRoadSegmentList2, random);
                    coin.isCoinFinished = false;
                    isObstacleGen = true;
                }
                obstacles.checkObstacleCollisions(root2, hero.heroView, hero.isJumping);
                if (obstacles.isObstacleFinished) {
                    bigObstacle.generateBigCoins(root2, roadSegmentList, parallelRoadSegmentList1, parallelRoadSegmentList2, random);
                    //mid.generateMid(root, roadSegmentList, parallelRoadSegmentList1, parallelRoadSegmentList2, random);
                    obstacles.isObstacleFinished = false;
                    isBigObstacleGen = true;
                }
                bigObstacle.checkBigObstacleCollisions2(root2, hero.heroView, hero.isJumping , HelloController3.this);

                //if(!isTimerRunning) {return;}
                if (bigObstacle.isBigObstacleFinished && isTimerRunning) {
                    System.out.println("Why");
                    mid.generateMid(root2, roadSegmentList, parallelRoadSegmentList1, parallelRoadSegmentList2, random);
                    bigObstacle.isBigObstacleFinished = false;
                }

                mid.checkCoinCollisions(root2, hero.heroView,roadSegmentList,parallelRoadSegmentList1,parallelRoadSegmentList2, isTimerRunning);
                if (mid.isMidFinished && isTimerRunning) {
                    System.out.println("fairuz");
                    quizObstacle.generateObstacles(root2, roadSegmentList, parallelRoadSegmentList1, parallelRoadSegmentList2, random);
                    //coin.generateCoins(root, roadSegmentList, parallelRoadSegmentList1, parallelRoadSegmentList2, random);
                    coinAfterMId.generateCoins(root2, roadSegmentList, parallelRoadSegmentList1, parallelRoadSegmentList2, random);
                    fire.generateMid(root2, roadSegmentList, parallelRoadSegmentList1, parallelRoadSegmentList2, random);
                    mid.isMidFinished = false;
                    quizObstacle.isQuizObstacleFinished=false;
                }
                quizObstacle.checkObstacleCollisions(root2, hero.heroView, damagePointsLabel, isTimerRunning);
                coinAfterMId.checkCoinCollisions2(root2, hero.heroView);
                fire.checkCoinCollisions(root2, hero.heroView , HelloController3.this);
                System.out.println(isTimerRunning);
                //coin.checkCoinCollisions(root, hero.heroView);

                if (quizObstacle.isQuizObstacleFinished) {
                    System.out.println("finished");
                    if (oneTimeGenerate) {
                        audio.stopMusic_normal();
                        audio.play_music_zombie(root2);
                        monster.generateMonster(root2,3);
                        quizObstacle.isQuizObstacleFinished = false;
                        checkQues();
                        oneTimeGenerate = false;
                        thirdlevel=true;
                        startTimer();
                        damagePointsLabel.setVisible(false);
                    }
                }
                if(thirdlevel==true){


                    frameCountM++;
                    monster.HeroKillMonster(frameCountM, stopMonster, oneKill);
                    // Calculate the elapsed time in seconds


                    // Update the label with the elapsed time
                    // System.out.println("Elapsed Time: %.1f seconds "+elapsedTimeSeconds);
                    //time pore chenge kora jabe
                    if (elapsedTimeSeconds_check > 7.5 && elapsedTimeSeconds_check< 7.7 && oneKill == false) {
                        oneKill = true;
                        if (track_zombie_kill == true) {
                            //
                            for (int i = 0; i < monster.Monster.length; i++) {
                                // FileInputStream inputStream = new FileInputStream("C:\\Users\\HP\\Music\\run3\\run3-0" + i + ".png");
                                monster.Monster[i] = new Image(getClass().getResource("/monster3kill/monster3kill-" + i + ".png").toExternalForm());

                            }
                            frameCountM = 0;
                            monster.currentMonsterIndex = 0;
                            MONSTER_ANIMATION_INTERVAL = 12;
                        } else {
                            flag_hero_gone = true;
                            //
                            for (int i = 0; i < monster.Monster.length; i++) {
                                // FileInputStream inputStream = new FileInputStream("C:\\Users\\HP\\Music\\run3\\run3-0" + i + ".png");
                                monster.Monster[i] = new Image(getClass().getResource("/monster3killhero/monster3killhero-" + i + ".png").toExternalForm());

                            }
                            frameCountM = 0;
                            monster.currentMonsterIndex = 0;
                            MONSTER_ANIMATION_INTERVAL = 8;


                            for (int i = 0; i < hero.heroImages.length; i++) {
                                // FileInputStream inputStream = new FileInputStream("C:\\Users\\HP\\Music\\run3\\run3-0" + i + ".png");
                                hero.heroImages[i] = new Image(getClass().getResource("/hero1kill/hero1kill-" + i + ".png").toExternalForm());
                            }
                            frameCount = 0;
                            hero.currentHeroImageIndex = 0;


                        }


                    }

                    //new page load
                    if(elapsedTimeSeconds_check>12.7&& elapsedTimeSeconds_check<13)
                    {

                            timer.stop();
                            timer1.stop();

                            audio.stopMusic_zombie();
                            call_hero_win();

                            // Use the wrapper values
                    }
                    //quizObstacle.generateObstacles(root, roadSegmentList, parallelRoadSegmentList1, parallelRoadSegmentList2, random);

                    // }
                }



            }
        };
        timer.start();

        // Set up keyboard input handling
        Scene scene = new Scene(root2, SCENE_WIDTH, SCENE_HEIGHT);
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
//        scene.setOnKeyReleased(event -> {
//            if (event.getCode() == KeyCode.DOWN) {
//                hero.isDownKeyPressed = false;
//            }
//        });

        connect_database();
        pointsLabel = new javafx.scene.text.Text("Points: " + points);
        pointsLabel.setFill(Color.WHITE);
        pointsLabel.setStyle("-fx-font-size: 55px; -fx-text-fill: white;");
        pointsLabel.setLayoutX(20);
        pointsLabel.setLayoutY(50);
        root2.getChildren().add(pointsLabel);

        //mrim
        // Add this code in the start method after initializing the points label
        bonusPointsLabel = new javafx.scene.text.Text("Bonus Points: 0");
        bonusPointsLabel.setFill(Color.WHITE);
        bonusPointsLabel.setStyle("-fx-font-size: 55px; -fx-text-fill: white;");
        bonusPointsLabel.setLayoutX(20);
        bonusPointsLabel.setLayoutY(120);
        root2.getChildren().add(bonusPointsLabel);
        // Add this code in the start method after initializing the points label
        damagePointsLabel = new javafx.scene.text.Text("Damage Points: 0");
        damagePointsLabel.setFill(Color.WHITE);
        damagePointsLabel.setStyle("-fx-font-size: 55px; -fx-text-fill: white;");
        damagePointsLabel.setLayoutX(SCENE_WIDTH );
        damagePointsLabel.setLayoutY(50);
        root2.getChildren().add(damagePointsLabel);




        // Set the stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("Scrolling Background with Continuous Road and Animated Hero");
        primaryStage.show();
        primaryStage.setFullScreen(true);
    }
    void  call_hero_lose()
    {
        //if clash , then game over and new page will be loaded


        calculate_data();
        hr.hero_lose_start(primaryStage , points , bonusPoints , damagePoints);


    }

    void call_hero_win()
    {
        calculate_data();
        hrW.hero_win_start(primaryStage, points , bonusPoints , damagePoints);

    }
    private  void calculate_data()
    {
        try {
            // Extract numeric part from bonusPointsLabel
            String bonusText = bonusPointsLabel.getText().replaceAll("[^0-9]", "");
            bonusPointsWrapper[0] = Integer.parseInt(bonusText);

            // Extract numeric part from damagePointsLabel
            String damageText = damagePointsLabel.getText().replaceAll("[^0-9]", "");
            damagePointsWrapper[0] = Integer.parseInt(damageText);
            if(thirdlevel==true && track_zombie_kill)
            {
                points+=100;
            }

            // Proceed with the rest of your logic
        } catch (NumberFormatException e) {
            System.err.println("Error converting label text to integer: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public void checkQues()
    {
        questionLabel=new javafx.scene.text.Text(question);
        questionLabel.setFill(Color.WHITE);
        questionLabel.setStyle("-fx-font-size: 40px; -fx-text-fill: white;");
        questionLabel.setX(500);
        questionLabel.setY((50+ questionLabel.getLayoutBounds().getHeight()) / 2);


        // Set the width and height of the root pane based on text dimensions
        root2.setPrefWidth(questionLabel.getLayoutBounds().getWidth());
        root2.setPrefHeight(questionLabel.getLayoutBounds().getHeight());
        root2.getChildren().add(questionLabel);
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
        root2.getChildren().add(checkBoxes);

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
    }
    public void startTimer() {
        startTime = System.nanoTime();

        timer1 = new AnimationTimer() {
            @Override
            public void handle(long now) {
                long elapsedTimeNano = now - startTime;
                double elapsedTimeMillis = elapsedTimeNano / 1_000_000_000.0;
                elapsedTimeSeconds_check=elapsedTimeMillis;
                // Print elapsed time
                System.out.printf("Elapsed Time 2nd : %.1f milliseconds\n", elapsedTimeMillis);

                // Example condition: print message between 10.5 and 11 seconds
                if (elapsedTimeMillis >7200 && elapsedTimeMillis <7300) {

                    System.out.println("Elapsed Time is between 10.5 and 11 seconds");
                }
            }
        };

        timer1.start();
    }

    public  void connect_database()
    {
        dc = new database_connection();
        dc.connection3("dld" ,HelloController3.this);
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



}






