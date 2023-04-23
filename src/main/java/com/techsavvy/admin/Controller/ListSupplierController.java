package com.techsavvy.admin.Controller;

import com.techsavvy.admin.Api.SupplierApi;
import com.techsavvy.admin.entity.Supplier;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import java.util.List;
import java.util.ResourceBundle;

public class ListSupplierController implements Initializable {
    public Button addSupplier_btn;
    private final SupplierApi supplierApi = new SupplierApi();
    private final List<Supplier> suppliers = supplierApi.getListSupplier();
    public TableColumn<Supplier, Integer> columSTT;
    public TableColumn<Supplier, String> colum_Id;
    public TableColumn<Supplier, String> column_Name;
    public TableColumn<Supplier, String> column_Email;
    public TableColumn<Supplier, String> column_phone;
    public TableColumn<Supplier, String> column_address;
    public TableColumn<Supplier, Void> column_options;
    public TableView<Supplier> table_supplier;

    public ListSupplierController() throws IOException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        onListener();
        setColumTable();
    }

    private void onListener() {
        addSupplier_btn.setOnAction(actionEvent -> {
            try {
                onAddSupplier();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void onAddSupplier() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/AddSupplier.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }


    private void setColumTable() {
        columSTT.setCellValueFactory(stt -> new ReadOnlyObjectWrapper<>(table_supplier.getItems().indexOf(stt.getValue()) + 1));
        Callback<TableColumn<Supplier, Void>, TableCell<Supplier, Void>> cellCallback = new Callback<>() {
            @Override
            public TableCell<Supplier, Void> call(TableColumn<Supplier, Void> supplierVoidTableColumn) {
                return new TableCell<>() {
                    private final Button infor_btn = new Button("Xem Thông Tin");

                    {
                        infor_btn.setOnAction((ActionEvent event) -> {
                            Supplier data = getTableView().getItems().get(getIndex());
                            System.out.println(data);
                            try {
                                setInfor(data);
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
                            setGraphic(infor_btn);
                        }
                    }
                };
            }
        };
        column_phone.setCellValueFactory(phone -> {
            String sdt = phone.getValue().getPhone();
            return new SimpleStringProperty(sdt);
        });
        colum_Id.setCellValueFactory(id -> {
            String ma = id.getValue().getId();
            return new SimpleStringProperty(ma);
        });
        column_address.setCellValueFactory(address -> {
            String dc = address.getValue().getAddress().getCity()
                    + address.getValue().getAddress().getDistrict()
                    + address.getValue().getAddress().getWards()
                    + address.getValue().getAddress().getStreet();
            return new SimpleStringProperty(dc);
        });
        column_Email.setCellValueFactory(email -> {
            String mail = email.getValue().getEmail();
            return new SimpleStringProperty(mail);
        });
        column_options.setCellFactory(cellCallback);

        ObservableList<Supplier> data = FXCollections.observableArrayList(suppliers);
        table_supplier.setItems(data);
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
