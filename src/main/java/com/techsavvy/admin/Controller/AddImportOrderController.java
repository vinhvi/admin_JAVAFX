package com.techsavvy.admin.Controller;

import entity.*;
import com.techsavvy.admin.Api.*;
import com.techsavvy.admin.Models.LocalStorage;
import com.techsavvy.admin.Models.Model;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
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
    private final OptionsApi optionsApi = new OptionsApi();
    private final ProductApi productApi = new ProductApi();
    private final EmployeeApi employeeApi = new EmployeeApi();
    private final LocalStorage localStorage = new LocalStorage();
    public Button infor_btn;
    public Button addProduct_btn;
    public TextField phoneSupplier_txt;
    public TextField nameSupplier_txt;
    public Button check_btn;
    public TextField address_txt;
    public TextField importDate_txt;
    public TextField idImportOrder_txt;
    private final String loHang = productApi.getRandomLH();
    public TableView<Product> table_list_product;

    public TableColumn<Product, Integer> column_STT;

    public TableColumn<Product, String> column_nameSp;

    public TableColumn<Product, String> column_type;

    public TableColumn<Product, String> column_count;

    public TableColumn<Product, String> column_loHang;

    public TableColumn<Product, String> column_origin;

    public TableColumn<Product, String> column_priceImport;

    public TableColumn<Product, Void> column_options;
    private final List<Product> products = new ArrayList<>();
    private final List<Options> options = new ArrayList<>();
    public Button create_btn;
    public TextField tt_txt;
    public Button cancel_btn;


    public AddImportOrderController() throws IOException, ClassNotFoundException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListener();
        setTableListProduct();
        try {
            setIdOrder();
        } catch (IOException | ClassNotFoundException e) {
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
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        infor_btn.setOnAction(actionEvent -> {
            try {
                setInfor(getSupplier());
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        create_btn.setOnAction(actionEvent -> {
            try {
                crateImportOrder();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        cancel_btn.setOnAction(actionEvent -> backToImportOrder());
    }


    private void setIdOrder() throws IOException, ClassNotFoundException {
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
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();
        Product newProduct = controller.getProduct();
        if (newProduct != null) {
            products.add(newProduct);
            options.addAll(controller.getOptionsList());
            System.out.println("List Options: " + options);
            table_list_product.getItems().add(newProduct);
            setTT();
            System.out.println("Product after add: " + products);
        }

    }


    public void setTableListProduct() {
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
            String aaa = String.valueOf(products.size());
            return new SimpleStringProperty(aaa);
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
                            products.remove(data);
                            setTT();
                            table_list_product.getItems().setAll(products);
                        });

                        infor_btn.setOnAction(actionEvent -> {
                            Product product = getTableRow().getItem();
                            showProductInfo((Stage) ((Node) actionEvent.getSource()).getScene().getWindow(), product);
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
    }

    private Supplier getSupplier() throws IOException, ClassNotFoundException {
        return supplierApi.getByPhone(phoneSupplier_txt.getText());
    }

    private void onSetSupplier() throws IOException, ClassNotFoundException {
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

    private List<Options> getListOptionsByProduct(String productId) {
        List<Options> optionsList = new ArrayList<>();
        for (Options options1 : options) {
            if (options1.getProduct().getId().equals(productId)) {
                optionsList.add(options1);
            }
        }
        return optionsList;
    }

    public void showProductInfo(Stage stage, Product product) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/AddProduct.fxml"));
        List<Options> optionsList = getListOptionsByProduct(product.getId());
        try {
            Parent root = loader.load();
            AddProductController controller = loader.getController();
            //set infor product
            controller.setProduct(product);
            //set infor tableOptions
            controller.setTableOptions(optionsList);
            controller.setOptionsList(optionsList);
            //show windows update product
            Stage dialog = new Stage();
            dialog.initOwner(stage);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setScene(new Scene(root));
            dialog.showAndWait();
            // Update the product table if the product is edited
            if (controller.isEdited()) {
                Product editedProduct = controller.getProduct();
                for (Product product1 : products) {
                    if (product1.getId().equals(editedProduct.getId())) {
                        product1.setName(editedProduct.getName());
                        product1.setType(editedProduct.getType());
                        product1.setCounts(editedProduct.getCounts());
                        product1.setOrigin(editedProduct.getOrigin());
                        product1.setStatus(editedProduct.isStatus());
                    }
                }
                options.addAll(controller.getOptionsList());
                setTT();
                table_list_product.getItems().setAll(products);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setTT() {
        float tt = 0;
        for (Options options1 : options) {
            tt += options1.getPriceImport();
        }
        tt_txt.setText(tt + " VND");
    }

    private ImportOrder getImport() throws IOException, ClassNotFoundException {
        ImportOrder importOrder = new ImportOrder();
        importOrder.setId(idImportOrder_txt.getText());
        Date date = new Date();
        importOrder.setImportDate(date);
        String idEmployee = localStorage.getEmployeeInLocal();
        Employee employee = employeeApi.getById(idEmployee);
        importOrder.setEmployee(employee);
        Supplier supplier = supplierApi.getByPhone(phoneSupplier_txt.getText());
        importOrder.setSupplier(supplier);
        return importOrder;
    }

    private void crateImportOrder() throws IOException, ClassNotFoundException {
        ImportOrderDetail importOrderDetail = new ImportOrderDetail();
        float tt = 0;
        boolean a = importOrderApi.add(getImport());
        boolean b;
        boolean c;
        boolean d;

        if (a) {
            System.out.println("save importOrder: " + true);
            for (Product product : products) {
                b = productApi.add(product);
                if (b) {
                    System.out.println("save product: " + true);
                    for (Options options : options) {
                        if (options.getProduct().getId().equals(product.getId())) {
                            c = optionsApi.createOptions(options);
                            System.out.println("save options for product id: " + product.getId() + " " + c);
                        }
                        tt = Float.parseFloat(String.valueOf(options.getCount())) * options.getPriceImport();
                    }
                    importOrderDetail.setProduct(product);
                    importOrderDetail.setCount(product.getCounts());
                    importOrderDetail.setPrice(tt);
                    importOrderDetail.setImportOrder(getImport());
                    d = importOrderApi.createImportDetail(importOrderDetail);
                    System.out.println("save importOrderDetail for productId: " + product.getId() + " " + d);
                }
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thành Công");
            alert.setHeaderText("Tạo phiếu nhập thành công");
            alert.setContentText("");
            alert.show();
            backToImportOrder();
        } else {
            System.out.println("save importOrder: " + false);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText("Lỗi");
            alert.setContentText("");
            alert.show();
        }
    }

    private void backToImportOrder() {
        Model.getInstance().getViewFactory().getSelectMenuItem().set("ListImportOrder");
    }


}



