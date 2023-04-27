package com.techsavvy.admin.Controller;

import com.techsavvy.admin.Api.EmployeeApi;
import com.techsavvy.admin.Models.LocalStorage;
import com.techsavvy.admin.entity.Employee;
import com.techsavvy.admin.entity.Product;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;

import javafx.scene.chart.LineChart;

import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DashboardController implements Initializable {
    public LineChart<String, Integer> lineChart_Product;

    private final EmployeeApi employeeApi = new EmployeeApi();
    private final LocalStorage localStorage = new LocalStorage();
    public Label date_label;
    public Label name_label;
    public TableView<Product> tableView;
    public TableColumn<Product, Integer> columnSTT;
    public TableColumn<Product, String> columnMaSP;
    public TableColumn<Product, String> columnCount;
    public TableColumn<Product, String> columnNamSP;

    public DashboardController() throws IOException, ClassNotFoundException {
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createLineChart();
        getDate();
        setColumn();
        try {
            getNameEmployee();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void createLineChart()  {
        lineChart_Product.getXAxis().setLabel("Thời gian");
        lineChart_Product.getYAxis().setLabel("Số lượng");

        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("Tháng 1", 100));
        series.getData().add(new XYChart.Data<>("Tháng 2", 150));
        series.getData().add(new XYChart.Data<>("Tháng 3", 200));
        series.getData().add(new XYChart.Data<>("Tháng 4", 180));
        series.getData().add(new XYChart.Data<>("Tháng 5", 250));
        series.getData().add(new XYChart.Data<>("Tháng 6", 250));
        series.getData().add(new XYChart.Data<>("Tháng 7", 300));
        series.getData().add(new XYChart.Data<>("Tháng 8", 350));
        series.getData().add(new XYChart.Data<>("Tháng 9", 150));
        series.getData().add(new XYChart.Data<>("Tháng 10", 250));
        series.getData().add(new XYChart.Data<>("Tháng 11", 450));
        series.getData().add(new XYChart.Data<>("Tháng 12", 350));


        lineChart_Product.getData().add(series);
    }

    private void getDate() {
        Date date = new Date();
        SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        String formattedDate = "";

        try {
            Date date2 = inputFormat.parse(String.valueOf(date));
            SimpleDateFormat outputFormat = new SimpleDateFormat("EEE dd/MM/yyyy");
            formattedDate = outputFormat.format(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        date_label.setText(formattedDate);
    }

    private void getNameEmployee() throws IOException, ClassNotFoundException {
        Employee employee = employeeApi.getById(localStorage.getEmployeeInLocal());
        name_label.setText(employee.getLastName());
    }

    private void setColumn(){
        columnSTT.setCellValueFactory(column -> new ReadOnlyObjectWrapper<>(tableView.getItems().indexOf(column.getValue()) + 1));
        columnMaSP.setCellValueFactory(column1-> new SimpleStringProperty(String.valueOf(column1.getValue().getId())));
        columnNamSP.setCellValueFactory(column2 -> new SimpleStringProperty(column2.getValue().getName()));
        columnCount.setCellValueFactory(column3 -> new SimpleStringProperty(String.valueOf(column3.getValue().getCounts())));

        ObservableList<Product> products = FXCollections.observableArrayList(setList());
        tableView.setItems(products);

    }

    private List<Product> setList(){
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setId("SP001");
        product.setName("iphone 6");
        product.setCounts(49);
        products.add(product);
        Product product1 = new Product();
        product1.setId("SP002");
        product1.setName("iphone 7");
        product1.setCounts(40);
        products.add(product1);
        Product product2 = new Product();
        product2.setId("SP003");
        product2.setName("iphone x");
        product2.setCounts(35);
        products.add(product2);
        Product product3 = new Product();
        product3.setId("SP004");
        product3.setName("iphone 11");
        product3.setCounts(20);
        products.add(product3);
        Product product4 = new Product();
        product4.setId("SP005");
        product4.setName("iphone 11 pro max");
        product4.setCounts(35);
        products.add(product4);
        return products;
    }
}
