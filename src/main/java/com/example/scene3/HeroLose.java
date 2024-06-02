package com.example.scene3;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class HeroLose {

    @FXML
    private TextField total_point;

    @FXML
    private TextField totalcgpa;

    @FXML
    private TextField maxscore;
    @FXML
    private ImageView heroimageview;
    private Image[] heroImages;
    public static final int HERO_WIDTH = 150;
    public static final int HERO_HEIGHT = 150;
    private static final int HERO_ANIMATION_INTERVAL = 10; // In
    public int currentHeroImageIndex = 0;
    private AnimationTimer timer;
    private  int frameCount=0;
    private   cgpaCalculation cgpaC=new cgpaCalculation();
    private  Stage stage;
    private  database_connection db1=new database_connection();
    private  database_connection db2=new database_connection();
    private  double maxsr;

    private  float cgpar;


    //rank show
    //cgpa
    public  void hero_lose_start(Stage stage,int points , int bonusPoints , int damagePoints)
    {

        this.stage=stage;
        //fxml load
        FXMLLoader loader = new FXMLLoader(getClass().getResource("hero_lose.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Get the controller from the loader
        HeroLose controller = loader.getController();
        controller.setStage(stage);

        // Create the scene with the loaded root node
        Scene scene = new Scene(root);

        // Set the scene to the stage
        stage.setScene(scene);

        // Set the title of the stage
        stage.setTitle("You Lose");

        // Show the stage
        stage.show();

        //to show hero moving
        heroImages = new Image[6];
        for (int i = 0; i < heroImages.length; i++) {
            // FileInputStream inputStream = new FileInputStream("C:\\Users\\HP\\Music\\run3\\run3-0" + i + ".png");
            heroImages[i] = new Image(getClass().getResource("/Run/run3-0"+ i + ".png").toExternalForm());
        }
        heroimageview .setImage(heroImages[0]);
        heroimageview.setFitWidth(HERO_WIDTH);
        heroimageview.setFitHeight(HERO_HEIGHT);
        timer=new AnimationTimer() {

            @Override
            public void handle(long l) {
                System.out.println("time");
                frameCount++;
                if (frameCount >= HERO_ANIMATION_INTERVAL ) {
                    frameCount = 0;
                    currentHeroImageIndex = (currentHeroImageIndex + 1) % heroImages.length;
                    heroimageview.setImage(heroImages[currentHeroImageIndex]);

                }
            }
        };timer.start();
        //
        int total=points+bonusPoints+damagePoints;
        System.out.println(total);

        String str=String.valueOf(total);
        System.out.println(str);
        total_point.setText(str);
          cgpar=cgpaC.calculate_cgpa(points,bonusPoints,damagePoints);
         totalcgpa.setText(String.valueOf(cgpar));
        getMaxScore();
        //rank
        if(maxsr<cgpar)
        {
            maxsr= cgpar;
            db2.updateMaxScore(maxsr);
        }
        maxscore.setText(String.valueOf(maxsr));




    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    @FXML
    public  void go_beck_main_menu(ActionEvent event ) throws IOException {
       timer.stop();
        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("startPage.fxml"));
        Parent root = loader.load();

        // Get the controller from the loader
        startPage controller = loader.getController();
        controller.setStage(stage);

        // Create the scene with the loaded root node
        Scene scene = new Scene(root);

        // Set the scene to the stage
        stage.setScene(scene);

        // Set the title of the stage
        stage.setTitle("Start Page");

        // Show the stage
        stage.show();

    }
    @FXML
    public  void exit(ActionEvent e)
    {
        Platform.exit();
    }

    public  void getMaxScore()
    {
        maxsr=db1.getMaxScore();
    }

}
