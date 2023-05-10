package com.techsavvy.admin.Controller;

import entity.Options;
import entity.Product;
import com.techsavvy.admin.Api.OptionsApi;
import com.techsavvy.admin.Api.ProductApi;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddProductForSaleOrderController implements Initializable {

    private final ProductApi productApi = new ProductApi();
    private final OptionsApi optionsApi = new OptionsApi();

    public TextField count_sale;
    public TextField color_options_selected;
    public TextField name_options_selected;
    public TextField lo_product_selected;
    public TextField name_product_selected;
    public TextField id_product_selected;
    public TableColumn<Options, Void> column_options;
    public TableColumn<Options, String> column_price_options;
    public TableColumn<Options, String> column_count_options;
    public TableColumn<Options, String> column_color_options;
    public TableColumn<Options, String> column_name_options;
    public TableColumn<Options, Integer> column_stt_options;
    public TableView<Options> table_list_options;
    public TableColumn<Product, Void> column_options_product;
    public TableColumn<Product, String> column_lo_so_product;
    public TableColumn<Product, String> column_count_product;
    public TableColumn<Product, String> column_name_product;
    public TableColumn<Product, String> column_id_product;
    public TableColumn<Product, String> column_type;
    public TableColumn<Product, Integer> column_stt_product;
    public TableView<Product> table_list_product;
    public TextField search_txt;
    public Button btn_search;
    public Label lbl_error;
    public Button confirm_btn;
    public Button update_btn;

    private Options options;

    public Options getOptions() {
        return options;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setOnListener();
        formatCountTxt();
    }

    private void setOnListener() {
        btn_search.setOnAction(actionEvent -> {
            try {
                searchProduct();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        confirm_btn.setOnAction(actionEvent -> addProductForOrder());
        update_btn.setOnAction(actionEvent -> updateSeclected());
    }

    private void searchProduct() throws IOException, ClassNotFoundException {
        String key = search_txt.getText();
        List<Product> products = new ArrayList<>(productApi.getProductByIdOrName(key));
        if (products.isEmpty()) {
            lbl_error.setVisible(true);
        } else {
            lbl_error.setVisible(false);
            setTableSearchProduct(products);
        }

    }

    private void setTableSearchProduct(List<Product> products) {
        column_stt_product.setCellValueFactory(stt -> new ReadOnlyObjectWrapper<>(table_list_product.getItems().indexOf(stt.getValue()) + 1));
        column_name_product.setCellValueFactory(name -> {
            if (name.getValue().getName() != null) {
                String nameProduct = name.getValue().getName();
                return new SimpleStringProperty(nameProduct);
            } else {
                return null;
            }

        });
        column_type.setCellValueFactory(type -> {
            String nameType = type.getValue().getType().getName();
            return new SimpleStringProperty(nameType);
        });
        column_count_product.setCellValueFactory(count -> {
            String sl = String.valueOf(count.getValue().getCounts());
            return new SimpleStringProperty(sl);
        });
        column_id_product.setCellValueFactory(id -> {
            String idProduct = id.getValue().getId();
            return new SimpleStringProperty(idProduct);
        });
        column_lo_so_product.setCellValueFactory(lH -> {
            String lo = lH.getValue().getLo();
            return new SimpleStringProperty(lo);
        });

        Callback<TableColumn<Product, Void>, TableCell<Product, Void>> cellCallback = new Callback<>() {
            @Override
            public TableCell<Product, Void> call(TableColumn<Product, Void> productVoidTableColumn) {
                return new TableCell<>() {
                    private final Button select_btn = new Button("Chọn");

                    {
                        select_btn.setOnAction((ActionEvent event) -> {
                            Product data = getTableView().getItems().get(getIndex());
                            name_product_selected.setText(data.getName());
                            lo_product_selected.setText(data.getLo());
                            id_product_selected.setText(data.getId());
                            try {
                                List<Options> optionsList = optionsApi.getOptionsByProduct(data.getId());
                                setTableOptions(optionsList);
                            } catch (IOException | ClassNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(select_btn);
                        }
                    }
                };
            }
        };
        column_options_product.setCellFactory(cellCallback);
        ObservableList<Product> observableList = FXCollections.observableArrayList(products);
        table_list_product.setItems(observableList);
    }

    private void setTableOptions(List<Options> optionsList) {
        column_stt_options.setCellValueFactory(stt -> new ReadOnlyObjectWrapper<>(table_list_options.getItems().indexOf(stt.getValue()) + 1));
        column_color_options.setCellValueFactory(color -> {
            String colorOptions = color.getValue().getColor();
            return new SimpleStringProperty(colorOptions);
        });
        column_count_options.setCellValueFactory(count -> {
            String countOptions = String.valueOf(count.getValue().getCount());
            return new SimpleStringProperty(countOptions);
        });
        column_name_options.setCellValueFactory(name -> {
            String nameOp = name.getValue().getNameOptions();
            return new SimpleStringProperty(nameOp);
        });
        column_price_options.setCellValueFactory(price -> {
            String priceOp = String.valueOf(price.getValue().getPrice());
            return new SimpleStringProperty(priceOp);
        });
        Callback<TableColumn<Options, Void>, TableCell<Options, Void>> cellCallback = new Callback<>() {
            @Override
            public TableCell<Options, Void> call(TableColumn<Options, Void> optionsVoidTableColumn) {
                return new TableCell<>() {
                    private final Button selectOptions_btn = new Button("Chọn");

                    {
                        selectOptions_btn.setOnAction((ActionEvent event) -> {
                            Options data = getTableView().getItems().get(getIndex());
                            name_options_selected.setText(data.getNameOptions());
                            color_options_selected.setText(data.getColor());
                            options = data;
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(selectOptions_btn);
                        }
                    }

                };

            }
        };
        column_options.setCellFactory(cellCallback);
        ObservableList<Options> observableLis = FXCollections.observableArrayList(optionsList);
        table_list_options.setItems(observableLis);

    }

    private void addProductForOrder() {
        if (checkIsEmpty()) {
            options.setCount(Integer.parseInt(count_sale.getText()));
            Stage stage = (Stage) confirm_btn.getScene().getWindow();
            stage.close();
        }
    }

    private boolean checkIsEmpty() {
        if (id_product_selected.getText().isEmpty()
                || name_product_selected.getText().isEmpty()
                || lo_product_selected.getText().isEmpty()
                || name_options_selected.getText().isEmpty()
                || color_options_selected.getText().isEmpty()
                || count_sale.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Lỗi !!");
            alert.setHeaderText("Lỗi");
            alert.setContentText("Nhập đầy đủ thông tin !!");
            alert.show();
            return false;
        } else {
            return true;
        }
    }

    private void formatCountTxt() {
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            String input = change.getText();
            if (input.matches("[0-9]*")) { // cho phép số và dấu chấm thập phân
                return change;
            }
            return null;
        });
        count_sale.setTextFormatter(formatter);
    }

    public void setInforProductUpdate(Options options) {
        id_product_selected.setText(options.getProduct().getId());
        name_product_selected.setText(options.getProduct().getName());
        name_options_selected.setText(options.getNameOptions());
        lo_product_selected.setText(options.getProduct().getLo());
        color_options_selected.setText(options.getColor());
        count_sale.setText(String.valueOf(options.getCount()));
    }

    private void updateSeclected() {
        if (checkIsEmpty()) {
            Stage stage = (Stage) update_btn.getScene().getWindow();
            int count = Integer.parseInt(count_sale.getText());
            options.setCount(count);
            stage.close();
        }

    }

}
