package com.techsavvy.admin.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddImportOrderController implements Initializable {
    public Button infor_btn;
    public Button addProduct_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListener();
    }

    private void addListener() {
        infor_btn.setOnAction(actionEvent -> {
            try {
                onInforSupplier();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        addProduct_btn.setOnAction(actionEvent -> {
            try {
                onAddProduct();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void onInforSupplier() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/InforSupplier.fxml"));
        Parent root = fxmlLoader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void onAddProduct() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/CreateProduct.fxml"));
        Parent root = fxmlLoader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
