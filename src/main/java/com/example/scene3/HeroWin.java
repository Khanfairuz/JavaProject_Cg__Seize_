package com.example.scene3;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

public class HeroWin {

    private Text totalcgpa;
    private Text maxscore;

    private ImageView gifFail;

    private AnimationTimer timer;

    private cgpaCalculation cgpaC = new cgpaCalculation();
    private Stage stage;
    private database_connection db1 = new database_connection();
    private database_connection db2 = new database_connection();
    private double maxsr;
    private float cgpar;

    public void hero_win_start(Stage stage, int points, int bonusPoints, int damagePoints) {

        this.stage = stage;
        Image image = new Image(getClass().getResource("/win_new.jpg").toExternalForm());
        gifFail = new ImageView(image);
        gifFail.setPreserveRatio(true);

        // Create UI components
        AnchorPane root = new AnchorPane(gifFail);

        Text totalCgpaText = new Text("Total Cgpa");
        totalCgpaText.setLayoutX(250.0);
        totalCgpaText.setLayoutY(300.0);
        totalCgpaText.setFont(Font.font("System", FontWeight.BOLD, 35.0));
        totalCgpaText.setFill(Color.WHITE);

        totalcgpa = new Text();
        totalcgpa.setLayoutX(450.0);
        totalcgpa.setLayoutY(300.0);
        totalcgpa.setFont(Font.font("System", FontWeight.BOLD, 35.0));
        totalcgpa.setFill(Color.WHITE);

        Text maxScoreText = new Text("Max Score");
        maxScoreText.setLayoutX(50.0);
        maxScoreText.setLayoutY(204.0);
        maxScoreText.setFont(Font.font("System", FontWeight.BOLD, 35.0));
        maxScoreText.setFill(Color.WHITE);

        maxscore = new Text();
        maxscore.setLayoutX(230.0);
        maxscore.setLayoutY(204.0);
        maxscore.setFont(Font.font("System", FontWeight.BOLD, 35.0));
        maxscore.setFill(Color.WHITE);

        Button exitButton = new Button("Exit");
        exitButton.setLayoutX(650.0);
        exitButton.setLayoutY(500.0);
        exitButton.setPrefHeight(50.0);
        exitButton.setPrefWidth(90.0);
        exitButton.setFont(new Font(29.0));
        // Set button background to purple and text to navy blue
        exitButton.setStyle("-fx-background-color: violet; -fx-text-fill: navy;");

        exitButton.setOnAction(e -> exit());

        // Add components to the root
        root.getChildren().addAll(totalCgpaText, totalcgpa, maxScoreText, maxscore, exitButton);

        // Create the scene with the root node
        Scene scene = new Scene(root, 800, 600);

        // Set the scene to the stage
        stage.setScene(scene);

        // Set the title of the stage
        stage.setTitle("You Win");

        // Show the stage
        stage.show();
        centerStage(stage, scene);

        // Initialize animation (if needed)
        // initializeAnimation();

        // Set the values
        cgpar = cgpaC.calculate_cgpa(points, bonusPoints, damagePoints);
        totalcgpa.setText(String.valueOf(cgpar));
        getMaxScore();

        if (maxsr < cgpar) {
            maxsr = cgpar;
            db2.updateMaxScore(maxsr);
        }
        maxscore.setText(String.valueOf(maxsr));
        System.out.println("Total Points : "+points);
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
    public void exit() {
        Platform.exit();
    }

    public void getMaxScore() {
        maxsr = db1.getMaxScore();
    }
}

