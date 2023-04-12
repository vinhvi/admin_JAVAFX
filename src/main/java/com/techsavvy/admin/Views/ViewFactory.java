package com.techsavvy.admin.Views;

import com.techsavvy.admin.Controller.ViewController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ViewFactory {
    private final StringProperty selectMenuItem;
    private AnchorPane dasAnchorView;
    private AnchorPane listCustomer;
    private AnchorPane listImportOrder;
    private AnchorPane createImportOrder;
    private AnchorPane saleOrder;
    private AnchorPane createSaleOrder;
    private VBox listProduct;

    public ViewFactory() {
        this.selectMenuItem = new SimpleStringProperty("");
    }

    public AnchorPane getDasAnchorView() {
        if (dasAnchorView == null) {
            try {
                dasAnchorView = new FXMLLoader(getClass().getResource("/Fxml/Dashboard.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dasAnchorView;
    }

    public void showLoginWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Login.fxml"));
        createStage(loader);
    }

    public void createStage(FXMLLoader loader) {

        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("TECH SAVVY");
        stage.show();
    }


    public void showViews() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Views.fxml"));
        ViewController viewController = new ViewController();
        loader.setController(viewController);
        createStage(loader);
    }

    public AnchorPane getListCustomer() {
        if (listCustomer == null) {
            try {
                listCustomer = new FXMLLoader(getClass().getResource("/Fxml/ListCustomer.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return listCustomer;
    }

    public AnchorPane getListImportOrder() {
        if (listImportOrder == null) {
            try {
                listImportOrder = new FXMLLoader(getClass().getResource("/Fxml/ImportOrder.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return listImportOrder;
    }

    public AnchorPane getAddImportOrder() {
        if (createImportOrder == null) {
            try {
                createImportOrder = new FXMLLoader(getClass().getResource("/Fxml/CreateImportOrder.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return createImportOrder;
    }

    public AnchorPane getSaleOrder() {
        if (saleOrder == null) {
            try {
                saleOrder = new FXMLLoader(getClass().getResource("/Fxml/SaleOrder.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return saleOrder;
    }

    public AnchorPane getCreateSaleOrder() {
        if (createSaleOrder == null) {
            try {
                createSaleOrder = new FXMLLoader(getClass().getResource("/Fxml/CreateSaleOrder.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return createSaleOrder;
    }

    public VBox getListProduct() {
        if (listProduct == null) {
            try {
                listProduct = new FXMLLoader(getClass().getResource("/Fxml/ListProduct.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return listProduct;
    }

    public void closeStage(Stage stage) {
        stage.close();
    }

    public StringProperty getSelectMenuItem() {
        return selectMenuItem;
    }

}
