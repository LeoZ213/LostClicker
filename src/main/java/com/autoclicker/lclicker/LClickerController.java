package com.autoclicker.lclicker;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.awt.*;
import java.awt.event.InputEvent;
import java.io.IOException;
import java.util.function.UnaryOperator;

public class LClickerController {
    @FXML
    private RadioButton cLButton;
    @FXML
    private TextField x_Field;
    @FXML
    private TextField y_Field;
    @FXML
    private Text x_Label;
    @FXML
    private Text y_Label;
    @FXML
    private ChoiceBox<String> mouseButtonChoiceBox;
    @FXML
    private ChoiceBox<String> singleDoubChoiceBox;
    @FXML
    private Button startButton;
    @FXML
    private Button endButton;
    @FXML
    private TextField hourTextField;
    @FXML
    private TextField minTextField;
    @FXML
    private TextField secondTextField;
    @FXML
    private TextField millisecTextField;
    @FXML
    private Spinner<Integer> timeLimitSpinner;
    @FXML
    private Spinner<Integer> numOfTimesSpinner;
    @FXML
    private RadioButton timeLimitRadioButton;
    @FXML
    private RadioButton numRepeatsRadioButton;
    private StringConverter<Integer> integerConverter;
    private boolean clickStarted;
    public static Stage HELPWINDOW;
    public static Stage HOTKEYWINDOW;

    public void initialize() {
        clickStarted = false;
        fillTheChoiceBoxes();
        initializeIntegerStringConverter();
        initializeXYField();
        initializeMinSecondField();
        initializeMillisecondField();
        initializeHoursField();
        initializeTimeLimitSpinner();
        initializeRepeatsSpinner();
    }

    @FXML
    public void cLOnSelected() {
        x_Field.setVisible(false);
        y_Field.setVisible(false);
        x_Label.setVisible(false);
        y_Label.setVisible(false);
    }

    @FXML
    public void sLOnSelected() {
        x_Field.setVisible(true);
        y_Field.setVisible(true);
        x_Label.setVisible(true);
        y_Label.setVisible(true);
    }

