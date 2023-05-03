package com.techsavvy.admin.Controller;

import com.techsavvy.admin.Api.ImportOrderApi;
import com.techsavvy.admin.Models.Model;
import com.techsavvy.admin.entity.ImportOrder;
import com.techsavvy.admin.entity.ImportOrderDetail;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class ImportOrderController implements Initializable {


    private final ImportOrderApi importOrderApi = new ImportOrderApi();

    public Button search_btn;
    public Button add_btn;
    public TableView<ImportOrder> table_importOrder;
    public TableColumn<ImportOrder, Integer> column_stt;
    public TableColumn<ImportOrder, String> column_id;
    public TableColumn<ImportOrder, String> column_nameSupplier;
    public TableColumn<ImportOrder, String> column_email;
    public TableColumn<ImportOrder, String> column_sdt;
    public TableColumn<ImportOrder, String> column_date;
    public TableColumn<ImportOrder, String> column_tt;
    public TableColumn<ImportOrder, Void> column_options;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setDataForTable();
        addListener();
        try {
            getListImportOrder();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void addListener() {
        add_btn.setOnAction(actionEvent -> onAddOrder());
    }

    private void onAddOrder() {
        Model.getInstance().getViewFactory().getSelectMenuItem().set("CreateImportOrder");
    }


    private void getListImportOrder() throws IOException, ClassNotFoundException {
        List<ImportOrder> importOrders = importOrderApi.getListImportOrder();
        ObservableList<ImportOrder> observableList = FXCollections.observableArrayList(importOrders);
        table_importOrder.setItems(observableList);
    }

    private String getTTImportOrder(String ma) throws IOException, ClassNotFoundException {
        List<ImportOrderDetail> importOrderDetails = importOrderApi.getListImportOrderDetailByImportOrderId(ma);
        float tt_importDetail = 0;
        for (ImportOrderDetail importOrderDetail : importOrderDetails) {
            tt_importDetail += importOrderDetail.getPrice();
        }
        float number = tt_importDetail;
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
        return decimalFormat.format(number);
    }

    private void setDataForTable() {
        column_stt.setCellValueFactory(stt -> new ReadOnlyObjectWrapper<>(table_importOrder.getItems().indexOf(stt.getValue()) + 1));
        column_id.setCellValueFactory(id -> {
            String ma = id.getValue().getId();
            return new SimpleStringProperty(ma);
        });
        column_date.setCellValueFactory(date -> {
            Date dateImport = date.getValue().getImportDate();
            SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
            String formattedDate = "";
            try {
                Date date2 = inputFormat.parse(String.valueOf(dateImport));
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
                formattedDate = outputFormat.format(date2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return new SimpleStringProperty(formattedDate);
        });
        column_sdt.setCellValueFactory(sdt -> {
            String phone = sdt.getValue().getSupplier().getPhone();
            return new SimpleStringProperty(phone);
        });
        column_email.setCellValueFactory(email -> {
            String mail = email.getValue().getSupplier().getEmail();
            return new SimpleStringProperty(mail);
        });
        column_nameSupplier.setCellValueFactory(name -> {
            String nameSupplier = name.getValue().getSupplier().getName();
            return new SimpleStringProperty(nameSupplier);
        });

        column_tt.setCellValueFactory(tt -> {
            try {
                String tt_importOrder = getTTImportOrder(tt.getValue().getId());
                return new SimpleStringProperty(tt_importOrder + " VND");
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        });

        Callback<TableColumn<ImportOrder, Void>, TableCell<ImportOrder, Void>> cellCallback = new Callback<>() {
            @Override
            public TableCell<ImportOrder, Void> call(TableColumn<ImportOrder, Void> importOrderVoidTableColumn) {
                return new TableCell<>() {
                    private final Button inforImportOrder_btn = new Button("Xem thông tin");

                    {
                        inforImportOrder_btn.setOnAction(actionEvent -> {
                            ImportOrder importOrder = getTableView().getItems().get(getIndex());
                            System.out.println(importOrder);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(inforImportOrder_btn);
                        }
                    }
                };
            }
        };

        column_options.setCellFactory(cellCallback);

    }

}
