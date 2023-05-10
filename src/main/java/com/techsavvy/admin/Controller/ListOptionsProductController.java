package com.techsavvy.admin.Controller;

import com.techsavvy.admin.Api.OptionsApi;
import com.techsavvy.admin.Models.Model;
import entity.Options;
import entity.Product;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

public class ListOptionsProductController implements Initializable {
    private final OptionsApi optionsApi = new OptionsApi();
    public TextField name_Options_txt;
    public TextField color_Options_txt;
    public TextField count_Options_txt;
    public TextField priceSale_txt;
    public TextField priceImport_txt;
    public Button btn_edit;
    public TableView<Options> table_Options;

    public TableColumn<Options, Integer> column_stt;
    public TableColumn<Options, String> column_name;
    public TableColumn<Options, String> column_color;
    public TableColumn<Options, String> column_count;
    public TableColumn<Options, String> column_price_sale;
    public TableColumn<Options, String> column_price_import;
    public Button btn_confirm;
    public Button btn_cancel;
    private final AtomicInteger index = new AtomicInteger(0);
    private List<Options> optionsList = new ArrayList<>();

    private Product product;

    public Product getProduct() {
        return product;
    }

    private Options optionsInTable;

    public List<Options> getOptionsList() {
        return optionsList;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setDataForTable();
        formatTextFieldPriceSale();
        formatTextFieldImport();
        onListener();
        onClickTable();
        formatTextFieldCount();
    }

    private void onListener() {
        btn_edit.setOnAction(actionEvent -> updateInTable());
        btn_confirm.setOnAction(actionEvent -> {
            try {
                updateOptionsInDatabase();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        btn_cancel.setOnAction(actionEvent -> close());

    }

    private void close() {
        Stage stage = (Stage) btn_cancel.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
    }

    private void updateOptionsInDatabase() throws IOException, ClassNotFoundException {
        boolean isUpdate = false;
        for (Options options : optionsList) {
            if (optionsApi.updateOptions(options)) {
                isUpdate = true;
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Cảnh báo");
                alert.setHeaderText("Lỗi!!");
                alert.showAndWait();
            }
        }
        if (isUpdate) {
            close();
        }

    }

    private String formatFloat(float abc) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        return decimalFormat.format(abc);
    }

//    private String formatFloat(float value) {
//        DecimalFormat decimalFormat = new DecimalFormat("#,##0.##");
//        String[] suffix = new String[]{"", "K", "M", "B", "T"};
//        int index = 0;
//        while (value >= 1000 && index < suffix.length - 1) {
//            value /= 1000;
//            index++;
//        }
//        return decimalFormat.format(value) + suffix[index];
//    }

    public void setOptionsListForTable(Product product) throws IOException, ClassNotFoundException {
        this.product = product;
        this.optionsList = optionsApi.getOptionsByProduct(this.product.getId());
        System.out.println("List Options for database: " + optionsList);
        ObservableList<Options> observableList = FXCollections.observableArrayList(optionsList);
        table_Options.setItems(observableList);
    }

    private void setDataForTable() {
        column_stt.setCellValueFactory(stt -> new ReadOnlyObjectWrapper<>(table_Options.getItems().indexOf(stt.getValue()) + 1));
        column_name.setCellValueFactory(name -> {
            String nameOptions = name.getValue().getNameOptions();
            return new SimpleStringProperty(nameOptions);
        });
        column_color.setCellValueFactory(color -> {
            String color_Options = color.getValue().getColor();
            return new SimpleStringProperty(color_Options);
        });
        column_count.setCellValueFactory(count -> {
            int count_Options = count.getValue().getCount();
            return new SimpleStringProperty(String.valueOf(count_Options));
        });
        column_price_sale.setCellValueFactory(sale -> {
            String price = formatFloat(sale.getValue().getPrice());
            return new SimpleStringProperty(price);
        });
        column_price_import.setCellValueFactory(import_price -> {
            String price = formatFloat(import_price.getValue().getPriceImport());
            return new SimpleStringProperty(price);
        });

    }

    private void onClickTable() {
        table_Options.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) {
                Options options = table_Options.getSelectionModel().getSelectedItem();
                name_Options_txt.setText(options.getNameOptions());
                color_Options_txt.setText(options.getColor());
                count_Options_txt.setText(String.valueOf(options.getCount()));
                priceSale_txt.setText(String.valueOf(options.getPrice()));
                priceImport_txt.setText(String.valueOf(options.getPriceImport()));
                optionsInTable = options;
                int index1 = table_Options.getSelectionModel().getSelectedIndex();
                index.set(index1); // Cập nhật giá trị biến index
            }
        });
    }


    private Boolean check() {
        if (name_Options_txt.getText() == null
                || color_Options_txt.getText() == null
                || count_Options_txt.getText() == null
                || priceSale_txt.getText() == null
                || priceImport_txt.getText() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Nhập đầy đủ thông tin !!");
            return false;
        } else {
            return true;
        }
    }

    private void setDefaultTxt() {
        name_Options_txt.setText("");
        color_Options_txt.setText("");
        count_Options_txt.setText("");
        priceImport_txt.setText("");
        priceSale_txt.setText("");

    }

    private void updateInTable() {
        Options options = getOptionInForm();
        if (options != null) {
            options.setId(optionsInTable.getId());
            optionsList.set(index.get(), options);
            ObservableList<Options> observableList = FXCollections.observableArrayList(optionsList);
            table_Options.setItems(observableList);
            setDefaultTxt();
            System.out.println("Options after update for table: " + options);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cảnh báo");
            alert.setHeaderText("Thông tin không được trống");
            alert.showAndWait();
        }
    }

    private Options getOptionInForm() {
        Options options = new Options();
        if (check()) {
            options.setNameOptions(name_Options_txt.getText());
            options.setColor(color_Options_txt.getText());
            options.setCount(Integer.parseInt(count_Options_txt.getText()));
            options.setPrice(Float.parseFloat(priceSale_txt.getText()));
            options.setPriceImport(Float.parseFloat(priceImport_txt.getText()));
            return options;
        }
        return null;
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

    private void formatTextFieldCount() {
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            String input = change.getText();
            if (input.matches("[0-9]*\\.?[0-9]*")) { // cho phép số và dấu chấm thập phân
                return change;
            }
            return null;
        });
        count_Options_txt.setTextFormatter(formatter);
    }

    private void formatTextFieldImport() {
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            String input = change.getText();
            if (input.matches("[0-9]*\\.?[0-9]*")) { // cho phép số và dấu chấm thập phân
                return change;
            }
            return null;
        });
        priceImport_txt.setTextFormatter(formatter);
    }

}
