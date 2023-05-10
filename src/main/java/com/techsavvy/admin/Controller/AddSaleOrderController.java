package com.techsavvy.admin.Controller;

import entity.*;
import com.techsavvy.admin.Api.*;
import com.techsavvy.admin.Models.LocalStorage;
import com.techsavvy.admin.Models.Model;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class AddSaleOrderController implements Initializable {

    private final OrderApi orderApi = new OrderApi();

    private final EmployeeApi employeeApi = new EmployeeApi();

    private final VoucherApi voucherApi = new VoucherApi();

    private final CustomerApi customerApi = new CustomerApi();

    private final PaymentsApi paymentsApi = new PaymentsApi();

    private final ShippingApi shippingApi = new ShippingApi();

    private final LocalStorage localStorage = new LocalStorage();

    public TextField date_create;
    public TextField id_order;
    public ComboBox<String> combobox_payment;
    public ComboBox<String> combobox_status_payment;
    public Button add_product_sale_btn;
    public TableView<Options> table_product_sale;

    public TableColumn<Options, Integer> column_stt;
    public TableColumn<Options, String> column_id_product;
    public TableColumn<Options, String> column_name;
    public TableColumn<Options, String> column_type;

    public TableColumn<Options, String> column_count;
    public TableColumn<Options, String> column_price;

    public TableColumn<Options, Void> column_options;
    public Button infor_btn;

    public TextField phone_txt;
    public Button add_customer_btn;
    public Label error_phone_lbl;
    public Button check_customer_btn;
    public TextField name_customer_txt;
    public TextField address_customer_txt;
    public ComboBox<String> combobox_ship;
    public ComboBox<String> combobox_status_ship;
    public TextField tt_1_txt;
    public TextField discount_txt;
    public TextField tt_2_txt;
    public TextField price_ship_txt;
    public TextArea note_txt;
    public Button create_and_print_btn;
    public Button create_btn;
    public Button add_voucher_btn;
    private Customer customer;
    private final List<Payments> paymentsList = new ArrayList<>();
    private final List<ShippingCompany> shippingCompanyList = new ArrayList<>();
    private final List<Options> optionsList = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        onListener();
        setDate();
        try {
            setIdOrder();
            setValueForCombobox();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void onListener() {
        add_product_sale_btn.setOnAction(actionEvent -> {
            try {
                addProductSale();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        infor_btn.setOnAction(actionEvent -> {
            try {
                setInfor_btn();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        add_customer_btn.setOnAction(actionEvent -> {
            try {
                addCustomer();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        check_customer_btn.setOnAction(actionEvent -> {
            try {
                checkCustomerWithPhone();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        create_btn.setOnAction(actionEvent -> {
            try {
                createOrder();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }


    private void addProductSale() throws IOException {
        if (price_ship_txt.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Thông Báo!");
            alert.setHeaderText("Vui Lòng Nhập Thông Tin Khách Hàng !");
            alert.setContentText(null);
            alert.show();
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/AddProductForSaleOrder.fxml"));
            Parent root = fxmlLoader.load();
            AddProductForSaleOrderController controller = fxmlLoader.getController();
            controller.update_btn.setVisible(false);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait();
            setOptionsForList(controller.getOptions());
        }
    }

    private void setIdOrder() throws IOException, ClassNotFoundException {
        String id = orderApi.getRandomIdOrder();
        id_order.setText(id);
    }

    private void setDate() {
        Date date = new Date();
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = outputFormat.format(date);
        date_create.setText(formattedDate);
    }

    private boolean checkPhoneTxt() {
        if (phone_txt.getText().isEmpty()) {
            error_phone_lbl.setVisible(true);
            error_phone_lbl.setText("Nhập số điện thoại cần tìm !!");
            return false;
        }
        return true;
    }

    private void checkCustomerWithPhone() throws IOException, ClassNotFoundException {
        if (checkPhoneTxt()) {
            Customer customer = customerApi.getCustomerByPhone(phone_txt.getText());
            if (customer != null) {
                this.customer = customer;
                name_customer_txt.setText(customer.getFirstName() + " " + customer.getLastName());
                address_customer_txt.setText(customer.getAddress().getCity()
                        + ", " + customer.getAddress().getDistrict()
                        + ", " + customer.getAddress().getWards()
                        + ", " + customer.getAddress().getStreet());
                if (customer.getAddress().getCity().equals("TP.HCM") || customer.getAddress().getCity().equals("TP.Ha Noi")) {
                    price_ship_txt.setText("25,000 VND");
                } else {
                    price_ship_txt.setText("30,000 VND");
                }

            } else {
                error_phone_lbl.setVisible(true);
                error_phone_lbl.setText("Không  tìm thấy !!");
            }
        }
    }

    private void setInfor_btn() throws IOException, ClassNotFoundException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/InforCustomer.fxml"));
        Parent root = fxmlLoader.load();
        InforCustomerController controller = fxmlLoader.getController();
        if (this.customer != null) {
            controller.add_btn.setVisible(false);
            controller.setInforCustomer(this.customer);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } else {
            error_phone_lbl.setVisible(true);
            error_phone_lbl.setText("Nhập số điện khách để tìm !!");
        }
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
        stage.show();
    }

    private void setValueForCombobox() throws IOException, ClassNotFoundException {
        ObservableList<String> payment = FXCollections.observableArrayList();
        List<Payments> paymentsList = paymentsApi.getListPayments();
        for (Payments payments : paymentsList) {
            payment.add(payments.getContent());
            this.paymentsList.add(payments);
        }
        combobox_payment.setItems(payment);
        ObservableList<String> ship = FXCollections.observableArrayList();
        List<ShippingCompany> shippingCompanyList = shippingApi.getListShippingCompany();
        for (ShippingCompany shippingCompany : shippingCompanyList) {
            ship.add(shippingCompany.getName());
            this.shippingCompanyList.add(shippingCompany);
        }
        combobox_ship.setItems(ship);

        ObservableList<String> status = FXCollections.observableArrayList();
        status.addAll("Đã Thanh Toán", "Chưa Thanh Toán", "Nhận Hàng Thanh Toán");
        combobox_status_payment.setItems(status);
        combobox_status_payment.setValue("Chưa Thanh Toán");


        ObservableList<String> status_ship = FXCollections.observableArrayList();
        status_ship.addAll("Đang Vận Chuyển", "Mua Tại Cửa Hàng");
        combobox_status_ship.setItems(status_ship);
        combobox_status_ship.setValue("Đang Vận Chuyển");

    }

    private void createOrder() throws IOException, ClassNotFoundException {
        Order order = new Order();
        order.setId(id_order.getText());
        for (Payments payments : paymentsList) {
            if (combobox_payment.getValue().equals(payments.getContent())) {
                order.setPayments(payments);
            }
        }
        for (ShippingCompany shippingCompany : shippingCompanyList) {
            if (combobox_ship.getValue().equals(shippingCompany.getName())) {
                order.setShippingCompany(shippingCompany);
            }
        }
        order.setCustomer(customer);
        String maNV = localStorage.getEmployeeInLocal();
        Employee employee = employeeApi.getById(maNV);
        order.setEmployee(employee);
        order.setNotes(note_txt.getText());
        Date date = new Date();
        order.setBookingDate(date);
        order.setStatusDelivery(combobox_status_ship.getValue());
        order.setStatusPayment(combobox_status_payment.getValue().equals("Đã Thanh Toán"));
        Order order_saved = orderApi.createOrder(order);
        if (order_saved != null) {
            boolean check = false;
            OrderDetail orderDetail = new OrderDetail();
            OrderDetail orderDetail_saved;
            for (Options options : optionsList) {
                orderDetail.setOptions(options);
                orderDetail.setOrder(order_saved);
                orderDetail.setQuantity(options.getCount());
                orderDetail.setUnitPrice(Float.parseFloat(String.valueOf(options.getCount())) * options.getPrice());
                orderDetail_saved = orderApi.createOrderDetail(orderDetail);
                if (orderDetail_saved != null) {
                    check = true;
                }
            }
            if (check) {
                Model.getInstance().getViewFactory().getSelectMenuItem().set("SaleOrder");
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi!");
                alert.setHeaderText("Xảy ra lỗi khi tạo hóa đơn !");
                alert.setContentText(null);
                alert.show();
            }
        }
    }

    public void setOptionsForList(Options options) {
        this.optionsList.add(options);
        setTable_product_sale(optionsList);
    }

    private void setTable_product_sale(List<Options> options) {
        column_stt.setCellValueFactory(stt -> new ReadOnlyObjectWrapper<>(table_product_sale.getItems().indexOf(stt.getValue()) + 1));
        column_id_product.setCellValueFactory(id -> {
            String id_Product = id.getValue().getProduct().getId();
            return new SimpleStringProperty(id_Product);
        });
        column_name.setCellValueFactory(name -> {
            String name_Product = name.getValue().getProduct().getName();
            return new SimpleStringProperty(name_Product);
        });
        column_count.setCellValueFactory(sl -> {
            String count_sale = String.valueOf(sl.getValue().getCount());
            return new SimpleStringProperty(count_sale);
        });
        column_price.setCellValueFactory(price -> {
            String price_sale = String.valueOf(price.getValue().getPrice());
            return new SimpleStringProperty(price_sale);
        });
        column_type.setCellValueFactory(type -> {
            String type_product = type.getValue().getProduct().getType().getName();
            return new SimpleStringProperty(type_product);
        });
        Callback<TableColumn<Options, Void>, TableCell<Options, Void>> cellCallback = new Callback<>() {

            @Override
            public TableCell<Options, Void> call(TableColumn<Options, Void> optionsVoidTableColumn) {
                return new TableCell<>() {
                    private final Button deleteButton = new Button("Xóa");
                    private final Button infor_btn = new Button("Xem");

                    {
                        deleteButton.setOnAction((ActionEvent event) -> {
                            Options data = getTableView().getItems().get(getIndex());
                            options.remove(data);
                            optionsList.addAll(options);
                            setTT(optionsList);
                            ObservableList<Options> observableList = FXCollections.observableArrayList(options);
                            table_product_sale.setItems(observableList);
                        });

                        infor_btn.setOnAction(actionEvent -> {
                            Options options1 = getTableRow().getItem();
                            int index = getTableView().getItems().indexOf(options1);
                            try {
                                updateProductSeclected(options1, index);
                            } catch (IOException e) {
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
                            HBox container = new HBox(deleteButton, infor_btn);
                            container.setSpacing(5);
                            setGraphic(container);
                        }
                    }
                };
            }
        };

        column_options.setCellFactory(cellCallback);
        setTT(options);
        ObservableList<Options> observableList = FXCollections.observableArrayList(options);
        table_product_sale.setItems(observableList);
    }

    private void updateProductSeclected(Options options, int index) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/AddProductForSaleOrder.fxml"));
        Parent root = fxmlLoader.load();
        AddProductForSaleOrderController controller = fxmlLoader.getController();
        controller.confirm_btn.setVisible(false);
        controller.setInforProductUpdate(options);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();
        Options newOptions = controller.getOptions();
        optionsList.set(index, newOptions);
        setTT(optionsList);
        ObservableList<Options> observableList = FXCollections.observableArrayList(optionsList);
        table_product_sale.setItems(observableList);
    }

    private void setTT(List<Options> optionsList) {
        float tt = 0;
        String str = price_ship_txt.getText();
        String[] parts = str.split(" ");
        float price_ship = Float.parseFloat(parts[0].replace(",", ""));

        for (Options options : optionsList) {
            float abc = tt +=
                    Float.parseFloat(String.valueOf(options.getCount())) * options.getPrice();
            tt_1_txt.setText(formatNumber(abc) + " VND");
        }
        String abc = discount_txt.getText();
        if (!abc.isEmpty()) {
            Voucher voucher = voucherApi.getVoucherById(abc);
            float tt_2 = tt - voucher.getDiscount() + price_ship;
            tt_2_txt.setText(formatNumber(tt_2) + " VND");
        } else {
            float number_format = tt + price_ship;
            tt_2_txt.setText(formatNumber(number_format) + " VND");
        }
    }

    private String formatNumber(float number) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        return decimalFormat.format(number);
    }

    public void setInforFromOrder(Order order) throws IOException, ClassNotFoundException {
        Date date = order.getBookingDate();
        LocalDate localDate1 = LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault());
        date_create.setText(String.valueOf(localDate1));
        id_order.setText(order.getId());
        combobox_payment.setValue(order.getPayments().getContent());
        combobox_ship.setValue(order.getShippingCompany().getName());
        if (order.isStatusPayment()) {
            combobox_status_payment.setValue("Đã Thanh Toán");
        } else {
            combobox_status_payment.setValue("Chưa Thanh Toán");
        }
        combobox_status_ship.setValue(order.getStatusDelivery());
        note_txt.setText(order.getNotes());
        this.customer = order.getCustomer();
        name_customer_txt.setText(order.getCustomer().getFirstName() + " " + order.getCustomer().getLastName());
        phone_txt.setText(customer.getPhone());
        address_customer_txt.setText(customer.getAddress().getCity()
                + ", " + customer.getAddress().getDistrict()
                + ", " + customer.getAddress().getWards()
                + ", " + customer.getAddress().getStreet());

        if (customer.getAddress().getCity().equals("TP.HCM") || customer.getAddress().getCity().equals("TP.Ha Noi")) {
            price_ship_txt.setText("25,000 VND");
        } else {
            price_ship_txt.setText("30,000 VND");
        }
        List<Options> options = new ArrayList<>();
        List<OrderDetail> orderDetailList = orderApi.getByOrder(order.getId());
        Options options1;
        if (orderDetailList != null) {
            for (OrderDetail orderDetail : orderDetailList) {
                options1 = orderDetail.getOptions();
                options1.setCount(orderDetail.getQuantity());
                options.add(options1);
            }
            optionsList.addAll(options);
            setTable_product_sale(optionsList);
            setTT(optionsList);
        }
    }

}
