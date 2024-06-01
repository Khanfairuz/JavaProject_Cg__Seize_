package com.example.scene3;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Monster {
    private static final int SCENE_WIDTH = 800;
    private static final int SCENE_HEIGHT = 600;
    private static final int SCROLL_SPEED = 4;
    private  static  final  int MONSTER_SCROLL_SPEED=2;
    private static final int ROAD_HEIGHT = 40;
    private static final int ROAD_Y = SCENE_HEIGHT - ROAD_HEIGHT;



    private  static  final int  MONSTER_WIDTH=250;
    private  static  final int  MONSTER_HEIGHT=250;

    private  static  final  int MONSTER_Y=ROAD_Y-MONSTER_HEIGHT;

    private  static int MONSTER_ANIMATION_INTERVAL=15;



    private Pane root;

    //
   public   Image[] Monster;
    //

   public   ImageView monsterView;


   public   int  currentMonsterIndex=0;







    ////PLay music
    public  audio_play audio=new audio_play();



    public void generateMonster(Pane root ,int lvl)
    {
        Monster = new Image[4];

        String image_path="/monster"+lvl+"walk/monster"+lvl+"walk-";
        for (int i = 0; i < Monster.length; i++) {

            Monster[i] = new Image(getClass().getResource(image_path+ i + ".png").toExternalForm());
        }
        // Create ImageView for hero and set initial position
        monsterView = new ImageView(Monster[0]);
        monsterView.setFitWidth(MONSTER_WIDTH);
        monsterView.setFitHeight(MONSTER_HEIGHT);
        monsterView.setLayoutX(Screen.getPrimary().getVisualBounds().getWidth());
        // Get the last rectangle from the combined list of road segments
        monsterView.setLayoutY(MONSTER_Y);
        root.getChildren().add(monsterView);
        System.out.println("monsteeeeeeeeeeeeer");

        //monsterView.setLayoutX(monsterView.getLayoutX() - MONSTER_SCROLL_SPEED);

    }

    public void HeroKillMonster( int frameCountM,boolean stopMonster ,boolean oneKill)
    {
        monsterView.setLayoutX(monsterView.getLayoutX() - MONSTER_SCROLL_SPEED);

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
    }
}
