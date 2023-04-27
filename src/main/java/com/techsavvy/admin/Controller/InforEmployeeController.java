package com.techsavvy.admin.Controller;

import com.techsavvy.admin.Api.AccountApi;
import com.techsavvy.admin.Api.EmployeeApi;
import com.techsavvy.admin.Api.RoleApi;
import com.techsavvy.admin.Models.Model;
import com.techsavvy.admin.entity.Account;
import com.techsavvy.admin.entity.Address;
import com.techsavvy.admin.entity.Employee;
import com.techsavvy.admin.entity.Role;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class InforEmployeeController implements Initializable {
    private final EmployeeApi employeeApi = new EmployeeApi();
    private final AccountApi accountApi = new AccountApi();
    private final RoleApi roleApi = new RoleApi();

    public VBox root;

    public Label close_lbl;
    public TextField maNV_txt;
    public TextField firstName_txt;
    public TextField lastName_txt;
    public DatePicker importDate;
    public DatePicker dateOfBirth;
    public TextField email_txt;
    public TextField phone_txt;
    public ComboBox<String> combobox_sex = new ComboBox<>();
    public TextField city_txt;
    public TextField district_txt;
    public TextField wards_txt;
    public TextField street_txt;
    public Button update_btn;
    public Button huy_btn;
    public Employee getEmployee = new Employee();

    public CheckBox checkbox_1;
    public CheckBox checkbox_2;
    public CheckBox checkbox_3;

    public InforEmployeeController() throws IOException, ClassNotFoundException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setValeComboboxSex();
        addListener();

    }

    private void addListener() {
        huy_btn.setOnAction(actionEvent -> onClose());
        update_btn.setOnAction(actionEvent -> {
            try {
                Employee employee = getEmployee();
                System.out.println(employee);
                if (employeeApi.update(employee)) {
                    onClose();
                } else {
                    System.out.println("Up date employee error!!");
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
                }
        );
    }


    private void onClose() {
        Stage stage = (Stage) close_lbl.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
    }

    public void setValeComboboxSex() {
        ObservableList<String> sex = FXCollections.observableArrayList();
        sex.addAll("NAM", "NỮ");
        combobox_sex.setValue("NỮ");
        combobox_sex.setItems(sex);
    }

    public void setInforEmployee(String maNV) throws IOException, ClassNotFoundException {

        getEmployee = employeeApi.getById(maNV);
        maNV_txt.setText(getEmployee.getId());
        firstName_txt.setText(getEmployee.getFirstName());
        lastName_txt.setText(getEmployee.getLastName());
        phone_txt.setText(getEmployee.getPhone());
        email_txt.setText(getEmployee.getEmail());
        city_txt.setText(getEmployee.getAddress().getCity());
        wards_txt.setText(getEmployee.getAddress().getWards());
        street_txt.setText(getEmployee.getAddress().getStreet());
        district_txt.setText(getEmployee.getAddress().getDistrict());
        Date importDate1 = getEmployee.getImportDate();
        Date datOfBirth1 = getEmployee.getDateOfBirth();
        LocalDate localDate1 = LocalDate.ofInstant(datOfBirth1.toInstant(), ZoneId.systemDefault());
        LocalDate localDate = LocalDate.ofInstant(importDate1.toInstant(), ZoneId.systemDefault());
        importDate.setValue(localDate);
        dateOfBirth.setValue(localDate1);
        Account account = accountApi.getByEmail(email_txt.getText());
        Set<Role> roles = account.getRoles();
        for (Role role : roles) {
            if (role.getName().equals("ROLE_ADMIN")) {
                checkbox_1.setSelected(true);
            }
            if (role.getName().equals("ROLE_EMPLOYEE")) {
                checkbox_2.setSelected(true);
            }
            if (role.getName().equals("ROLE_CUSTOMER")) {
                checkbox_3.setSelected(true);
            }

        }
        int sex = getEmployee.getSex();
        if (sex == 1) {
            combobox_sex.setValue("NAM");
        } else {
            combobox_sex.setValue("Nữ");
        }
    }

    public Employee getEmployee() throws IOException, ClassNotFoundException {
        Employee employee = new Employee();
        employee.setId(maNV_txt.getText());
        Account account = accountApi.getByEmail(email_txt.getText());
        Set<Role> roles = new HashSet<>();
        employee.setFirstName(firstName_txt.getText());
        employee.setLastName(lastName_txt.getText());
        Role role = null;
        if (checkbox_1.isSelected()) {
            role = roleApi.getByName(checkbox_1.getText());
            roles.add(role);
        }
        if (checkbox_2.isSelected()) {
            role = roleApi.getByName(checkbox_2.getText());
            roles.add(role);
        } else {
            role = roleApi.getByName(checkbox_3.getText());
            roles.add(role);
        }
        account.setRoles(roles);
        if (combobox_sex.getValue().equals("NAM")) {
            employee.setSex(1);
        } else {
            employee.setSex(0);
        }
        Address address = new Address();
        address.setCity(city_txt.getText());
        address.setWards(wards_txt.getText());
        address.setDistrict(district_txt.getText());
        address.setStreet(street_txt.getText());
        employee.setAddress(address);

        //Lấy rồi chuyển về date cho ngày vào làm
        LocalDate dateValue = importDate.getValue();
        Instant instant = Instant.from(dateValue.atStartOfDay(ZoneId.systemDefault()));
        Date dateImport = Date.from(instant);

        employee.setImportDate(dateImport);
        //Lấy rồi chuyển về date cho ngày sinh
        LocalDate dateValue1 = dateOfBirth.getValue();
        Instant instant1 = Instant.from(dateValue1.atStartOfDay(ZoneId.systemDefault()));
        Date dateOfBirth1 = Date.from(instant1);

        employee.setDateOfBirth(dateOfBirth1);
        employee.setEmail(email_txt.getText());
        employee.setPhone(phone_txt.getText());


        return employee;
    }


}
