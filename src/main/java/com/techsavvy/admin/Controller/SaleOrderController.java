package com.techsavvy.admin.Controller;

import com.techsavvy.admin.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class SaleOrderController implements Initializable {
    public Button creatOrder_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListener();
    }

    private void addListener() {
        creatOrder_btn.setOnAction(actionEvent -> onAddOrder());
    }

    private void onAddOrder() {
        Model.getInstance().getViewFactory().getSelectMenuItem().set("CreateSaleOrder");
    }
}
