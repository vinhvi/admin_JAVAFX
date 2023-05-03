package com.techsavvy.admin.Controller;

import com.techsavvy.admin.Api.CustomerApi;
import com.techsavvy.admin.entity.Customer;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class ListCustomerController implements Initializable {

    private final CustomerApi customerApi = new CustomerApi();

    public Button search_btn;
    public TextField search_txt;
    public Button add_btn;
    public TableView<Customer> table_customer;

    public TableColumn<Customer, Integer> column_stt;
    public TableColumn<Customer, String> column_id;

    public TableColumn<Customer, String> column_name;
    public TableColumn<Customer, String> column_email;
    public TableColumn<Customer, String> column_sdt;
    public TableColumn<Customer, String> column_sex;
    public TableColumn<Customer, String> column_DateOfBirth;
    public TableColumn<Customer, String> column_address;

    public TableColumn<Customer, Void> column_options;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            setListCustomer();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        onListener();
    }

    private void setListCustomer() throws IOException, ClassNotFoundException {
        List<Customer> customerList = customerApi.getListCustomer();
        setTable_customer(customerList);

    }

    private void onListener() {
        add_btn.setOnAction(actionEvent -> {
            try {
                addCustomer();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void setTable_customer(List<Customer> customerList) {
        column_stt.setCellValueFactory(column -> new ReadOnlyObjectWrapper<>(table_customer.getItems().indexOf(column.getValue()) + 1));
        column_id.setCellValueFactory(column -> {
            String id = column.getValue().getId();
            return new SimpleStringProperty(id);
        });
        column_name.setCellValueFactory(column -> {
            String name = column.getValue().getFirstName() + " " + column.getValue().getLastName();
            return new SimpleStringProperty(name);
        });
        column_email.setCellValueFactory(column -> {
            String email = column.getValue().getEmail();
            return new SimpleStringProperty(email);
        });
        column_sdt.setCellValueFactory(column -> {
            String sdt = column.getValue().getPhone();
            return new SimpleStringProperty(sdt);
        });
        column_address.setCellValueFactory(column -> {
            String address = column.getValue().getAddress().getCity()
                    + ", " + column.getValue().getAddress().getDistrict()
                    + ", " + column.getValue().getAddress().getWards()
                    + ", " + column.getValue().getAddress().getStreet();
            return new SimpleStringProperty(address);
        });

        column_sex.setCellValueFactory(column -> {
            String sex;
            if (column.getValue().getSex() == 1) {
                sex = "Nam";
            } else {
                sex = "Nữ";
            }
            return new SimpleStringProperty(sex);
        });

        column_DateOfBirth.setCellValueFactory(column -> {
            Date date = column.getValue().getDateOfBirth();
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

        Callback<TableColumn<Customer, Void>, TableCell<Customer, Void>> cellCallback = new Callback<>() {
            @Override
            public TableCell<Customer, Void> call(TableColumn<Customer, Void> productVoidTableColumn) {
                return new TableCell<>() {
                    private final Button inforImportOrder_btn = new Button("Xem thông tin");

                    {
                        inforImportOrder_btn.setOnAction(actionEvent -> {
                            Customer data = getTableView().getItems().get(getIndex());
                            try {
                                inforCustomer(data);
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
        column_options.setCellFactory(cellCallback);
        ObservableList<Customer> observableList = FXCollections.observableArrayList(customerList);
        table_customer.setItems(observableList);
    }

    private void inforCustomer(Customer customer) throws IOException, ClassNotFoundException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/InforCustomer.fxml"));
        Parent root = fxmlLoader.load();
        InforCustomerController controller = fxmlLoader.getController();
        controller.setInforCustomer(customer);
        controller.add_btn.setVisible(false);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();
        setListCustomer();
    }

    private void addCustomer() throws IOException, ClassNotFoundException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/InforCustomer.fxml"));
        Parent root = fxmlLoader.load();
        InforCustomerController controller = fxmlLoader.getController();
        controller.confirm_btn.setVisible(false);
        String idCustomer = customerApi.getRandomId();
        controller.id_txt.setText(idCustomer);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();
        setListCustomer();
    }
}
