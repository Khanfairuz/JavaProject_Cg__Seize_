package com.example.scene3;

import javafx.animation.TranslateTransition;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private int currentHeroImageIndex = 0;
    public Point2D heroVelocity=new Point2D(0,1);
    boolean canJump=true;
    boolean jumpObsColl=false;
    public boolean isJumping = false;
    public Pane root;

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
    }

    public int frameHero(int frameCount)
    {
        if (frameCount >= HERO_ANIMATION_INTERVAL) {
            frameCount = 0;
            currentHeroImageIndex = (currentHeroImageIndex + 1) % heroImages.length;
            heroView.setImage(heroImages[currentHeroImageIndex]);
        }
        return frameCount;
    }




    public void jump(Pane root) {
        isJumping = true;

        double initialJumpHeight = isJumping ? -200 : -100; // Initial jump height or higher jump height
        TranslateTransition jumpAnimation = new TranslateTransition(Duration.seconds(0.3), heroView);
        jumpAnimation.setByY(initialJumpHeight); // Set initial or higher jump height
        jumpAnimation.setCycleCount(1);

        jumpAnimation.setOnFinished(event -> {
            isJumping = false;
            jumpToGround(root); // Move the character back to the ground after the jump
        });

        jumpAnimation.play();
    }

//    private void jumpToGround() {
//        double currentY = heroView.getTranslateY();
//        if (currentY != 0) {
//            TranslateTransition jumpToGroundAnimation = new TranslateTransition(Duration.seconds(0.1), heroView);
//            jumpToGroundAnimation.setToY(0);
//            jumpToGroundAnimation.setToX(300);// Move the character back to the ground
//            jumpToGroundAnimation.play();
//            jumpToGroundAnimation.setOnFinished(event -> {
//                isJumping = false; // Set isJumping to false after the jump animation finishes
//                System.out.println("Jump animation finished, isJumping set to false");
//            });
//        }
//    }

    public void jumpHero()
    {
        if(canJump)
        {
            heroVelocity=heroVelocity.add(0, -30);
            canJump=false;
        }
    }

    public void moveX(int value){
        boolean movingRight = value > 0;
        for(int i = 0; i<Math.abs(value); i++) {
            for (ImageView platform : Obstacle.obstacles) {
                if(this.getBoundsInParent().intersects(platform.getBoundsInParent())) {
                    if (movingRight) {
                        if (this.getTranslateX() + HERO_HEIGHT == platform.getTranslateX()){
                            this.setTranslateX(this.getTranslateX() - 1);
                            return;
                        }
                    } else {
                        if (this.getTranslateX() == platform.getTranslateX() + HERO_HEIGHT) {
                            this.setTranslateX(this.getTranslateX() + 1);
                            return;
                        }
                    }
                }
            }
            this.setTranslateX(this.getTranslateX() + (movingRight ? 1 : -1));
        }
    }
    public void moveY(int value){
        boolean movingDown = value >0;
        for(int i = 0; i < Math.abs(value); i++){
            for(ImageView platform : new ArrayList<>(Obstacle.obstacles)){
                System.out.println("LAMe");
                if(heroView.getBoundsInParent().intersects(platform.getBoundsInParent())){
                    System.out.println("HU");
                    if(movingDown){
                        if(this.getTranslateY()+ HERO_HEIGHT == platform.getTranslateY()){
                            this.setTranslateY(this.getTranslateY()-1);
                            canJump = true;
                            return;
                        }
                    }
                    else{
                        if(this.getTranslateY() == platform.getTranslateY()+ HERO_HEIGHT){
                            this.setTranslateY(this.getTranslateY()+1);
                            heroVelocity = new Point2D(0,10);
                            return;
                        }
                    }
                }
            }
            this.setTranslateY(this.getTranslateY() + (movingDown?1:-1));
            if(this.getTranslateY()>640){
                this.setTranslateX(0);
                this.setTranslateY(400);
                HelloController.root.setLayoutX(0);
            }
        }
    }
    private void jumpToGround(Pane root) {
        double currentY = heroView.getTranslateY();
        if (currentY != 0) {
            TranslateTransition jumpToGroundAnimation = new TranslateTransition(Duration.seconds(0.1), heroView);
            ImageView obstacle=Obstacle.collisionY(root, heroView);
            if(obstacle==null)
                jumpToGroundAnimation.setByY(0);
            else
            jumpToGroundAnimation.setToY(obstacle.getTranslateY()-240); // Move the character in the Y axis
            // Adjust the X coordinate to move the character a bit forward
            jumpToGroundAnimation.setToX(heroView.getTranslateX() + 70); // Adjust 50 as needed

            if(heroView.getTranslateX()+200>HelloController.SCENE_WIDTH)
            {
                jumpToGroundAnimation.setToX(HelloController.SCENE_WIDTH - HERO_WIDTH - 700);
            }
            jumpToGroundAnimation.play();
            jumpToGroundAnimation.setOnFinished(event -> {
                isJumping = false; // Set isJumping to false after the jump animation finishes
                System.out.println("Jump animation finished, isJumping set to false");
            });
        }
    }
    public void ground()
    {
        TranslateTransition backToGround=new TranslateTransition(Duration.seconds(0.1), heroView);
        backToGround.setToY(0);
        backToGround.play();
        backToGround.setOnFinished(e->{
            System.out.println("On ground");
        });
        heroView.setY(0);
    }
    private void quizjump() {
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
            jumpToGroundAnimation.setToY(0); // Move the character back to the ground
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




}
