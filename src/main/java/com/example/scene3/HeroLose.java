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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

public class HeroLose {
    private   startPage st=new startPage();

    private TextField total_point;
    private TextField totalcgpa;
    private TextField maxscore;
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
        totalPointsText.setFont(new Font(25.0));
        totalPointsText.setFill(Color.WHITE);

        total_point = new TextField();
        total_point.setLayoutX(150.0);
        total_point.setLayoutY(45.0);


        Text totalCgpaText = new Text("Total Cgpa");
        totalCgpaText.setLayoutX(14.0);
        totalCgpaText.setLayoutY(150.0);
        totalCgpaText.setFont(new Font(25.0));
        totalCgpaText.setFill(Color.WHITE);


        totalcgpa = new TextField();
        totalcgpa.setLayoutX(146.0);
        totalcgpa.setLayoutY(133.0);


        Text maxScoreText = new Text("Max Score");
        maxScoreText.setLayoutX(580.0);
        maxScoreText.setLayoutY(65.0);
        maxScoreText.setFont(new Font(25.0));
        maxScoreText.setFill(Color.WHITE);

        maxscore = new TextField();
        maxscore.setLayoutX(700.0);
        maxscore.setLayoutY(45.0);


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

        // Initialize animation
        initializeAnimation();

        // Set the values
        int total = points + bonusPoints + damagePoints;
        total_point.setText(String.valueOf(total));

        cgpar = cgpaC.calculate_cgpa(points, bonusPoints, damagePoints);
        totalcgpa.setText(String.valueOf(cgpar));
        getMaxScore();

        if (maxsr < cgpar) {
            maxsr = cgpar;
            db2.updateMaxScore(maxsr);
        }
        maxscore.setText(String.valueOf(maxsr));

        maxscore.setEditable(false);
        totalcgpa.setEditable(false);
        total_point.setEditable(false);
        adjustTextFieldWidth(maxscore);
        adjustTextFieldWidth(totalcgpa);
        adjustTextFieldWidth(total_point);
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

    private void adjustTextFieldWidth(TextField textField) {
        // Set initial width based on the text content
        textField.setPrefWidth(calculateTextWidth(textField));

        // Add listener to adjust width when text changes
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            textField.setPrefWidth(calculateTextWidth(textField));
        });
    }
    private double calculateTextWidth(TextField textField) {
        Text tempText = new Text(textField.getText());
        tempText.setFont(textField.getFont());
        return tempText.getLayoutBounds().getWidth() + 40; // Add some padding
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
