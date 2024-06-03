package com.example.scene3;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

public class HeroLose {
    private   startPage st=new startPage();

    private Text totalcgpa;
    private Text maxscore;
    private ImageView heroimageview;
    private  ImageView gifFail;

    private Image[] heroImages;
    public static final int HERO_WIDTH = 150;
    public static final int HERO_HEIGHT = 150;
    private static final int HERO_ANIMATION_INTERVAL = 10;
    public int currentHeroImageIndex = 0;
    private AnimationTimer timer;
    private int frameCount = 0;
    private cgpaCalculation cgpaC = new cgpaCalculation();
    private Stage stage;
    private database_connection db1 = new database_connection();
    private database_connection db2 = new database_connection();
    private double maxsr;
    private float cgpar;

    public void hero_lose_start(Stage stage, int points, int bonusPoints, int damagePoints) {

        this.stage = stage;
        Image image = new Image(getClass().getResource("/Fail_gif.jpg").toExternalForm());
        gifFail=new ImageView(image);
        gifFail.setPreserveRatio(true);

        // Create UI components
        AnchorPane root = new AnchorPane(gifFail);

        Text totalPointsText = new Text("Total Points");
        totalPointsText.setLayoutX(14.0);
        totalPointsText.setLayoutY(65.0);
        totalPointsText.setFont(Font.font("System", FontWeight.BOLD, 25.0));
        totalPointsText.setFill(Color.WHITE);

        Text total_point = new Text();
        total_point.setLayoutX(158.0);
        total_point.setLayoutY(65.0); // Adjusted to align with the label
        total_point.setFont(Font.font("System", FontWeight.BOLD, 25.0));
        total_point.setFill(Color.WHITE);

        Text totalCgpaText = new Text("Total Cgpa");
        totalCgpaText.setLayoutX(14.0);
        totalCgpaText.setLayoutY(150.0);
        totalCgpaText.setFont(Font.font("System", FontWeight.BOLD, 25.0));
        totalCgpaText.setFill(Color.WHITE);

        totalcgpa = new Text();
        totalcgpa.setLayoutX(150.0);
        totalcgpa.setLayoutY(150.0); // Adjusted to align with the label
        totalcgpa.setFont(Font.font("System", FontWeight.BOLD, 25.0));
        totalcgpa.setFill(Color.WHITE);

        Text maxScoreText = new Text("Max Score");
        maxScoreText.setLayoutX(580.0);
        maxScoreText.setLayoutY(65.0);
        maxScoreText.setFont(Font.font("System", FontWeight.BOLD, 25.0));
        maxScoreText.setFill(Color.WHITE);

        maxscore = new Text();
        maxscore.setLayoutX(710.0);
        maxscore.setLayoutY(65.0); // Adjusted to align with the label
        maxscore.setFont(Font.font("System", FontWeight.BOLD, 25.0));
        maxscore.setFill(Color.WHITE);


        heroimageview = new ImageView();
        heroimageview.setLayoutX(68.0);
        heroimageview.setLayoutY(340.0);
        heroimageview.setFitHeight(203.0);
        heroimageview.setFitWidth(209.0);
        heroimageview.setPreserveRatio(true);




        Button exitButton = new Button("Exit");
        exitButton.setLayoutX(650.0);
        exitButton.setLayoutY(500.0);
        exitButton.setPrefHeight(50.0);
        exitButton.setPrefWidth(107.0);
        exitButton.setFont(new Font(28.0));
        exitButton.setStyle("-fx-background-color: violet; -fx-text-fill: navy;");
        exitButton.setOnAction(e -> exit());



        // Add components to the root
        root.getChildren().addAll(totalPointsText, total_point, totalCgpaText, totalcgpa, maxScoreText, maxscore, heroimageview, exitButton);

        // Create the scene with the root node
        Scene scene = new Scene(root, 800,600);

        // Set the scene to the stage
        stage.setScene(scene);

        // Set the title of the stage
        stage.setTitle("You Lose");

        // Show the stage
        stage.show();
        centerStage(stage, scene);
        // Initialize animation
        initializeAnimation();

        // Set the values
        int total = points + (int)(damagePoints*.02);
        total_point.setText(String.valueOf(total));

        cgpar = cgpaC.calculate_cgpa(points, bonusPoints, damagePoints);
        totalcgpa.setText(String.valueOf(cgpar));
        getMaxScore();

        if (maxsr < cgpar) {
            maxsr = cgpar;
            db2.updateMaxScore(maxsr);
        }
        maxscore.setText(String.valueOf(maxsr));


    }

    private void initializeAnimation() {
        heroImages = new Image[6];
        for (int i = 0; i < heroImages.length; i++) {
            heroImages[i] = new Image(getClass().getResource("/Run/run3-0" + i + ".png").toExternalForm());
        }

        heroimageview.setImage(heroImages[0]);
        heroimageview.setFitWidth(HERO_WIDTH);
        heroimageview.setFitHeight(HERO_HEIGHT);

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                frameCount++;
                if (frameCount >= HERO_ANIMATION_INTERVAL) {
                    frameCount = 0;
                    currentHeroImageIndex = (currentHeroImageIndex + 1) % heroImages.length;
                    heroimageview.setImage(heroImages[currentHeroImageIndex]);
                }
            }
        };
        timer.start();
    }
    private void centerStage(Stage stage, Scene scene) {
        // Get the screen bounds
        javafx.geometry.Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        // Calculate the center position
        double x = (screenBounds.getWidth() - scene.getWidth()) / 2;
        double y = (screenBounds.getHeight() - scene.getHeight()) / 2;

        // Adjust y to keep some distance from the top and bottom of the screen
        double verticalMargin = 50.0;
        y = Math.max(verticalMargin, y);
        y = Math.min(screenBounds.getHeight() - scene.getHeight() - verticalMargin, y);

        // Set the stage position
        stage.setX(x);
        stage.setY(y);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }


    @FXML
    public  void exit()
    {
        Platform.exit();
    }

    public  void getMaxScore()
    {
        maxsr=db1.getMaxScore();
    }

}
