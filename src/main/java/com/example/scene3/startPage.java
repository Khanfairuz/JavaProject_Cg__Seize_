package com.example.scene3;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;

import java.io.FileNotFoundException;


    public class startPage extends Application {
        private Stage st;

        public static void main(String[] args) {
            launch(args);
        }

        @Override
        public void start(Stage stage) throws Exception {
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
            //stage.setFullScreen(true);
        }

        public void setStage(Stage stage) {
            this.st = stage;
        }

        @FXML
        public void click(ActionEvent e)throws  FileNotFoundException {
            // Create an instance of FinalRoundTry
            finalRoundTry finalRoundTry = new finalRoundTry();

            // Call the start_new method with the stage parameter
            finalRoundTry.start_new(st);
        }

        @FXML
        public void exitApplication(ActionEvent event) {
            Platform.exit();
        }
        @FXML
        private void showInfo(ActionEvent event) {
            // Create a custom label with the game info
            Label gameInfo = new Label(
                    "Use the Up key to jump, Down key to attack  "
                            + "Left key to move to the upper and Right key to move to the lower road. "

            );

            // Set the font size
            gameInfo.setStyle("-fx-font-size: 16px;");

            // Create a VBox to hold the label
            VBox content = new VBox(gameInfo);
            content.setPrefSize(900, Region.USE_COMPUTED_SIZE);

            // Create an alert with information type
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Key Movement");
            alert.setHeaderText(null);
            alert.getDialogPane().setContent(content);  // Set the custom content
            alert.initStyle(StageStyle.UNDECORATED);

            // Show the alert and wait for the user to close it
            alert.showAndWait();
        }

    }

