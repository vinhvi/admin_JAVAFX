package com.techsavvy.admin.Controller;

import com.techsavvy.admin.Api.OptionsApi;
import com.techsavvy.admin.Api.ProductApi;
import com.techsavvy.admin.entity.Options;
import com.techsavvy.admin.entity.Product;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ListProductController implements Initializable {
    private final ProductApi productApi = new ProductApi();
    private final OptionsApi optionsApi = new OptionsApi();
    public TableView<Product> table_list_product;
    public TableColumn<Product, Integer> column_stt;
    public TableColumn<Product, String> column_ma;
    public TableColumn<Product, String> column_nameProduct;
    public TableColumn<Product, String> column_count;
    public TableColumn<Product, String> column_type;
    public TableColumn<Product, String> column_LH;

    public TableColumn<Product, Void> column_options;
    public TableColumn<Product, String> column_countOptions;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setDataForTable();
        try {
            getDataForTable();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    private void getDataForTable() throws IOException, ClassNotFoundException {
        List<Product> productList = productApi.getListProduct();
        ObservableList<Product> observableList = FXCollections.observableArrayList(productList);
        table_list_product.setItems(observableList);
    }

    private void setDataForTable() {
        column_stt.setCellValueFactory(stt -> new ReadOnlyObjectWrapper<>(table_list_product.getItems().indexOf(stt.getValue()) + 1));
        column_ma.setCellValueFactory(id -> {
            String idProduct = id.getValue().getId();
            return new SimpleStringProperty(idProduct);
        });
        column_nameProduct.setCellValueFactory(name -> {
            String nameProduct = name.getValue().getName();
            return new SimpleStringProperty(nameProduct);
        });
        column_count.setCellValueFactory(sol -> {
            String count = String.valueOf(sol.getValue().getCounts());
            return new SimpleStringProperty(count);
        });
        column_LH.setCellValueFactory(lh -> {
            String adc = lh.getValue().getLo();
            return new SimpleStringProperty(adc);
        });
        column_type.setCellValueFactory(type -> {
            String nameType = type.getValue().getType().getName();
            return new SimpleStringProperty(nameType);
        });
        column_countOptions.setCellValueFactory(adf -> {
            try {
                List<Options> options = optionsApi.getOptionsByProduct(adf.getValue().getId());
                String count = String.valueOf(options.size());
                return new SimpleStringProperty(count);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        });

        Callback<TableColumn<Product, Void>, TableCell<Product, Void>> cellCallback = new Callback<>() {
            @Override
            public TableCell<Product, Void> call(TableColumn<Product, Void> productVoidTableColumn) {
                return new TableCell<>() {
                    private final Button inforImportOrder_btn = new Button("Xem thông tin");

                    {
                        inforImportOrder_btn.setOnAction(actionEvent -> {
                            Product productData = getTableView().getItems().get(getIndex());
                            System.out.println(productData);
                            showInforProduct((Stage) ((Node) actionEvent.getSource()).getScene().getWindow(), productData);
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
    }


    private void showInforProduct(Stage stage, Product product) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/UpdateProduct.fxml"));
        try {
            Parent root = loader.load();
            UpdateProductController controller = loader.getController();
            controller.setDataInforProduct(product);
            Stage dialog = new Stage();
            dialog.initOwner(stage);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setScene(new Scene(root));
            dialog.showAndWait();
            List<Product> productList = productApi.getListProduct();
            ObservableList<Product> observableList = FXCollections.observableArrayList(productList);
            table_list_product.setItems(observableList);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


}
