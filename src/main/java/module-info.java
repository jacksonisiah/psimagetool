module me.jackson.kissa {
    requires javafx.controls;

    requires javafx.graphics;
    requires java.desktop;

    opens me.jackson.kissa to javafx.fxml;
    exports me.jackson.kissa;
}