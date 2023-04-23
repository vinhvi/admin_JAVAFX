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
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpmime;
    requires org.apache.httpcomponents.httpcore;
    requires spring.core;

    opens com.techsavvy.admin to javafx.fxml;
    opens com.techsavvy.admin.entity to com.google.gson;

    exports com.techsavvy.admin.entity;
    exports com.techsavvy.admin;
    exports com.techsavvy.admin.Controller;


}