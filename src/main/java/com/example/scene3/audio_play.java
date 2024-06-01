package com.example.scene3;

import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;


public class audio_play {
    MediaPlayer mediaPlayer_normal;
    MediaPlayer mediaPlayer_zombie;

     public void  play_music_normal(Pane root) {


        // String mediaFilePath = "C:\\Users\\Nufsat\\Downloads\\flow-211881.mp3"; // Update this path to your media file
         String mediaFilePath="E:\\java pic\\hero_run.mp3";

         try {
             // Convert the file path to URI format
             File mediaFile = new File(mediaFilePath);
             String mediaUri = mediaFile.toURI().toString();

             // Create Media, MediaPlayer, and MediaView
             Media media = new Media(mediaUri);
             MediaPlayer mediaPlayer_normal = new MediaPlayer(media);
             MediaView mediaView = new MediaView(mediaPlayer_normal);

             // Create the layout and set the media view in the center

             root.getChildren().add(mediaView);


             // Play the media
             mediaPlayer_normal.play();
             // Loop the media
             mediaPlayer_normal.setOnEndOfMedia(() -> mediaPlayer_normal.seek(javafx.util.Duration.ZERO));
         } catch (Exception e) {
             e.printStackTrace();
             System.err.println("Error: " + e.getMessage());
         }
     }
     public  void play_music_zombie(Pane root)
     {

         String mediaFilePath="E:\\java pic\\Zombie Attack _cut.mp3";
         try {
             // Convert the file path to URI format
             File mediaFile = new File(mediaFilePath);
             String mediaUri = mediaFile.toURI().toString();

             // Create Media, MediaPlayer, and MediaView
             Media media = new Media(mediaUri);
             MediaPlayer mediaPlayer_zombie = new MediaPlayer(media);
             MediaView mediaView = new MediaView(mediaPlayer_zombie);

             // Create the layout and set the media view in the center

             root.getChildren().add(mediaView);


             // Play the media
             mediaPlayer_zombie.play();
             // Loop the media
             mediaPlayer_zombie.setOnEndOfMedia(() ->  mediaPlayer_zombie.seek(javafx.util.Duration.ZERO));
         } catch (Exception e) {
             e.printStackTrace();
             System.err.println("Error: " + e.getMessage());
         }
     }
    public void stopMusic_normal() {
        if (mediaPlayer_normal != null) {
            mediaPlayer_normal.stop();
        }
    }
    void stopMusic_zombie() {
        if (mediaPlayer_zombie!= null) {
            mediaPlayer_zombie.stop();
        }
    }

}
