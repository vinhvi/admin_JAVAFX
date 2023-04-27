package com.techsavvy.admin.Controller;

import com.techsavvy.admin.Api.AccountApi;
import com.techsavvy.admin.Api.EmployeeApi;
import com.techsavvy.admin.entity.Account;
import com.techsavvy.admin.entity.Employee;
import com.techsavvy.admin.entity.Role;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


public class ListEmployeeController implements Initializable {
    private final EmployeeApi employeeApi = new EmployeeApi();
    private final AccountApi accountApi = new AccountApi();
    public Button addEmployee_btn;
    public TableView<Employee> tableView;
    public TableColumn<Employee, String> roleColumn;
    public TableColumn<Employee, Integer> sttColumn;
    public TableColumn<Employee, String> nameEmployeeColumn;
    public TableColumn<Employee, String> sexColumn;
    public TableColumn<Employee, String> importDateColumn;

    public String maNVSelected;
    public Button infor_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListener();
        try {
            setRoleColumn();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void addListener() {
        addEmployee_btn.setOnAction(actionEvent -> {
            try {
                onAddEmployee();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        tableView.setOnMouseClicked(event -> {
            Employee selectedEmployee = tableView.getSelectionModel().getSelectedItem();
            if (selectedEmployee!=null){
                maNVSelected = selectedEmployee.getId();
            }
        });

        infor_btn.setOnAction(actionEvent -> {
            try {
                inforEmployee();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void onAddEmployee() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/CreateEmployee.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void setRoleColumn() throws IOException, ClassNotFoundException {
        roleColumn.setCellValueFactory(cellData -> {
            try {
                String chucVu = "";
                Account account = accountApi.getByEmail(cellData.getValue().getEmail());
                Set<Role> roles = account.getRoles();
                String roleNames = roles.stream().map(Role::getName).collect(Collectors.joining(",  "));
                if (roleNames.equals("ROLE_EMPLOYEE")) {
                    chucVu = "Nhân Viên";
                } else {
                    chucVu = "Quản Lý";
                }
                return new SimpleStringProperty(chucVu);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        });

        sttColumn.setCellValueFactory(column -> new ReadOnlyObjectWrapper<>(tableView.getItems().indexOf(column.getValue()) + 1));

        nameEmployeeColumn.setCellValueFactory(nameColumn -> {
            String name = nameColumn.getValue().getFirstName() + nameColumn.getValue().getLastName();
            return new SimpleStringProperty(name);
        });

        sexColumn.setCellValueFactory(sex -> {
            int get = sex.getValue().getSex();
            String setSex = "";
            if (get == 1) {
                setSex = "Nam";
            } else {
                setSex = "Nữ";
            }
            return new SimpleStringProperty(setSex);
        });

        importDateColumn.setCellValueFactory(importDate -> {
            Date date = importDate.getValue().getImportDate();
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

        List<Employee> employeeList = new ArrayList<>();
        Account account;
        for (Employee employee : employeeApi.getListEmployee()) {
            account = accountApi.getByEmail(employee.getEmail());
            employee.setAccount(account);
            employeeList.add(employee);
        }
        ObservableList<Employee> observableEmployeeList = FXCollections.observableArrayList(employeeList);
        tableView.setItems(observableEmployeeList);

    }

    private void inforEmployee() throws IOException, ClassNotFoundException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/InforEmployee.fxml"));
        Parent root = fxmlLoader.load();
        InforEmployeeController inforEmployeeController = fxmlLoader.getController();
        inforEmployeeController.setInforEmployee(maNVSelected);
        System.out.println(maNVSelected);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }


}
