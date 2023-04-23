package com.techsavvy.admin.Controller;

import com.techsavvy.admin.Api.ProductApi;
import com.techsavvy.admin.Api.TypeApi;
import com.techsavvy.admin.Models.Model;
import com.techsavvy.admin.entity.Product;
import com.techsavvy.admin.entity.Type;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Setter
public class AddProductController implements Initializable {
    private final ProductApi productApi = new ProductApi();
    private final TypeApi typeApi = new TypeApi();
    public Label label_close;
    public TextField id_Product;
    public TextField nameProduct_txt;
    public TextField priceImport_txt;
    public ComboBox<String> combobox_type;
    public ComboBox<String> combobox_status;
    public TextField count_txt;
    public Button confirm_btn;
    public Button cancel_btn;
    public TextField priceSale_txt;
    public TextField lo_txt;
    public TextField origin_txt;
    private List<Product> productList;

    AddImportOrderController addImportOrderController = AddImportOrderController.getInstance();

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCombobox();
        formatTextFieldCount();
        formatTextFieldPrice();
        formatTextFieldPriceSale();
        try {
            getIdProduct();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        onListener();
    }

    private void onListener() {
        cancel_btn.setOnAction(actionEvent -> onClose());
        confirm_btn.setOnAction(actionEvent -> {
            try {
                addProductToList();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void getIdProduct() throws IOException {
        String idProduct = productApi.getRandomId();
        id_Product.setText(idProduct);
    }

    private void onClose() {
        Stage stage = (Stage) label_close.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
    }

    private void setCombobox() {
        ObservableList<String> type = FXCollections.observableArrayList(
                "Phone",
                "Tablet",
                "Accessory"
        );
        combobox_type.setItems(type);
        combobox_type.setValue("Phone");
        ObservableList<String> status = FXCollections.observableArrayList(
                "Kinh doanh",
                "Không kinh doanh"
        );
        combobox_status.setItems(status);
        combobox_status.setValue("Không kinh doanh");
    }


    public void setNewProductToList() throws IOException {
        Product product = new Product();
        if (nameProduct_txt.getText() != null
                || origin_txt.getText() != null
                || count_txt.getText() != null
                || priceImport_txt.getText() != null
                || priceSale_txt.getText() != null) {

            product.setId(id_Product.getText());
            product.setLo(lo_txt.getText());
            product.setOrigin(origin_txt.getText());
            product.setName(nameProduct_txt.getText());
            Type type = typeApi.getByName(combobox_type.getValue());
            product.setType(type);
            product.setCounts(Integer.parseInt(count_txt.getText()));
            product.setPrice(Float.parseFloat(priceSale_txt.getText()));
            boolean status;
            product.setPriceImport(Float.parseFloat(priceImport_txt.getText()));
            status = combobox_status.getValue().equals("Kinh doanh");
            product.setStatus(status);
            for (Product product1 : productList) {
                if (product1.getId().equals(product.getId())) {
                    System.out.println("adasdasd");
                    return;
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi!!");
            alert.setContentText("Các field không được trống!!");
            return;
        }
        addImportOrderController.addProduct(product);
    }

    private void formatTextFieldPrice() {
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            String input = change.getText();
            if (input.matches("[0-9]*")) {
                return change;
            }
            return null;
        });
        priceImport_txt.setTextFormatter(formatter);
    }

    private void formatTextFieldCount() {
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            String input = change.getText();
            if (input.matches("[0-9]*")) {
                return change;
            }
            return null;
        });
        count_txt.setTextFormatter(formatter);
    }

    private void formatTextFieldPriceSale() {
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            String input = change.getText();
            if (input.matches("[0-9]*")) {
                return change;
            }
            return null;
        });
        priceSale_txt.setTextFormatter(formatter);
    }

    public void setLH(String lh) {
        lo_txt.setText(lh);
    }

    private void addProductToList() throws IOException {
        setNewProductToList();
        Stage stage = (Stage) confirm_btn.getScene().getWindow();
        stage.close();
    }

}
