package com.example.scene3;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;

import java.io.FileNotFoundException;

import static javafx.scene.text.FontWeight.BOLD;


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
    public void click(MouseEvent event)throws  FileNotFoundException {
        // Create an instance of FinalRoundTry
        HelloController hc = new HelloController();

        // Call the start_new method with the stage parameter
        hc.start_new(st);
       // HelloController hc3 = new HelloController3();

        // Call the start_new method with the stage parameter
        //hc3.start_new_3(st, 0, 0, 0);
       // HelloController2 hc2 = new HelloController2();

        // Call the start_new method with the stage parameter
        //hc2.start_new_2(st, 0, 0, 0);
    }


    @FXML
    public void exitApplication(MouseEvent event) {
        Platform.exit();
    }
    @FXML
    private void showInfo(MouseEvent event) {
        // Create a custom label with the game info
        Label gameInfo = new Label(
                "Use 4 key to play the game. Keys are,up, down,left right.\n"+
                        "  Use the Up key to jump.\n" +
                        "  Down key to collect Quiz to obtain Bonus points.\n" +
                        "  Left key to move to the upper road segment.\n" +
                        "  Right key to move to the lower road segment.\n"+
                        "  Each coin contains 2 points.\n"+
                        "  Each quiz contain 1 bonus point\n"+
                        "  In Level-3 if Bonus point>15, then if collide with big obstacle, life will retrive."

                );

        // Set the font size and color
        gameInfo.setStyle("-fx-font-size: 18px; -fx-text-fill: navy;");
        gameInfo.setFont(Font.font("System", FontWeight.BOLD, 18.0));

        // Create a VBox to hold the label
        VBox content = new VBox(gameInfo);
        content.setPrefSize(800, Region.USE_COMPUTED_SIZE);
        content.setStyle("-fx-background-color: aqua;");

        // Create an alert with information type
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Key Movement");
        alert.setHeaderText(null);
        alert.getDialogPane().setContent(content); // Set the custom content

        // Set the stage style to undecorated
        alert.initStyle(StageStyle.UNDECORATED);

        // Set the background color of the dialog pane
        alert.getDialogPane().setStyle("-fx-background-color: aqua;");

        // Show the alert and wait for the user to close it
        alert.showAndWait();
    }

}
/////
