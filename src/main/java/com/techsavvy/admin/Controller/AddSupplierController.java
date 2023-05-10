package com.techsavvy.admin.Controller;

import entity.*;
import com.techsavvy.admin.Api.SupplierApi;
import com.techsavvy.admin.Models.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddSupplierController implements Initializable {

    private final SupplierApi supplierApi = new SupplierApi();
    public TextField street_txt;
    public Button confirm_btn;
    public Button cancel_btn;
    public ComboBox<String> status_combobox;
    public TextField wards_txt;
    public TextField district_txt;
    public TextField city_txt;
    public TextField phone_txt;
    public TextField email_txt;
    public TextField name_txt;
    public TextField id_txt;
    public Label close_label;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        onListener();
        setCombobox();
        try {
            setIdSupplier();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void onListener() {
        cancel_btn.setOnAction(actionEvent -> onClose());
        confirm_btn.setOnAction(actionEvent -> {
            try {
                addSupplier();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void onClose() {
        Stage stage = (Stage) close_label.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
    }

    private void setCombobox() {
        ObservableList<String> status = FXCollections.observableArrayList(
                "Cung Cấp",
                "Không Cung Cấp"
        );
        status_combobox.setItems(status);
        status_combobox.setValue("Không Cung Cấp");
    }

    private void addSupplier() throws IOException {
        boolean supplier = supplierApi.add(getSupplier());
        if (supplier) {
            onClose();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
        }
    }

    private void setIdSupplier() throws IOException {
        String id = supplierApi.getRandomId();
        id_txt.setText(id);
    }

    public Supplier getSupplier() {
        Supplier supplier = new Supplier();
        supplier.setId(id_txt.getText());
        supplier.setEmail(email_txt.getText());
        supplier.setName(name_txt.getText());
        supplier.setPhone(phone_txt.getText());
        Address address = new Address();
        address.setCity(city_txt.getText());
        address.setDistrict(district_txt.getText());
        address.setWards(wards_txt.getText());
        address.setStreet(street_txt.getText());
        supplier.setAddress(address);
        if (status_combobox.getValue().equals("Cung Cấp")) {
            supplier.setStatus(true);
        } else {
            supplier.setStatus(false);
        }
        return supplier;
    }
}
