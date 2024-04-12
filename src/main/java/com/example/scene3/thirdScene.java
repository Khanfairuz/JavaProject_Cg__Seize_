package com.example.scene3;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class thirdScene {



        @FXML
        private ImageView img3;
        @FXML
        private  ImageView img4;

        private   double image_width;
        private  double image_height;
        private  int sprite_x_count=2;
        private  int sprite_y_count=5;
        private  double sprite_width;
        private  double sprite_height;
        private  int frameIndex=0;
        public  boolean flag=true;
        private  int secondsElapsed=0;
        public  Timeline main_timer;//scene start theke count korbe
        public  Timeline timeline;//hero walk
       public  Timeline timelineh;//hero die

        //zombie

         private   double image_width_za;
    private  double image_height_za;
    private  int sprite_x_count_za=1;
    private  int sprite_y_count_za=5;
    private  double sprite_width_za;
    private  double sprite_height_za;
    private  int frameIndex_za=0;
    public  boolean zombie_flag=true;//no use

    private  Timeline timelinez;//dead
    private Timeline timelineza;//alive

    //QUESTION AND ANSWER
    private database_connection dc;
    private  String question;
    private  String optionA;
    private  String optionB;
    private  String optionC;
    private  String optionD;
    private  String correctAns;

    //question show
    @FXML
    private  Label label1;
    @FXML
    private CheckBox A;
    @FXML
    private CheckBox B;
    @FXML
    private CheckBox C;
    @FXML
    private CheckBox D;

    public  boolean track_zombie_kill=false;
    public  boolean track_hero_kill=false;
    @FXML
    public Button b2;
    @FXML
    public  Button b3;


    public  ActionEvent to_load_next_scene;//at present no use

        @FXML
        public void initialize() {
            //hero
            image_width = img3.getImage().getWidth();
            image_height = img3.getImage().getHeight();
            sprite_width = image_width / sprite_y_count;
            sprite_height = image_height / sprite_x_count;
            //zombie
            image_width_za = img4.getImage().getWidth();
            image_height_za = img4.getImage().getHeight();
            sprite_width_za = image_width_za / sprite_y_count_za;
            sprite_height_za = image_height_za / sprite_x_count_za;
            frameIndex_za=0;

            //database call
            dc = new database_connection();
            dc.connection("objectorientedprogramming" ,this);
            label1.setText(question);
            A.setText(optionA);
            B.setText(optionB);
            C.setText(optionC);
            D.setText(optionD);

            //question..ans
            System.out.println("Question: " + question);
            System.out.println("Option A: " + optionA);
            System.out.println("Option B: " + optionB);
            System.out.println("Option C: " + optionC);
            System.out.println("Option D: " + optionD);
            System.out.println("Correct Answer: " + correctAns);

            System.out.println("Hlw");
            track_time();
            sprite_movement_hero();
            //sprite_movement_zombie();
            Platform.runLater(this::sprite_movement_zombie);//zombie te scene set er por er value lagbe
            
        }

        //main time trcak --.15 sec event

        public  void track_time()
        {
           main_timer = new Timeline(
                    new KeyFrame(Duration.seconds(1), event -> {
                        secondsElapsed++;
                        System.out.println("TIMER : "+secondsElapsed);
                        if(secondsElapsed==15)
                        {
                            if(track_zombie_kill==true)
                            {
                              b2.setVisible(true);

                            }
                            else
                            {
                                //nije nije marbo
                                b3.fire();
                            }
                        }
                    })
            );
            main_timer.setCycleCount(Animation.INDEFINITE);
            main_timer.play();
        }
        //b2 button er sathe connected
        @FXML
        public  void kill(ActionEvent e)
        {
           System.out.println("HIII");
            flag=false;
            zombie_flag=false;
            img4.setImage(new Image(getClass().getResource("/image_folder_f/z1_die-removebg_less.png").toExternalForm()));


            image_width_za = img4.getImage().getWidth();
            image_height_za = img4.getImage().getHeight();
            sprite_width_za = image_width_za / sprite_y_count_za;
            sprite_height_za = image_height_za / sprite_x_count_za;
            sprite_x_count_za=1;
            sprite_y_count_za=3;
            timelineza.stop();
            img4.setViewport(new Rectangle2D(0, 0, sprite_width_za, sprite_height_za));
            sprite_movement_zombie_die();




        }

        //b3 button er sathe connected
        @FXML
        public  void kill_hero(ActionEvent e)
        {

            timeline.stop();
            sprite_movement_hero_die();
        }

        //walking hero
        public void sprite_movement_hero()
        {
            img3.setViewport(new Rectangle2D(0,0,sprite_width , sprite_height));
                   timeline=new Timeline(
                    new KeyFrame(Duration.seconds(0.2), event -> {
                        if(flag==true) {


                            frameIndex = (frameIndex + 1) % 2;
                            System.out.println(frameIndex * sprite_width);
                            img3.setViewport(new Rectangle2D(frameIndex * sprite_width, 0, sprite_width, sprite_height));
                            img3.setTranslateX(img3.getTranslateX() + 4);
                        }
                        else if(flag==false)
                        {

                            frameIndex = (frameIndex + 1) % sprite_y_count;
                            System.out.println(frameIndex * sprite_width);
                            img3.setViewport(new Rectangle2D(frameIndex * sprite_width, 0, sprite_width, sprite_height));
                            img3.setTranslateX(img3.getTranslateX() + 4);
                            if(frameIndex==0)
                            {
                                flag=true;
                            }
                        }

                         // System.out.println(frameIndex);
                    })
            );
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();
        }
        //walking zombie
    public void sprite_movement_zombie()
    {
        //img4.setTranslateX(img4.getParent().getBoundsInParent().getWidth() - sprite_width_za);
        //img4.setViewport(new Rectangle2D(sprite_width_za*(sprite_y_count_za-1),0,sprite_width_za , sprite_height_za));
        double sceneWidth = img4.getScene().getWidth(); // Get the width of the scene
        double zombieWidth = img4.getBoundsInLocal().getWidth(); // Get the width of the zombie image

        // Set the initial position of the zombie to the right corner
        img4.setTranslateX(sceneWidth - zombieWidth);

        // Set the viewport to show the first frame of the zombie
        img4.setViewport(new Rectangle2D(0, 0, sprite_width_za, sprite_height_za));
           timelineza=new Timeline(
                new KeyFrame(Duration.seconds(0.1), event -> {

                           frameIndex_za = (frameIndex_za + 1) % sprite_y_count_za;

                           img4.setViewport(new Rectangle2D(frameIndex_za * sprite_width_za, 0, sprite_width_za, sprite_height_za));
                           img4.setTranslateX(img4.getTranslateX() - 4);


                })
        );
        timelineza.setCycleCount(Animation.INDEFINITE);
        timelineza.play();
    }
    public void sprite_movement_zombie_die()
    {



        // Set the viewport to show the first frame of the zombie
        //img4.setViewport(new Rectangle2D(0, 0, sprite_width_za, sprite_height_za));
        timelinez=new Timeline(
                new KeyFrame(Duration.seconds(0.2), event -> {

                    frameIndex_za = (frameIndex_za + 1) % sprite_y_count_za;
                    System.out.println("SPRITE "+frameIndex * sprite_width);
                    img4.setViewport(new Rectangle2D(frameIndex_za * sprite_width_za, 0, sprite_width_za, sprite_height_za));
                    img4.setTranslateX(img4.getTranslateX() - 4);
                    if(frameIndex_za==2)
                    {
                        img4.toBack();
                        img4.setVisible(false);

                        timelineza.stop();
                    }


                })
        );
        timelinez.setCycleCount(Animation.INDEFINITE);
        timelinez.play();
    }

    public  void sprite_movement_hero_die()
    {

                timelineh=new Timeline(
                new KeyFrame(Duration.seconds(0.2), event -> {

                        frameIndex = (frameIndex + 1) % (sprite_y_count);
                        System.out.println(frameIndex * sprite_width);
                        img3.setViewport(new Rectangle2D(frameIndex * sprite_width, sprite_height*1, sprite_width, sprite_height));
                        img3.setTranslateX(img3.getTranslateX() + 4);
                        if(frameIndex==4)
                        {
                            img3.setVisible(false);
                            timelineh.stop();
                        }


                    // System.out.println(frameIndex);
                })
        );
        timelineh.setCycleCount(Animation.INDEFINITE);
        timelineh.play();




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
    }



    }

    /*
        img3 -->hero r jonno -->imageView;
        img4 -->zombie r jonno -->imageView
        sprite_x_count , sprite_y_count , sprite_width , sprite_height,freamIndex , flag-->hero
        flag-->true -->walk
        flag--->false-->kill
        to_load_next_scene-->



     */
//i am checking to see the pull push of github