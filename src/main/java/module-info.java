module com.qtvsmart.admin.admin {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;

    opens com.qtvsmart.admin to javafx.fxml;
    exports com.qtvsmart.admin;
    exports com.qtvsmart.admin.Controller;


}