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

public class HeroWin {



    private TextField totalcgpa;
    private TextField maxscore;

    private  ImageView gifFail;




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
        gifFail=new ImageView(image);
        gifFail.setPreserveRatio(true);

        // Create UI components
        AnchorPane root = new AnchorPane(gifFail);




        Text totalCgpaText = new Text("Total Cgpa");
        totalCgpaText.setLayoutX(14.0);
        totalCgpaText.setLayoutY(300.0);
        totalCgpaText.setFont(new Font(25.0));
        totalCgpaText.setFill(Color.WHITE);


        totalcgpa = new TextField();
        totalcgpa.setLayoutX(450.0);
        totalcgpa.setLayoutY(300.0);


        Text maxScoreText = new Text("Max Score");
        maxScoreText.setLayoutX(100.0);
        maxScoreText.setLayoutY(100.0);
        maxScoreText.setFont(new Font(25.0));
        maxScoreText.setFill(Color.WHITE);

        maxscore = new TextField();
        maxscore.setLayoutX(700.0);
        maxscore.setLayoutY(45.0);







        Button exitButton = new Button("Exit");
        exitButton.setLayoutX(650.0);
        exitButton.setLayoutY(500.0);
        exitButton.setPrefHeight(50.0);
        exitButton.setPrefWidth(107.0);
        exitButton.setFont(new Font(28.0));
        exitButton.setOnAction(e -> exit());



        // Add components to the root
        root.getChildren().addAll(  totalCgpaText, totalcgpa, maxScoreText, maxscore,  exitButton);

        // Create the scene with the root node
        Scene scene = new Scene(root, 800,600);

        // Set the scene to the stage
        stage.setScene(scene);

        // Set the title of the stage
        stage.setTitle("You Lose");

        // Show the stage
        stage.show();

        // Initialize animation


        // Set the values


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
        adjustTextFieldWidth(maxscore);
        adjustTextFieldWidth(totalcgpa);

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
