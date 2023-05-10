package com.techsavvy.admin.Controller;

import com.techsavvy.admin.Api.AccountApi;
import com.techsavvy.admin.Api.EmployeeApi;
import entity.Account;
import entity.Employee;
import entity.Role;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.Callback;

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

    public TableColumn<Employee, Integer> column_stt;
    public TableColumn<Employee, String> nameEmployeeColumn;
    public TableColumn<Employee, String> sexColumn;
    public TableColumn<Employee, String> importDateColumn;

    public TableColumn<Employee, Void> column_option;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListener();
        try {
            setTableEmployee();
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
    }

    private void onAddEmployee() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/CreateEmployee.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void getListEmployee(){

    }

    private void setTableEmployee() throws IOException, ClassNotFoundException {
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

        column_stt.setCellValueFactory(stt -> new ReadOnlyObjectWrapper<>(tableView.getItems().indexOf(stt.getValue()) + 1));

        nameEmployeeColumn.setCellValueFactory(nameColumn -> {
            String name = nameColumn.getValue().getFirstName() + " " + nameColumn.getValue().getLastName();
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
        Callback<TableColumn<Employee, Void>, TableCell<Employee, Void>> cellCallback = new Callback<>() {
            @Override
            public TableCell<Employee, Void> call(TableColumn<Employee, Void> employeeVoidTableColumn) {
                return new TableCell<>() {
                    private final Button inforEmployee_btn = new Button("Xem Thông Tin");
                    {
                        inforEmployee_btn.setOnAction(actionEvent -> {
                            Employee data = getTableView().getItems().get(getIndex());
                            try {
                                inforEmployee(data.getId());
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
                            setGraphic(inforEmployee_btn);
                        }
                    }
                };
            }
        };
        column_option.setCellFactory(cellCallback);
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

    private void inforEmployee(String maNV) throws IOException, ClassNotFoundException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/InforEmployee.fxml"));
        Parent root = fxmlLoader.load();
        InforEmployeeController inforEmployeeController = fxmlLoader.getController();
        inforEmployeeController.setInforEmployee(maNV);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }


}
