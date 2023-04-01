module com.qtvsmart.admin.admin {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;

    opens com.techsavvy.admin to javafx.fxml;
    exports com.techsavvy.admin;
    exports com.techsavvy.admin.Controller;


}