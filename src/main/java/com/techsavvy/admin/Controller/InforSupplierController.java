package com.techsavvy.admin.Controller;

import entity.Supplier;
import com.techsavvy.admin.Api.SupplierApi;
import com.techsavvy.admin.Models.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class InforSupplierController implements Initializable {
    private final SupplierApi supplierApi = new SupplierApi();
    public TextField id_txt;
    public Label label_close;
    public TextField name_txt;
    public TextField email_txt;
    public TextField phone_txt;
    public TextField city_txt;
    public TextField district_txt;
    public TextField wards_txt;
    public TextField street_txt;
    public ComboBox<String> status_combobox;
    public Button confirm_btn;
    public Button cancel_btn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCombobox();
        onListener();
    }

    private void onListener() {
        cancel_btn.setOnAction(actionEvent -> onClose());
    }

    private void setCombobox() {
        ObservableList<String> status = FXCollections.observableArrayList(
                "Cung Cấp",
                "Không Cung Cấp"
        );
        status_combobox.setItems(status);

    }

    private void onClose() {
        Stage stage = (Stage) label_close.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
    }

    public void setInforSupplier(Supplier supplier) {
        id_txt.setText(supplier.getId());
        name_txt.setText(supplier.getName());
        phone_txt.setText(supplier.getPhone());
        email_txt.setText(supplier.getEmail());
        city_txt.setText(supplier.getAddress().getCity());
        district_txt.setText(supplier.getAddress().getDistrict());
        wards_txt.setText(supplier.getAddress().getWards());
        street_txt.setText(supplier.getAddress().getStreet());
        boolean status = supplier.isStatus();
        if (status) {
            status_combobox.setValue("Cung Cấp");
        } else {
            status_combobox.setValue("Không Cung Cấp");
        }
    }
}
