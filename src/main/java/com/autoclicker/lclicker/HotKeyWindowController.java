package com.autoclicker.lclicker;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class HotKeyWindowController {
    @FXML
    private Label stopHotKeyLabel;
    @FXML
    private Label startHotKeyLabel;
    @FXML
    private Button stopHotKeyButton;
    @FXML
    private Button startHotKeyButton;

    /**
     * Handles the click event of the button for stop hotkey in the hotkey window
     * Changes the value of the controller's end button text based on the KeyPressed event
     * Removes the keyPressed event handler from the stage
     */
    @FXML
    public void stopHotKeyButtonClicked() {
        Stage stage = (Stage) stopHotKeyButton.getScene().getWindow();
        stopHotKeyLabel.setText("Waiting for Input");
        stage.getScene().setOnKeyPressed(event -> {
            // Check if the pressed key is a valid hotkey
            if (event.getCode() == KeyCode.F3) {
                stopHotKeyLabel.setText("Stop Hotkey: F3");
            } else {
                // Set the label text to the pressed key
                stopHotKeyLabel.setText("Stop Hotkey: " + event.getCode().getName());
                HelloApplication.controller.setEndButtonText("End/" + event.getCode().getName());
            }
            // Remove the key press event handler
            stage.getScene().setOnKeyPressed(null);
        });
    }

    /**
     * Handles the click event of the button for start hotkey in the hotkey window
     * Changes the value of the controller's start button text based on the KeyPressed event
     * Removes the keyPressed event handler from the stage
     */
    public void startHotKeyButtonClicked() {
        Stage stage = (Stage) startHotKeyButton.getScene().getWindow();
        startHotKeyLabel.setText("Waiting for Input");
        stage.getScene().setOnKeyPressed(keyEvent -> {
            // Check if the pressed key is a valid hotkey
            if (keyEvent.getCode() == KeyCode.F1) {
                startHotKeyLabel.setText("Start HotKey: F1");
            } else {
                // Set the label text to the pressed key
                startHotKeyLabel.setText("Start HotKey: " + keyEvent.getCode().getName());
                HelloApplication.controller.setStartButtonText("Start/" + keyEvent.getCode().getName());
            }
            // Remove the key press event handler
            stage.getScene().setOnKeyPressed(null);
        });
    }
}

