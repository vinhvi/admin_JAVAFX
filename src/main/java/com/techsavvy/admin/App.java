package com.techsavvy.admin;

import com.techsavvy.admin.Api.EmployeeApi;
import com.techsavvy.admin.Models.Model;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) {
        Model.getInstance().getViewFactory().showLoginWindow();
    }

    public static void main(String[] args)  {
        launch();
    }

}
