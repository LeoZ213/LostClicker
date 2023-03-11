package com.autoclicker.lclicker;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

public class HelloApplication extends Application implements NativeKeyListener {
    public static LClickerController controller;
    public void nativeKeyPressed(NativeKeyEvent e) {
        //Listens for keys pressed and decides whether to start/stop or do nothing
        final String code = NativeKeyEvent.getKeyText(e.getKeyCode());
        Platform.runLater(()->{
            if (code.equals(controller.getStartButtonText())){
                controller.startButtonClicked();
            }else if(code.equals(controller.getEndButtonText())) {
                controller.endButtonClicked();
            }
        });
    }

    @Override
    public void start(Stage stage) throws IOException {
        //Gets the source for the fxmlLoader from "LClickerResigned.fxml" and creates a new scene with it
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("LClickerResigned.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        //Sets up the nativeKeyListener for the current application
        controller = fxmlLoader.getController();
        this.registerNativeKeyListener(this);
        //Sets the title, scene, and prevents resizable
        stage.setTitle("LClicker");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        stage.setOnCloseRequest(event -> {
            //If HOTKEYWINDOW Stage has been created, close it. Or else it does nothing
            if (LClickerController.HOTKEYWINDOW != null) {
                LClickerController.HOTKEYWINDOW.close();
            }
            //If HELPWINDOW Stage has been created, close it. Or else it does nothing
            if (LClickerController.HELPWINDOW != null) {
                LClickerController.HELPWINDOW.close();
            }
            //Unregisters the NativeHook, prints StackTrace if error occurs
            try {
                GlobalScreen.unregisterNativeHook();
            } catch (NativeHookException n) {
                n.printStackTrace();
            }
            System.out.println("Closed Successfully");
            controller.endButtonClicked();
        });
    }
    private void registerNativeKeyListener(NativeKeyListener keyListener) {
        /*Registers NativeHook to the Global Screen and adds a NativeKeyListener to the "keylistener" passed
        as the parameter
        */
        try {
            GlobalScreen.registerNativeHook();
        }
        catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());

            System.exit(1);
        }
        GlobalScreen.addNativeKeyListener(keyListener);
    }

    public static void main(String[] args) {
        launch();
    }
}