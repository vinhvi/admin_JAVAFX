package com.techsavvy.admin.Controller;

import com.techsavvy.admin.Api.CustomerApi;
import com.techsavvy.admin.Api.EvaluateApi;
import com.techsavvy.admin.entity.Customer;
import com.techsavvy.admin.entity.Evaluate;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class ListEvaluateController implements Initializable {
    private final CustomerApi customerApi = new CustomerApi();
    private final EvaluateApi evaluateApi = new EvaluateApi();
    public TableView<Evaluate> table_evaluate;

    public TableColumn<Evaluate, Integer> column_stt;
    public TableColumn<Evaluate, String> column_id;

    public TableColumn<Evaluate, String> column_name;

    public TableColumn<Evaluate, String> column_email;
    public TableColumn<Evaluate, String> column_phone;
    public TableColumn<Evaluate, String> column_date;
    public TableColumn<Evaluate, String> column_content;
    public TableColumn<Evaluate, String> column_value;
    public TableColumn<Evaluate, Void> column_option;
    private Customer customer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setTable_evaluate(List<Evaluate> evaluateList) {

        column_stt.setCellValueFactory(stt -> new ReadOnlyObjectWrapper<>(table_evaluate.getItems().indexOf(stt.getValue()) + 1));
        column_id.setCellValueFactory(column -> {
            String id = "";
            try {
                this.customer = customerApi.getCustomerByEmail(column.getValue().getAccount().getEmail());
                id = customer.getId();
                return new SimpleStringProperty(id);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        column_name.setCellValueFactory(column -> {
            String name = customer.getFirstName() + " " + customer.getLastName();
            return new SimpleStringProperty(name);
        });
        column_date.setCellValueFactory(column -> {
            Date date = column.getValue().getEvaluationDate();
            SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
            String formattedDate = "";
            try {
                Date date2 = inputFormat.parse(String.valueOf(date));
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
                formattedDate = outputFormat.format(date2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return new SimpleStringProperty(formattedDate);
        });
        column_content.setCellValueFactory(column -> {
            String nd = column.getValue().getContent();
            return new SimpleStringProperty(nd);
        });
        column_phone.setCellValueFactory(column -> {
            String phone = customer.getPhone();
            return new SimpleStringProperty(phone);
        });
        column_value.setCellValueFactory(column -> {
            String value = column.getValue() + "/5 ⭐";
            return new SimpleStringProperty(value);
        });
        Callback<TableColumn<Evaluate, Void>, TableCell<Evaluate, Void>> cellCallback = new Callback<>() {
            @Override
            public TableCell<Evaluate, Void> call(TableColumn<Evaluate, Void> productVoidTableColumn) {
                return new TableCell<>() {
                    private final Button inforImportOrder_btn = new Button("Xem thông tin");

                    {
                        inforImportOrder_btn.setOnAction(actionEvent -> {
                            Evaluate data = getTableView().getItems().get(getIndex());
                            try {
                                boolean check = evaluateApi.deleteById(data.getId());
                                if (check) {
                                    evaluateList.remove(data);
                                    ObservableList<Evaluate> evaluateObservableList = FXCollections.observableArrayList(evaluateList);
                                    table_evaluate.setItems(evaluateObservableList);
                                } else {
                                    System.out.println("Lỗi !!");
                                }
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
                            setGraphic(inforImportOrder_btn);
                        }
                    }
                };
            }
        };
        column_option.setCellFactory(cellCallback);
        ObservableList<Evaluate> evaluateObservableList = FXCollections.observableArrayList(evaluateList);
        table_evaluate.setItems(evaluateObservableList);
    }

}
