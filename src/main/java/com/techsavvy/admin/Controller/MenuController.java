package com.techsavvy.admin.Controller;

import com.techsavvy.admin.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
    public Button dashboard_btn;
    public Button listcustomer_btn;
    public Button listimportorder_btn;
    public Button listsupplier_btn;
    public Button saleorder_btn;
    public Button listproduct_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListeners();
    }

    private void addListeners() {
        dashboard_btn.setOnAction(actionEvent -> onDashboard());
        listcustomer_btn.setOnAction(actionEvent -> onListCustomer());
        listimportorder_btn.setOnAction(actionEvent -> onListImportOrder());
        saleorder_btn.setOnAction(actionEvent -> onListSaleOrder());
        listproduct_btn.setOnAction(actionEvent -> onListProduct());
    }

    private void onDashboard() {
        Model.getInstance().getViewFactory().getSelectMenuItem().set("Dashboard");
    }

    private void onListCustomer() {
        Model.getInstance().getViewFactory().getSelectMenuItem().set("ListCustomer");
    }

    private void onListImportOrder() {
        Model.getInstance().getViewFactory().getSelectMenuItem().set("ListImportOrder");
    }

    private void onListSaleOrder() {
        Model.getInstance().getViewFactory().getSelectMenuItem().set("SaleOrder");
    }

    private void onListProduct() {
        Model.getInstance().getViewFactory().getSelectMenuItem().set("ListProduct");
    }
}
