package com.autoclicker.lclicker;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

public class HelloApplication extends Application implements NativeKeyListener {
    public static LClickerController controller;

    public void nativeKeyPressed(NativeKeyEvent e) {
        /* Listens for specific key presses to determine when to start or stop an action.
        The code maps the key codes to corresponding actions using the controller's start and end button texts.
        This method is executed on a non-UI thread, and uses Platform.runLater() to update the UI thread safely.
        Note that this implementation assumes the start and end button texts are non-empty and unique, and handles the case when the same key is mapped to both start and end actions.
        */
        final String code = NativeKeyEvent.getKeyText(e.getKeyCode());
        Platform.runLater(() -> {
            if (code.equals(controller.getStartButtonText())) {
                controller.startButtonClicked();
            } else if (code.equals(controller.getEndButtonText())) {
                controller.endButtonClicked();
            }
        });
    }
    /**
     * Starts the LClicker application by loading the FXML layout from "LClickerRedesigned.fxml" and creating a new scene with it.
     * Sets up a nativeKeyListener for the application by registering this instance with the GlobalScreen.
     * Sets the title, scene, and icon of the main stage, and disables resizing.
     * Also registers a setOnCloseRequest event handler to check for open windows and closes them if they exist, and unregisters the native hook when the application is closed.
     * @param stage The main stage of the application.
     * @throws IOException If an error occurs while loading the FXML layout.
     */
    @Override
    public void start(Stage stage) throws IOException {
        // Gets the source for the fxmlLoader from "LClickerRedesigned.fxml" and creates a new scene with it
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("LClickerRedesigned.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        // Sets up the nativeKeyListener for the current application
        controller = fxmlLoader.getController();
        this.registerNativeKeyListener(this);

        // Sets the title, scene, and prevents resizable
        stage.setTitle("LClicker");
        stage.setScene(scene);
        stage.getIcons().add(new Image("https://tinyurl.com/mrxtn4nw"));
        stage.setResizable(false);
        stage.show();

        // Checks for opened windows, and closes them if opened
        stage.setOnCloseRequest(event -> {
            if (LClickerController.HOTKEYWINDOW != null) {
                LClickerController.HOTKEYWINDOW.close();
            }
            if (LClickerController.HELPWINDOW != null) {
                LClickerController.HELPWINDOW.close();
            }

            // Unregisters the NativeHook, prints StackTrace if error occurs
            try {
                GlobalScreen.unregisterNativeHook();
            } catch (NativeHookException n) {
                n.printStackTrace();
            }

            controller.endButtonClicked();
        });

    }
    /**
     * Registers a NativeKeyListener to the GlobalScreen and adds it to the specified keyListener object.
     * @param keyListener The NativeKeyListener object to register.
     */
    private void registerNativeKeyListener(NativeKeyListener keyListener) {
        /* Registers the NativeHook to the GlobalScreen and adds the keyListener parameter to it.
         * If there is a NativeHookException, prints an error message and exits the application with code 1.
         */
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
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
