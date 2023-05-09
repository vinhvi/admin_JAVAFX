package com.techsavvy.admin.Controller;

import com.techsavvy.admin.Api.EmployeeApi;
import com.techsavvy.admin.Models.LocalStorage;
import com.techsavvy.admin.Models.Model;
import com.techsavvy.admin.entity.Employee;
import com.techsavvy.admin.entity.Role;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
    private final LocalStorage localStorage = new LocalStorage();
    private final EmployeeApi employeeApi = new EmployeeApi();
    public Button dashboard_btn;
    public Button listcustomer_btn;
    public Button listimportorder_btn;
    public Button listsupplier_btn;
    public Button saleorder_btn;
    public Button listproduct_btn;
    public Button listemployee_btn;
    public Label nameNV_txt;
    public Label role_txt;
    public Button listDiscount_btn;
    public Button question_btn;

    public MenuController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListeners();
        try {
            getInfor();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void addListeners() {
        dashboard_btn.setOnAction(actionEvent -> onDashboard());
        listcustomer_btn.setOnAction(actionEvent -> onListCustomer());
        listimportorder_btn.setOnAction(actionEvent -> onListImportOrder());
        saleorder_btn.setOnAction(actionEvent -> onListSaleOrder());
        listproduct_btn.setOnAction(actionEvent -> onListProduct());
        listemployee_btn.setOnAction(actionEvent -> onListEmployee());
        listsupplier_btn.setOnAction(actionEvent -> onListSupplier());
        listDiscount_btn.setOnAction(actionEvent -> onListDiscount());
        question_btn.setOnAction(actionEvent -> onListQuestions());
    }

    private void getInfor() throws IOException, ClassNotFoundException {
        String maNV = localStorage.getEmployeeInLocal();
        Employee employee = employeeApi.getById(maNV);
        nameNV_txt.setText(employee.getFirstName()+" "+employee.getLastName());
        for (Role role1:employee.getAccount().getRoles()) {
            if (role1.getName().equals("ADMIN")){
                role_txt.setText("Quản Lý Cửa Hàng");
            }else {
                role_txt.setText("Nhân Viên Bán Hàng");
            }
        }
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

    private void onListEmployee() {
        Model.getInstance().getViewFactory().getSelectMenuItem().set("ListEmployee");
    }

    private void onListSupplier() {
        Model.getInstance().getViewFactory().getSelectMenuItem().set("ListSupplier");
    }

    private void onListDiscount() {
        Model.getInstance().getViewFactory().getSelectMenuItem().set("ListDiscount");
    }

    private void onListQuestions() {
        Model.getInstance().getViewFactory().getSelectMenuItem().set("ListQuestions");
    }
}
