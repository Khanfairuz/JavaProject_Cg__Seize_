package com.example.scene3;

import javafx.animation.AnimationTimer;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.ArrayList;

public class Hero extends Pane {
    public static final int HERO_WIDTH = 150;
    public static final int HERO_HEIGHT = 150;
    public static final int HERO_Y = HelloController.ROAD_Y - HERO_HEIGHT+30; // Adjust Y position as needed
    private static final int HERO_ANIMATION_INTERVAL = 10; // In
    public ImageView heroView;
    public Image[] heroImages;
   public int currentHeroImageIndex = 0;
    public Point2D heroVelocity=new Point2D(0,1);
    boolean canJump=true;
    boolean jumpObsColl=false;
    public boolean isJumping = false;
    public Pane root;
    private boolean isAttacking = false; // Flag to track if the hero is attacking
    public boolean isDownKeyPressed = false; // Declare isDownKeyPressed at the class level
    Image[] heroAttackImages = new Image[4];

    public Hero(Pane root)
    {
        this.root=root;
        heroImages = new Image[6];
        for (int i = 0; i < heroImages.length; i++) {
            // FileInputStream inputStream = new FileInputStream("C:\\Users\\HP\\Music\\run3\\run3-0" + i + ".png");
            heroImages[i] = new Image(getClass().getResource("/Run/run3-0"+ i + ".png").toExternalForm());
        }
        heroView = new ImageView(heroImages[0]);
        heroView.setFitWidth(HERO_WIDTH);
        heroView.setFitHeight(HERO_HEIGHT);
        heroView.setLayoutX(HelloController.SCENE_WIDTH - HERO_WIDTH - 300);
        heroView.setLayoutY(HERO_Y);
       // heroView.setTranslateZ(-100);
        getChildren().add(heroView);
      //  HelloController.root.getChildren().add(heroView);


        for (int i = 0; i < heroAttackImages.length; i++) {
            //FileInputStream inputStream = new FileInputStream("/sword/adventurer-attack1-0" + (i + 1) + ".png");
            heroAttackImages[i] = new Image(getClass().getResource("/sword/adventurer-attack1-0" + (i + 1) + ".png").toExternalForm());
        }
    }

    public int frameHero(int frameCount, boolean stopHero, boolean oneKill,boolean flag_hero_gone )
    {
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
        return frameCount;
    }






    public void ground()
    {
        TranslateTransition backToGround=new TranslateTransition(Duration.seconds(0.1), heroView);
        backToGround.setToY(0);
        heroView.setLayoutY(HERO_Y);
        backToGround.play();
        backToGround.setOnFinished(e->{
            System.out.println("On ground");
        });
        heroView.setY(0);
    }
    public void quizjump() {
        isJumping = true;
        double initialJumpHeight = -200; // Adjust this value as needed
        double forwardDistance = 50; // Distance to move forward on the x-axis

        TranslateTransition jumpAnimation = new TranslateTransition(Duration.seconds(0.4), heroView);
        jumpAnimation.setByY(initialJumpHeight); // Set initial jump height
        jumpAnimation.setByX(forwardDistance); // Move forward on the x-axis
        jumpAnimation.setCycleCount(1); // Only one cycle for each jump

        jumpAnimation.setOnFinished(event -> {
            quizjumpToGround(forwardDistance); // Move the character back to the ground after the jump, preserving the forward position
        });

        jumpAnimation.play();
    }

    private void quizjumpToGround(double forwardDistance) {
        double currentY = heroView.getTranslateY();
        if (currentY != 0) {
            TranslateTransition jumpToGroundAnimation = new TranslateTransition(Duration.seconds(0.1), heroView);
            ImageView obstacle=BigObstacle.collisionY(root, heroView);
            if(obstacle==null) {
                jumpToGroundAnimation.setToY(0);

                System.out.println("null collide");
            }
            else
                jumpToGroundAnimation.setToY(obstacle.getTranslateY()-60); // Move the character in the Y axis

            jumpToGroundAnimation.setToX(heroView.getTranslateX() + 70); // Adjust 50 as needed

            if(heroView.getTranslateX()+200>HelloController.SCENE_WIDTH)
            {
                jumpToGroundAnimation.setToX(HelloController.SCENE_WIDTH - HERO_WIDTH - 700);
            }


            jumpToGroundAnimation.setByX(forwardDistance); // Ensure the forward position is maintained
            jumpToGroundAnimation.play();
            jumpToGroundAnimation.setOnFinished(event -> {
                isJumping = false; // Set isJumping to false after the jump animation finishes
                System.out.println("Jump animation finished, isJumping set to false");
            });
        } else {
            isJumping = false; // Set isJumping to false if already on the ground
            System.out.println("Jump animation finished, isJumping set to false");
        }
    }

    public void DownKeyQuiz()
    {
         {
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
    }



}
