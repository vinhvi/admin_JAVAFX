package com.techsavvy.admin.Controller;

import com.techsavvy.admin.Api.CustomerApi;
import com.techsavvy.admin.Api.RoleApi;
import entity.Account;
import entity.Address;
import entity.Customer;
import entity.Role;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class InforCustomerController implements Initializable {

    private final CustomerApi customerApi = new CustomerApi();

    private final RoleApi roleApi = new RoleApi();



    public TextField id_txt;
    public TextField first_name_txt;
    public TextField last_name_txt;
    public ComboBox<String> combobox_sex;
    public TextField email_txt;
    public TextField phone_txt;
    public TextField score_txt;
    public DatePicker date_of_birth_txt;
    public TextField city_txt;
    public TextField district_txt;
    public TextField wards_txt;
    public TextField street_txt;
    public ComboBox<String> combobox_type;
    public Button cancel_btn;
    public Button confirm_btn;
    public Button add_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setValueForCombobox();
        onListener();
    }

    private void onListener() {
        cancel_btn.setOnAction(actionEvent -> onClose());
        add_btn.setOnAction(actionEvent -> {
            try {
                create();
            } catch (IOException | ClassNotFoundException | ParseException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void onClose() {
        Stage stage = (Stage) cancel_btn.getScene().getWindow();
        stage.close();
    }

    public void setInforCustomer(Customer customer) {
        id_txt.setText(customer.getId());
        first_name_txt.setText(customer.getFirstName());
        last_name_txt.setText(customer.getLastName());
        if (customer.getSex() == 1) {
            combobox_sex.setValue("Nam");
        } else {
            combobox_sex.setValue("Nữ");
        }
        combobox_type.setValue(customer.getTypeCustomer());
        email_txt.setText(customer.getEmail());
        phone_txt.setText(customer.getPhone());
        score_txt.setText(String.valueOf(customer.getScore()));
        city_txt.setText(customer.getAddress().getCity());
        district_txt.setText(customer.getAddress().getDistrict());
        wards_txt.setText(customer.getAddress().getWards());
        street_txt.setText(customer.getAddress().getStreet());
        Date date = customer.getDateOfBirth();
        LocalDate localDate = LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault());
        date_of_birth_txt.setValue(localDate);

    }

    private void setValueForCombobox() {
        ObservableList<String> sex = FXCollections.observableArrayList();
        sex.addAll("Nam", "Nữ");
        combobox_sex.setValue("Nữ");
        combobox_sex.setItems(sex);

        ObservableList<String> type = FXCollections.observableArrayList();
        type.addAll("Khách Hàng Vãng Lai", "Khách Hàng Thành Viên");
        combobox_type.setItems(type);
        combobox_type.setValue("Khách Hàng Vãng Lai");
    }

    private boolean checkIsEmpty() {
        if (id_txt.getText().isEmpty()
                || email_txt.getText().isEmpty()
                || phone_txt.getText().isEmpty()
                || city_txt.getText().isEmpty()
                || district_txt.getText().isEmpty()
                || street_txt.getText().isEmpty()
                || wards_txt.getText().isEmpty()
                || first_name_txt.getText().isEmpty()
                || last_name_txt.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Thông Báo!!");
            alert.setHeaderText("Nhập đầy đủ thông  tin Khách Hàng!!");
            alert.show();
            return false;
        } else {
            return true;
        }
    }

    private void create() throws IOException, ClassNotFoundException, ParseException {
        if (checkIsEmpty()) {
            Customer newCustomer = new Customer();
            Account account = new Account();
            newCustomer.setId(id_txt.getText());
            newCustomer.setEmail(email_txt.getText());
            newCustomer.setPhone(phone_txt.getText());

            if (combobox_sex.getValue().equals("Nam")) {
                newCustomer.setSex(1);
            } else {
                newCustomer.setSex(0);
            }
            newCustomer.setTypeCustomer(combobox_type.getValue());
            if (combobox_type.getValue().equals("Khách Hàng Vãng Lai")) {
                newCustomer.setAccount(account);
                newCustomer.setScore(0);
            } else {
                newCustomer.setScore(Integer.parseInt(score_txt.getText()));
                account.setEnable(true);
                account.setEmail(email_txt.getText());
                account.setPassWordAccount("12345678");
                Role role = roleApi.getByName("ROLE_CUSTOMER");
                Set<Role> roleSet = new HashSet<>();
                roleSet.add(role);
                account.setRoles(roleSet);

                newCustomer.setAccount(account);
            }
            Address address = new Address();
            address.setCity(city_txt.getText());
            address.setDistrict(district_txt.getText());
            address.setWards(wards_txt.getText());
            address.setStreet(street_txt.getText());

            newCustomer.setAddress(address);

            //Lấy rồi chuyển về date cho ngày vào làm
            LocalDate dateValue = date_of_birth_txt.getValue();
            Instant instant = Instant.from(dateValue.atStartOfDay(ZoneId.systemDefault()));
            Date dateImport = Date.from(instant);

            System.out.println(dateImport);

            newCustomer.setDateOfBirth(dateImport);
            newCustomer.setFirstName(first_name_txt.getText());
            newCustomer.setLastName(last_name_txt.getText());

//            System.out.println(newCustomer);

            boolean isAdd = customerApi.addCustomer(newCustomer);
            if (isAdd) {
                Stage stage = (Stage) add_btn.getScene().getWindow();
                stage.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi!!");
                alert.setHeaderText("Lỗi!!");
                alert.show();
            }
        }

//        //Lấy rồi chuyển về date cho ngày vào làm
//        LocalDate dateValue = date_of_birth_txt.getValue();
//        Instant instant = Instant.from(dateValue.atStartOfDay(ZoneId.systemDefault()));
//        Date dateImport = Date.from(instant);
//        System.out.println(dateImport);


    }
}


