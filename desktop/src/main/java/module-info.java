module com.desktop.desktop {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.desktop.desktop to javafx.fxml;
    exports com.desktop.desktop;
}