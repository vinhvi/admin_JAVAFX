package com.techsavvy.admin.Views;

import com.techsavvy.admin.Controller.ViewController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ViewFactory {
    private final StringProperty selectMenuItem;
    private VBox dasAnchorView;
    private VBox listCustomer;
    private AnchorPane listImportOrder;
    private VBox createImportOrder;
    private VBox saleOrder;
    private ScrollPane createSaleOrder;
    private VBox listProduct;
    private ScrollPane listEmployee;
    private VBox listSupplier;
    private VBox addSupplier;
    private VBox listDiscount;
    private VBox listQuestions;

    public ViewFactory() {
        this.selectMenuItem = new SimpleStringProperty("");
    }

    public VBox getDasAnchorView() {
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

    public VBox getListCustomer() {
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

    public VBox getAddImportOrder() {
        if (createImportOrder == null) {
            try {
                createImportOrder = new FXMLLoader(getClass().getResource("/Fxml/CreateImportOrder.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return createImportOrder;
    }

    public VBox getSaleOrder() {
        if (saleOrder == null) {
            try {
                saleOrder = new FXMLLoader(getClass().getResource("/Fxml/SaleOrder.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return saleOrder;
    }

    public ScrollPane getCreateSaleOrder() {
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

    public ScrollPane getListEmployee() {
        if (listEmployee == null) {
            try {
                listEmployee = new FXMLLoader(getClass().getResource("/Fxml/ListEmployee.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return listEmployee;
    }

    public VBox getListSupplier() {
        if (listSupplier == null) {
            try {
                listSupplier = new FXMLLoader(getClass().getResource("/Fxml/ListSupplier.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return listSupplier;
    }

    public VBox getAddSupplier(){
        if (addSupplier == null) {
            try {
                addSupplier = new FXMLLoader(getClass().getResource("/Fxml/AddSupplier.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return addSupplier;
    }

    public VBox getListDiscount() {
        if (listDiscount == null) {
            try {
                listDiscount = new FXMLLoader(getClass().getResource("/Fxml/ListDiscount.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return listDiscount;
    }

    public VBox getListQuestions() {
        if (listQuestions == null) {
            try {
                listQuestions = new FXMLLoader(getClass().getResource("/Fxml/ListQuestion.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return listQuestions;
    }

    public void closeStage(Stage stage) {
        stage.close();
    }

    public StringProperty getSelectMenuItem() {
        return selectMenuItem;
    }

}
