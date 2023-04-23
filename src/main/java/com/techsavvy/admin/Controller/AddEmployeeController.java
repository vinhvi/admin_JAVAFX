package com.techsavvy.admin.Controller;

import com.techsavvy.admin.Api.EmployeeApi;
import com.techsavvy.admin.Api.RoleApi;
import com.techsavvy.admin.Models.Model;
import com.techsavvy.admin.entity.Address;
import com.techsavvy.admin.entity.Employee;
import com.techsavvy.admin.entity.Role;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class AddEmployeeController implements Initializable {
    private final RoleApi roleApi = new RoleApi();
    private final  EmployeeApi employeeApi = new EmployeeApi();
    public Button huy_btn;
    public Label close_lbl;
    public TextField maNV_txt;
    public TextField firstName_txt;
    public TextField lastName_txt;
    public DatePicker importDate;
    public DatePicker dateOfBirth;
    public ComboBox<String> combobox_role = new ComboBox<>();
    ;
    public ComboBox<String> combobox_sex = new ComboBox<>();
    public TextField street_txt;
    public TextField wards_txt;
    public TextField city_txt;
    public TextField email_txt;
    public TextField district_txt;
    public TextField phone_txt;
    public TextField pass_txt;
    public Button add_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListener();
        try {
            setId();
            getRole();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setValeComboboxSex();
    }

    private void addListener() {
        huy_btn.setOnAction(actionEvent -> onClose());
        add_btn.setOnAction(actionEvent -> {
            try {
                createEmployee();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void onClose() {
        Stage stage = (Stage) close_lbl.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
    }

    public void setId() throws IOException {
        EmployeeApi employeeApi = new EmployeeApi();
        String id = employeeApi.getRandomId();
        maNV_txt.setText(id);
    }

    public void getRole() throws IOException {
        ObservableList<String> roles = FXCollections.observableArrayList();
        List<Role> roleList = roleApi.getAllRoles();
        for (Role role1 : roleList) {
            if (role1.getName().equals("EMPLOYEE")) {
                combobox_role.setValue(role1.getName());
            }
            roles.add(role1.getName());

        }
        combobox_role.setItems(roles);

    }

    public void setValeComboboxSex() {
        ObservableList<String> sex = FXCollections.observableArrayList();
        sex.addAll("NAM", "NỮ");
        combobox_sex.setValue("NỮ");
        combobox_sex.setItems(sex);
    }

    public void createEmployee() throws IOException {
        Employee employee  = getEmployee();
        if (employeeApi.add(employee)){
            onClose();
        }else {
            System.out.println("Tạo nhân viên thất bại!!");
        }
    }

    public Employee getEmployee() throws IOException {
        Employee employee = new Employee();
        employee.setId(maNV_txt.getText());
        employee.setFirstName(firstName_txt.getText());
        employee.setLastName(lastName_txt.getText());
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