    @FXML
    public void hotKeyButtonClicked() throws IOException {
        if (HOTKEYWINDOW == null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("HotKeySettingsWindow.fxml"));
            Parent root = loader.load();
            HOTKEYWINDOW = new Stage();
            HOTKEYWINDOW.setScene(new Scene(root));
            HOTKEYWINDOW.setTitle("HotKey Settings");
            HOTKEYWINDOW.setResizable(false);
            HOTKEYWINDOW.show();
        } else if (HOTKEYWINDOW.isShowing()) {
            HOTKEYWINDOW.toFront();
        } else {
            HOTKEYWINDOW.show();
        }
        if (HOTKEYWINDOW.isIconified()) {
            HOTKEYWINDOW.setIconified(false);
            HOTKEYWINDOW.toFront();
        }
    }

    @FXML
    public void settingsButtonClicked() throws IOException {
        if (HELPWINDOW == null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("HELPWINDOW.fxml"));
            Parent root = loader.load();
            HELPWINDOW = new Stage();
            HELPWINDOW.setScene(new Scene(root));
            HELPWINDOW.setTitle("Help Window");
            HELPWINDOW.getIcons().add(new Image("C:\\Users\\lzhan\\OneDrive\\Documents\\Random Coding projects\\LClicker\\src\\main\\resources\\Images\\screwdriver.png"));
            HELPWINDOW.setResizable(false);
            HELPWINDOW.show();
        } else if (HELPWINDOW.isShowing()) {
            HELPWINDOW.toFront();
        } else {
            HELPWINDOW.show();
        }
        if (HELPWINDOW.isIconified()) {
            HELPWINDOW.setIconified(false);
            HELPWINDOW.toFront();
        }
    }

    public void startButtonClicked() {
        checkTextFieldsAreBlank();
        checkIfSpinnersAreBlank();
        clickStarted = true;
        Platform.runLater(this::clicker);
    }

    public void endButtonClicked() {
        clickStarted = false;
    }

    private void clicker() {
        long startTime = System.currentTimeMillis();
        //Coverts minutes into milliseconds
        long endTime = timeLimitSpinner.getValue() * 60000L;
        long numRepeats = numOfTimesSpinner.getValue();

        System.out.println(numRepeats);


        Task<Void> task = new Task<>() {
            @Override
            public Void call() {
                try {
                    Robot robot = new Robot();
                    int count = 0;
                    //Gets the selected value of the Mouse Button Choice-box
                    String buttonInString = getSelectedMouseButton();

                    int mouseButton = InputEvent.BUTTON1_DOWN_MASK;
                    //Determines which one of the mouse buttons to use
                    if (buttonInString.equals("Middle")) {
                        mouseButton = InputEvent.BUTTON2_DOWN_MASK;
                    } else if (buttonInString.equals("Right")) {
                        mouseButton = InputEvent.BUTTON3_DOWN_MASK;
                    }
                    //If Current Location RadioButton isn't selected, move the mouse to the specified location
                    if (!(cLButton.isSelected())) {
                        System.out.println(x_Field.getText());
                        System.out.println(y_Field.getText());
                        robot.mouseMove(Integer.parseInt(x_Field.getText()), Integer.parseInt(y_Field.getText()));
                    }
                    while (clickStarted) {
                        if (getSingleOrDoubleClick().equals("Single")) {
                            robot.mousePress(mouseButton);
                            count++;
                        } else {
                            robot.mousePress(mouseButton);
                            robot.mousePress(mouseButton);
                            count = count + 2;
                        }
                        //A minimum of 15ms was added to prevent the freezing of the cpu
                        Thread.sleep(15);
                        long timeToSleep = ((Integer.parseInt(hourTextField.getText()) * 3600000L)
                                + (Integer.parseInt(minTextField.getText()) * 60000L)
                                + (Integer.parseInt(secondTextField.getText()) * 1000L)
                                + Integer.parseInt(millisecTextField.getText()));
                        Thread.sleep(timeToSleep);
                        robot.mouseRelease(mouseButton);
                        System.out.println("CLICKED!");
                        //Checks for value in the TextFields for the time intervals for each click
                        if (((System.currentTimeMillis() - startTime >= endTime) && timeLimitRadioButton.isSelected())
                                ||
                                (numRepeatsRadioButton.isSelected() && count >= numRepeats)) {
                            break;
                        }

                    }
                } catch (Exception error) {
                    error.printStackTrace();
                }
                return null;
            }
        };
        new Thread(task).start();
    }

    private void initializeXYField() {
        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("([1-9][0-9]*)?") && newText.length() < 5) {
                return change;
            }
            return null;
        };

        TextFormatter<Integer> textFormatter = new TextFormatter<>(this.integerConverter, 0, integerFilter);
        x_Field.setTextFormatter(textFormatter);

        TextFormatter<Integer> yTextFormatter = new TextFormatter<>(this.integerConverter, 0, integerFilter);
        y_Field.setTextFormatter(yTextFormatter);

    }

    private void initializeMinSecondField() {
        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("([1-5]?[0-9]|[0-9])?") && newText.length() < 3) {
                return change;
            }
            return null;
        };
        TextFormatter<Integer> secondTextFormatter = new TextFormatter<>(this.integerConverter, 0, integerFilter);
        secondTextField.setTextFormatter(secondTextFormatter);
        TextFormatter<Integer> minTextFormatter = new TextFormatter<>(this.integerConverter, 0, integerFilter);
        minTextField.setTextFormatter(minTextFormatter);
    }

    private void initializeMillisecondField() {
        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("([0-9]|[1-9][0-9]{1,2}|9[0-8][0-9]|99[0-8])?") && newText.length() < 4) {
                return change;
            }
            return null;
        };
        TextFormatter<Integer> millisecondFormatter = new TextFormatter<>(this.integerConverter, 0, integerFilter);
        millisecTextField.setTextFormatter(millisecondFormatter);

    }

    private void initializeHoursField() {
        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("([0-9]|[1-2][0-4])?") && newText.length() < 3) {
                return change;
            }
            return null;
        };
        TextFormatter<Integer> hourFormatter = new TextFormatter<>(this.integerConverter, 0, integerFilter);
        hourTextField.setTextFormatter(hourFormatter);
    }

    private void initializeIntegerStringConverter() {
        this.integerConverter = new IntegerStringConverter() {
            @Override
            public Integer fromString(String s) {
                if (s.isEmpty()) return 0;
                return super.fromString(s);
            }
        };
    }

    private void initializeTimeLimitSpinner() {
        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("(^([1-5]?[0-9]|60)$)?") && newText.length() < 3) {
                return change;
            }
            return null;
        };
        TextFormatter<Integer> timeLimitFormatter = new TextFormatter<>(this.integerConverter, 0, integerFilter);
        timeLimitSpinner.getEditor().setTextFormatter(timeLimitFormatter);
    }

    private void initializeRepeatsSpinner() {
        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("(^(0|[1-9]\\d{0,2}|[1-4]\\d{3}|5000)$)?") && newText.length() < 5) {
                return change;
            }
            return null;
        };
        TextFormatter<Integer> repeatsSpinnerFormater = new TextFormatter<>(this.integerConverter, 0, integerFilter);
        numOfTimesSpinner.getEditor().setTextFormatter(repeatsSpinnerFormater);
    }


    private void checkTextFieldsAreBlank() {
        if (x_Field.getText().isBlank()) {
            x_Field.setText("0");
        } else if (y_Field.getText().isBlank()) {
            y_Field.setText("0");
        } else if (hourTextField.getText().isBlank()) {
            hourTextField.setText("0");
        } else if (minTextField.getText().isBlank()) {
            minTextField.setText("0");
        } else if (secondTextField.getText().isBlank()) {
            secondTextField.setText("0");
        } else if (millisecTextField.getText().isBlank()) {
            millisecTextField.setText("0");
        }
    }

    private void checkIfSpinnersAreBlank() {
        if (timeLimitSpinner.getEditor().getText().isBlank()) {
            timeLimitSpinner.getEditor().setText("1");
        } else if (numOfTimesSpinner.getEditor().getText().isBlank()) {
            numOfTimesSpinner.getEditor().setText("1");
        }
    }

    private void fillTheChoiceBoxes() {
        mouseButtonChoiceBox.setItems(FXCollections.observableArrayList("Left", "Right", "Middle"));
        mouseButtonChoiceBox.getSelectionModel().selectFirst();

        singleDoubChoiceBox.setItems(FXCollections.observableArrayList("Single", "Double"));
        singleDoubChoiceBox.getSelectionModel().selectFirst();
    }

    @FXML
    public void timeLimitRadioButtonClicked() {
        timeLimitSpinner.setVisible(true);
        numOfTimesSpinner.setVisible(false);
    }

    @FXML
    public void numOfTimesRadioButtonClicked() {
        numOfTimesSpinner.setVisible(true);
        timeLimitSpinner.setVisible(false);
    }

    @FXML
    public void RuntilStoppedRadioButtonClicked() {
        numOfTimesSpinner.setVisible(false);
        timeLimitSpinner.setVisible(false);
    }

    private String getSelectedMouseButton() {
        return mouseButtonChoiceBox.getSelectionModel().getSelectedItem();
    }

    public String getStartButtonText() {
        String[] parts = startButton.getText().split("/");
        return parts[1];
    }

    public String getEndButtonText() {
        String[] parts = endButton.getText().split("/");
        return parts[1];
    }

    public String getSingleOrDoubleClick() {
        return singleDoubChoiceBox.getSelectionModel().getSelectedItem();
    }

    public void setStartButtonText(String text) {
        startButton.setText(text);
    }
    public void setEndButtonText(String text) {
        endButton.setText(text);
    }
}