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
import java.util.*;

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

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private void getInforCustomer(String email) throws IOException, ClassNotFoundException {
        Customer customer = customerApi.getCustomerByEmail(email);
        if (customer != null) {
            setCustomer(customer);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Thông báo");
            alert.setHeaderText("Không tìm thấy khách hàng " + email);
            alert.show();
        }
    }

    public void setTable_evaluate(List<Evaluate> evaluateList) {

        column_stt.setCellValueFactory(stt -> new ReadOnlyObjectWrapper<>(table_evaluate.getItems().indexOf(stt.getValue()) + 1));
        column_email.setCellValueFactory(column -> {
            String email = column.getValue().getAccount().getEmail();
            String email_customer = "";
            try {
                getInforCustomer(email);
                if (customer != null) {
                    email_customer = customer.getEmail();
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            return new SimpleStringProperty(email_customer);
        });
        column_id.setCellValueFactory(column -> {
            String id = "";
            if (customer != null) {
                id = customer.getId();
            }
            return new SimpleStringProperty(id);

        });
        column_name.setCellValueFactory(column -> {
            String name = "";
            if (customer != null) {
                name = customer.getFirstName() + " " + customer.getLastName();
            }
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
            String value = column.getValue().getValue() + "/5⭐";
            return new SimpleStringProperty(value);
        });
        Callback<TableColumn<Evaluate, Void>, TableCell<Evaluate, Void>> cellCallback = new Callback<>() {
            @Override
            public TableCell<Evaluate, Void> call(TableColumn<Evaluate, Void> productVoidTableColumn) {
                return new TableCell<>() {
                    private final Button inforImportOrder_btn = new Button("Xóa Đánh Giá");

                    {
                        inforImportOrder_btn.setOnAction(actionEvent -> {
                            Evaluate data = getTableView().getItems().get(getIndex());
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Bạn có muốn xóa đánh giá này ?");
                            alert.setHeaderText(null);
                            Optional<ButtonType> optionalButtonType = alert.showAndWait();
                            if (optionalButtonType.isPresent()) {
                                try {
                                    evaluateApi.deleteEvaluate(data.getId());
                                    evaluateList.remove(data);
                                    ObservableList<Evaluate> evaluateObservableList = FXCollections.observableArrayList(evaluateList);
                                    table_evaluate.setItems(evaluateObservableList);
                                } catch (IOException | ClassNotFoundException | InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
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
