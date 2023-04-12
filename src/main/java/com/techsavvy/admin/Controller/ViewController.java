package com.techsavvy.admin.Controller;

import com.techsavvy.admin.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ViewController implements Initializable {
    public BorderPane views;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getViewFactory().getSelectMenuItem().addListener(((observableValue, oldVal, newVal) -> {
            switch (newVal) {
                case "ListCustomer" -> views.setCenter((Model.getInstance().getViewFactory().getListCustomer()));
                case "ListImportOrder" -> views.setCenter(Model.getInstance().getViewFactory().getListImportOrder());
                case "CreateImportOrder" -> views.setCenter(Model.getInstance().getViewFactory().getAddImportOrder());
                case "SaleOrder" -> views.setCenter(Model.getInstance().getViewFactory().getSaleOrder());
                case "CreateSaleOrder" -> views.setCenter(Model.getInstance().getViewFactory().getCreateSaleOrder());
                case "ListProduct" -> views.setCenter(Model.getInstance().getViewFactory().getListProduct());
                default -> views.setCenter(Model.getInstance().getViewFactory().getDasAnchorView());
            }
        }));

    }
}
