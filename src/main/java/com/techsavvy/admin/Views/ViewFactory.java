package com.techsavvy.admin.Views;

import com.techsavvy.admin.Controller.ViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ViewFactory {

    private AnchorPane dasAnchorView;

    public ViewFactory() {}

    public AnchorPane getDasAnchorView() {
        if (dasAnchorView == null) {
            try {
                dasAnchorView = new FXMLLoader(getClass().getResource("/Fxml/Dashboard.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dasAnchorView;
    }

    public void showLoginWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource ("/Fxml/Login.fxml"));
        createStage(loader);
    }

    public void createStage(FXMLLoader loader) {

        Scene scene =null;
        try {
            scene = new Scene(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Stage stage =  new Stage();
        stage.setScene(scene);
        stage.setTitle("TECH SAVVY");
        stage.show();
    }
    public void showViews(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Views.fxml"));
        ViewController viewController = new ViewController();
        loader.setController(viewController);
        createStage(loader);
    }

}
