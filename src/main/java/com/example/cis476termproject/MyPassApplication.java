package com.example.cis476termproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MyPassApplication extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(MyPassApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 650, 425);

        stage.setTitle("CIS476 Term_Project");
        stage.setScene(scene);
        stage.setMinHeight(450);
        stage.setMinWidth(675);

        stage.show();
    }

    public static void switchScene(String fxmlFile) throws Exception {
        FXMLLoader loader = new FXMLLoader(MyPassApplication.class.getResource(fxmlFile));
        Scene scene = new Scene(loader.load());
        primaryStage.setScene(scene);
    }
}
