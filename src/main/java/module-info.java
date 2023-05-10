module com.qtvsmart.admin.admin {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;
    requires javafx.web;
    requires com.google.gson;
    requires lombok;
    requires com.fasterxml.jackson.databind;
    requires spring.web;
    requires jersey.media.multipart;
    requires spring.test;
    requires jakarta.ws.rs;
    requires spring.core;
    requires java.net.http;
    requires entity;

    opens com.techsavvy.admin to javafx.fxml;


    exports com.techsavvy.admin;
    exports com.techsavvy.admin.Controller;


}