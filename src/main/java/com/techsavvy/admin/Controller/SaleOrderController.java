package com.techsavvy.admin.Controller;

import entity.*;
import com.techsavvy.admin.Api.OrderApi;
import com.techsavvy.admin.Models.Model;
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

public class SaleOrderController implements Initializable {

    private final OrderApi orderApi = new OrderApi();

    public Button creatOrder_btn;
    public Button search_btn;
    public TextField search_txt;
    public TableView<Order> table_Order;

    public TableColumn<Order, Integer> column_stt;
    public TableColumn<Order, String> column_id;
    public TableColumn<Order, String> column_name_customer;
    public TableColumn<Order, String> column_email;
    public TableColumn<Order, String> column_sdt;
    public TableColumn<Order, String> column_tt;
    public TableColumn<Order, String> column_status_payment;
    public TableColumn<Order, String> column_booking_date;
    public TableColumn<Order, String> column_status_ship;

    public TableColumn<Order, Void> column_options;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListener();
        try {
            setLisOrder();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void addListener() {
        creatOrder_btn.setOnAction(actionEvent -> onAddOrder());
    }

    private void onAddOrder() {
        Model.getInstance().getViewFactory().getSelectMenuItem().set("CreateSaleOrder");
    }

    private void setLisOrder() throws IOException, ClassNotFoundException {
        List<Order> orderList = orderApi.getListOrder();
        setTable_Order(orderList);
    }

    private void setTable_Order(List<Order> orderList) {
        column_stt.setCellValueFactory(column -> new ReadOnlyObjectWrapper<>(table_Order.getItems().indexOf(column.getValue()) + 1));
        column_id.setCellValueFactory(column -> {
            String id = column.getValue().getId();
            return new SimpleStringProperty(id);
        });
        column_name_customer.setCellValueFactory(column -> {
            String name = column.getValue().getCustomer().getFirstName() + " " + column.getValue().getCustomer().getLastName();
            return new SimpleStringProperty(name);
        });
        column_email.setCellValueFactory(column -> {
            String email = column.getValue().getCustomer().getEmail();
            return new SimpleStringProperty(email);
        });
        column_sdt.setCellValueFactory(column -> {
            String sdt = column.getValue().getCustomer().getPhone();
            return new SimpleStringProperty(sdt);
        });
        column_tt.setCellValueFactory(column -> {
            String total = String.valueOf(column.getValue().getTotalMoney());
            return new SimpleStringProperty(total);
        });

        column_status_ship.setCellValueFactory(column -> {
            String status = column.getValue().getStatusDelivery();
            return new SimpleStringProperty(status);
        });

        column_status_payment.setCellValueFactory(column -> {
            String status;
            if (column.getValue().isStatusPayment()) {
                status = "Đã Thanh Toán";
            } else {
                status = "Chưa Thanh Toán";
            }
            return new SimpleStringProperty(status);
        });

        column_booking_date.setCellValueFactory(column -> {
            Date date = column.getValue().getBookingDate();
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

        Callback<TableColumn<Order, Void>, TableCell<Order, Void>> cellCallback = new Callback<>() {
            @Override
            public TableCell<Order, Void> call(TableColumn<Order, Void> productVoidTableColumn) {
                return new TableCell<>() {
                    private final Button inforImportOrder_btn = new Button("Xem thông tin");

                    {
                        inforImportOrder_btn.setOnAction(actionEvent -> {
                            Order data = getTableView().getItems().get(getIndex());
                            try {
                                inforOrder(data);
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
        ObservableList<Order> observableList = FXCollections.observableArrayList(orderList);
        table_Order.setItems(observableList);
    }

    private void inforOrder(Order order) throws IOException, ClassNotFoundException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/CreateSaleOrder.fxml"));
        Parent root = fxmlLoader.load();
        AddSaleOrderController controller = fxmlLoader.getController();
        controller.check_customer_btn.setVisible(false);
        controller.add_customer_btn.setVisible(false);
        controller.create_and_print_btn.setText("In Hóa Đơn");
        controller.create_btn.setText("Cập Nhật");
        controller.add_product_sale_btn.setVisible(false);
        controller.setInforFromOrder(order);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();
        setLisOrder();
    }
}
