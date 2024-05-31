package com.example.scene3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class AudioApplication extends Application {
    @Override

    public void start(Stage stage) throws IOException {



        String mediaFilePath = "C:\\Users\\Nufsat\\Downloads\\flow-211881.mp3"; // Update this path to your media file

        try {
            // Convert the file path to URI format
            File mediaFile = new File(mediaFilePath);
            String mediaUri = mediaFile.toURI().toString();

            // Create Media, MediaPlayer, and MediaView
            Media media = new Media(mediaUri);
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            MediaView mediaView = new MediaView(mediaPlayer);

            // Create the layout and set the media view in the center
            BorderPane root = new BorderPane();
            root.setCenter(mediaView);

            // Create the scene with the layout
            Scene scene = new Scene(root, 800, 600);

            // Set the scene on the stage
            stage.setScene(scene);
            stage.setTitle("Simple Media Player");
            stage.show();

            // Play the media
            mediaPlayer.play();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error: " + e.getMessage());
        }

    }

    public static void main(String[] args) { launch(); }


}
