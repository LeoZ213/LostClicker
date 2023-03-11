module com.example.lclicker {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.github.kwhat.jnativehook;

    opens com.autoclicker.lclicker to javafx.fxml;
    exports com.autoclicker.lclicker;
}