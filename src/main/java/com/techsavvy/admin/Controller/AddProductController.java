package com.techsavvy.admin.Controller;

import entity.Options;
import entity.Product;
import entity.Type;
import com.techsavvy.admin.Api.ProductApi;
import com.techsavvy.admin.Api.TypeApi;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.Setter;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

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
    public TableColumn<Options, Integer> column_stt;
    public TableColumn<Options, String> column_name;
    public TableColumn<Options, String> column_color;
    public TableColumn<Options, String> column_priceImport;
    public TableColumn<Options, String> column_priceSale;
    public TableView<Options> table_Options_Product;
    public Button add_options;
    public TextField name_Options_txt;
    public TextField color_txt;
    public TableColumn<Options, Void> column_options;
    public TextField count_Options_txt;
    public TableColumn<Options, String> column_count;

    private final AtomicInteger index = new AtomicInteger(0);

    private List<Options> optionsList = new ArrayList<>();

    private Product product;

    private boolean isEdited;

    public Product getProduct() {
        return product;
    }

    public boolean isEdited() {
        return isEdited;
    }

    public List<Options> getOptionsList() {
        return optionsList;
    }

    public void setOptionsList(List<Options> optionsList) {
        this.optionsList = optionsList;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCombobox();
        formatTextFieldCount();
        formatTextFieldPriceImport();
        formatTextFieldCountOptions();
        formatTextFieldPriceSale();
        try {
            getIdProduct();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        onListener();
    }

    private void onListener() {
        cancel_btn.setOnAction(actionEvent -> cancel());
        confirm_btn.setOnAction(actionEvent -> {
            try {
                setNewProductToList();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        add_options.setOnAction(actionEvent -> addOptions());
        setInforOptionsInText();
    }


    private void getIdProduct() throws IOException, ClassNotFoundException {
        String idProduct = productApi.getRandomId();
        id_Product.setText(idProduct);
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


    public void setNewProductToList() throws IOException, ClassNotFoundException {
        if (nameProduct_txt.getText() != null
                || origin_txt.getText() != null
                || count_txt.getText() != null) {
            product = new Product();
            product.setId(id_Product.getText());
            product.setLo(lo_txt.getText());
            product.setOrigin(origin_txt.getText());
            product.setName(nameProduct_txt.getText());
            Type type = typeApi.getByName(combobox_type.getValue());
            product.setType(type);
            product.setCounts(Integer.parseInt(count_txt.getText()));
            boolean status;
            status = combobox_status.getValue().equals("Kinh doanh");
            product.setStatus(status);
            isEdited = true;
            for (Options options : optionsList) {
                options.setProduct(product);
            }
            Stage stage = (Stage) confirm_btn.getScene().getWindow();
            stage.close();

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi!!");
            alert.setContentText("Các field không được trống!!");
            alert.show();
        }

    }

    private void cancel() {
        product = null;
        optionsList = null;
        Stage stage = (Stage) cancel_btn.getScene().getWindow();
        stage.close();
    }


    public void setProduct(Product product) {
        this.product = product;
        nameProduct_txt.setText(product.getName());
        count_txt.setText(String.valueOf(product.getCounts()));
        if (product.isStatus()) {
            combobox_status.setValue("Kinh doanh");
        } else {
            combobox_status.setValue("Không kinh doanh");
        }
        combobox_type.setValue(product.getType().getName());
        lo_txt.setText(product.getLo());
        origin_txt.setText(product.getOrigin());
        id_Product.setText(product.getId());
        ObservableList<Options> observableList = FXCollections.observableArrayList();
        table_Options_Product.setItems(observableList);
    }

    private void formatTextFieldPriceImport() {
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            String input = change.getText();
            if (input.matches("[0-9]*\\.?[0-9]*")) { // cho phép số và dấu chấm thập phân
                return change;
            }
            return null;
        });
        priceImport_txt.setTextFormatter(formatter);
    }

    private void removeValueText() {
        name_Options_txt.setText("");
        priceImport_txt.setText("");
        priceSale_txt.setText("");
        color_txt.setText("");
        count_Options_txt.setText("");
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

    private void formatTextFieldCountOptions() {
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            String input = change.getText();
            if (input.matches("[0-9]*")) {
                return change;
            }
            return null;
        });
        count_Options_txt.setTextFormatter(formatter);
    }

    private void formatTextFieldPriceSale() {
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            String input = change.getText();
            if (input.matches("[0-9]*\\.?[0-9]*")) { // cho phép số và dấu chấm thập phân
                return change;
            }
            return null;
        });
        priceSale_txt.setTextFormatter(formatter);
    }

    public void setLH(String lh) {
        lo_txt.setText(lh);
    }

    private Options getInforOptions() {
        Options options = new Options();
        if (name_Options_txt.getText() == null
                || color_txt.getText() == null
                || priceImport_txt.getText() == null
                || priceSale_txt.getText() == null) {
            options = null;
        } else {
            options.setNameOptions(name_Options_txt.getText());
            options.setPrice(Float.parseFloat(priceSale_txt.getText()));
            options.setPriceImport(Float.parseFloat(priceImport_txt.getText()));
            options.setColor(color_txt.getText());
            options.setCount(Integer.parseInt(count_Options_txt.getText()));

        }
        System.out.println("options: " + options);
        return options;
    }

    public void setTableOptions(List<Options> optionsListForTable) {
        column_stt.setCellValueFactory(stt -> new ReadOnlyObjectWrapper<>(table_Options_Product.getItems().indexOf(stt.getValue()) + 1));
        column_name.setCellValueFactory(name -> {
            String nameOptions = name.getValue().getNameOptions();
            return new SimpleStringProperty(nameOptions);
        });
        column_color.setCellValueFactory(color -> {
            String colorProduct = color.getValue().getColor();
            return new SimpleStringProperty(colorProduct);
        });
        column_priceImport.setCellValueFactory(price_import -> {
            String importPrice = String.valueOf(price_import.getValue().getPriceImport());
            return new SimpleStringProperty(importPrice);
        });
        column_priceSale.setCellValueFactory(sale -> {
            String sale_price = String.valueOf(sale.getValue().getPrice());
            return new SimpleStringProperty(sale_price);
        });
        column_count.setCellValueFactory(count -> {
            String count_options = String.valueOf(count.getValue().getCount());
            return new SimpleStringProperty(count_options);
        });
        Callback<TableColumn<Options, Void>, TableCell<Options, Void>> cellCallback = new Callback<>() {
            @Override
            public TableCell<Options, Void> call(TableColumn<Options, Void> optionsVoidTableColumn) {
                return new TableCell<>() {
                    private final Button deleteButton = new Button("Xóa");

                    {
                        deleteButton.setOnAction((ActionEvent event) -> {
                            Options data = getTableView().getItems().get(getIndex());
                            optionsList.remove(data);
                            table_Options_Product.getItems().setAll(optionsList);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        deleteButton.setAlignment(Pos.CENTER);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(deleteButton);
                        }
                    }

                };
            }
        };
        column_options.setCellFactory(cellCallback);
        ObservableList<Options> observableList = FXCollections.observableArrayList(optionsListForTable);
        table_Options_Product.setItems(observableList);
    }

    private void setInforOptionsInText() {
        table_Options_Product.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) {
                Options options = table_Options_Product.getSelectionModel().getSelectedItem();
                name_Options_txt.setText(options.getNameOptions());
                priceSale_txt.setText(String.valueOf(options.getPrice()));
                priceImport_txt.setText(String.valueOf(options.getPriceImport()));
                color_txt.setText(options.getColor());
                count_Options_txt.setText(String.valueOf(options.getCount()));
                int a = table_Options_Product.getSelectionModel().getSelectedIndex();
                index.set(a);
            }
        });
    }

    private void addOptions() {
        Options options = getInforOptions();
        boolean isCheck = false;
        if (options != null) {
            for (Options optionsInList : optionsList) {
                isCheck = options.getColor().equals(optionsInList.getColor()) && options.getNameOptions().equals(optionsInList.getNameOptions());
            }
            if (isCheck) {
                optionsList.set(index.get(), options);
                setTableOptions(optionsList);
                removeValueText();
                System.out.println("List Options after  update" + optionsList);
            } else {
                optionsList.add(options);
                setTableOptions(optionsList);
                removeValueText();
                System.out.println("List Options after  add" + optionsList);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi!!");
            alert.setHeaderText("Lỗi Khi Thêm Phiên Bản !!");
            alert.showAndWait();
        }
    }

}
