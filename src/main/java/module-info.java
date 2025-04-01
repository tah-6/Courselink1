module com.example.courselink1 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires java.sql;

    opens com.example.courselink1 to javafx.fxml;
    exports com.example.courselink1;
}