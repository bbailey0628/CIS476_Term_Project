package com.example.cis476termproject;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.InputEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class MyPassApplication extends Application {
    private static Stage primaryStage;
    private static Scene currentScene;
    private static PauseTransition inactivityTimer;
    private static EventHandler<InputEvent> activityHandler;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(MyPassApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);

        stage.setTitle("CIS476 Term_Project");
        stage.setScene(scene);
        stage.setMinHeight(450);
        stage.setMinWidth(675);

        attachActivityMonitoring(scene);

        stage.show();
    }

    public static void switchScene(String fxmlFile) throws Exception {
        FXMLLoader loader = new FXMLLoader(MyPassApplication.class.getResource(fxmlFile));
        Scene scene = new Scene(loader.load());
        primaryStage.setScene(scene);
        attachActivityMonitoring(scene);
    }

    private static void attachActivityMonitoring(Scene scene) {
        if (activityHandler == null) {
            activityHandler = event -> {
                SessionManager.getInstance().refreshActivity();
                restartInactivityTimer();
            };
        }

        if (currentScene != null) {
            currentScene.removeEventFilter(InputEvent.ANY, activityHandler);
        }

        currentScene = scene;
        currentScene.addEventFilter(InputEvent.ANY, activityHandler);
        restartInactivityTimer();
    }

    private static void restartInactivityTimer() {
        if (inactivityTimer == null) {
            Duration timeout = SessionManager.getInstance().getInactivityTimeout();
            inactivityTimer = new PauseTransition(timeout);
            inactivityTimer.setOnFinished(event -> handleAutoLock());
        }

        inactivityTimer.stop();
        inactivityTimer.playFromStart();
    }

    private static void handleAutoLock() {
        SessionManager.getInstance().clearSession();
        ClipboardManager.clearClipboard();
        try {
            switchScene("login-view.fxml");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
