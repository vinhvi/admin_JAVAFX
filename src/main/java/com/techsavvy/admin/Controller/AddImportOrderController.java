package com.techsavvy.admin.Controller;

import com.techsavvy.admin.Api.ImportOrderApi;
import com.techsavvy.admin.Api.ProductApi;
import com.techsavvy.admin.Api.SupplierApi;
import com.techsavvy.admin.entity.Product;
import com.techsavvy.admin.entity.Supplier;
import com.techsavvy.admin.entity.Type;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class AddImportOrderController implements Initializable {
    private final SupplierApi supplierApi = new SupplierApi();
    private final ImportOrderApi importOrderApi = new ImportOrderApi();
    private final ProductApi productApi = new ProductApi();
    public Button infor_btn;
    public Button addProduct_btn;
    public TextField phoneSupplier_txt;
    public TextField nameSupplier_txt;
    public Button check_btn;
    public TextField address_txt;
    public TextField importDate_txt;
    public TextField idImportOrder_txt;
    private final String loHang = productApi.getRandomLH();
    @FXML
    public TableView<Product> table_list_product = new TableView<>();
    @FXML
    public TableColumn<Product, Integer> column_STT = new TableColumn<>();
    @FXML
    public TableColumn<Product, String> column_nameSp = new TableColumn<>();
    @FXML
    public TableColumn<Product, String> column_type = new TableColumn<>();
    @FXML
    public TableColumn<Product, String> column_count = new TableColumn<>();
    @FXML
    public TableColumn<Product, String> column_loHang = new TableColumn<>();
    @FXML
    public TableColumn<Product, String> column_origin = new TableColumn<>();
    @FXML
    public TableColumn<Product, String> column_priceImport = new TableColumn<>();
    @FXML
    public TableColumn<Product, Void> column_options = new TableColumn<>();
    private final List<Product> productList = new ArrayList<>();

    private static final AddImportOrderController instance;

    static {
        try {
            instance = new AddImportOrderController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static AddImportOrderController getInstance() {
        return instance;
    }

    public AddImportOrderController() throws IOException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListener();
        test();
        try {
            setIdOrder();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void addListener() {
        infor_btn.setOnAction(actionEvent -> {
            try {
                onInforSupplier();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        addProduct_btn.setOnAction(actionEvent -> {
            try {
                onAddProduct();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        check_btn.setOnAction(actionEvent -> {
            try {
                onSetSupplier();
                System.out.println(getSupplier());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        infor_btn.setOnAction(actionEvent -> {
            try {
                setInfor(getSupplier());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void addProduct(Product product) {
        productList.add(product);
        updateTable();
    }

    public List<Product> getProductList() {
        return productList;
    }

    private void updateTable() {
        table_list_product.refresh();
        setTableListProduct(getProductList());
        System.out.println("product after add from list: " + productList);
    }

    private void test() {
        Product product = new Product();
        product.setId("SP342");
        product.setName("adasdas");
        product.setStatus(true);
        product.setLo("adasd");
        product.setOrigin("sdasd");
        Type type = new Type();
        type.setName("Phone");
        product.setType(type);
        product.setCounts(324);
        product.setPriceImport(2342);
        product.setPrice(34234);
        List<Product> products = getProductList();
        productList.add(product);
        setTableListProduct(productList);
    }


    private void setIdOrder() throws IOException {
        Date date = new Date();
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = outputFormat.format(date);
        importDate_txt.setText(formattedDate);
        idImportOrder_txt.setText(importOrderApi.getRandomId());
    }


    private void onInforSupplier() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/InforSupplier.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void onAddProduct() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/AddProduct.fxml"));
        Parent root = fxmlLoader.load();
        AddProductController controller = fxmlLoader.getController();
        controller.setLH(loHang);
        controller.setProductList(productList);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();

    }


    private void setTableListProduct(List<Product> products) {
        column_STT.setCellValueFactory(stt -> new ReadOnlyObjectWrapper<>(table_list_product.getItems().indexOf(stt.getValue()) + 1));
        column_nameSp.setCellValueFactory(name -> {
            if (name.getValue().getName() != null) {
                String nameProduct = name.getValue().getName();
                return new SimpleStringProperty(nameProduct);
            } else {
                return null;
            }

        });
        column_type.setCellValueFactory(type -> {
            String nameType = type.getValue().getType().getName();
            return new SimpleStringProperty(nameType);
        });
        column_count.setCellValueFactory(count -> {
            String sl = String.valueOf(count.getValue().getCounts());
            return new SimpleStringProperty(sl);
        });
        column_loHang.setCellValueFactory(lH -> {
            String lo = lH.getValue().getLo();
            return new SimpleStringProperty(lo);
        });
        column_origin.setCellValueFactory(origin -> {
            String hang = origin.getValue().getOrigin();
            return new SimpleStringProperty(hang);
        });
        column_priceImport.setCellValueFactory(priceImport -> {
            String aa = String.valueOf(priceImport.getValue().getPriceImport());
            return new SimpleStringProperty(aa);
        });

        Callback<TableColumn<Product, Void>, TableCell<Product, Void>> cellCallback = new Callback<>() {
            @Override
            public TableCell<Product, Void> call(TableColumn<Product, Void> productVoidTableColumn) {
                return new TableCell<>() {
                    private final Button deleteButton = new Button("Xóa");
                    private final Button infor_btn = new Button("Xem");

                    {
                        deleteButton.setOnAction((ActionEvent event) -> {
                            Product data = getTableView().getItems().get(getIndex());
                        });

                        infor_btn.setOnAction(actionEvent -> {
                            Product data = getTableView().getItems().get(getIndex());
                            System.out.println(data);
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
        ObservableList<Product> productObservableList = FXCollections.observableArrayList(products);
        table_list_product.setItems(productObservableList);
        System.out.println(productObservableList);
    }

    private Supplier getSupplier() throws IOException {
        return supplierApi.getByPhone(phoneSupplier_txt.getText());
    }

    private void onSetSupplier() throws IOException {
        Supplier supplier = getSupplier();
        if (supplier != null) {
            nameSupplier_txt.setText(supplier.getName());
            address_txt.setText(supplier.getAddress().getCity() + ", "
                    + supplier.getAddress().getDistrict() + ", "
                    + supplier.getAddress().getWards() + ", "
                    + supplier.getAddress().getStreet());
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi!!");
            alert.setContentText("error get Supplier!");
        }
    }


    private void setInfor(Supplier supplier) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/InforSupplier.fxml"));
        Parent root = fxmlLoader.load();
        InforSupplierController inforSupplierController = fxmlLoader.getController();
        inforSupplierController.setInforSupplier(supplier);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }


}



