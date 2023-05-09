package com.techsavvy.admin.Controller;

import com.techsavvy.admin.Api.EvaluateApi;
import com.techsavvy.admin.Api.ImageApi;
import com.techsavvy.admin.Api.ProductApi;
import com.techsavvy.admin.Api.TypeApi;
import com.techsavvy.admin.Models.Model;
import com.techsavvy.admin.entity.*;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

public class UpdateProductController implements Initializable {
    private final ProductApi productApi = new ProductApi();
    private final ImageApi imageApi = new ImageApi();
    private final TypeApi typeApi = new TypeApi();
    private final EvaluateApi evaluateApi =new EvaluateApi();
    public TableColumn<com.techsavvy.admin.entity.Image, Integer> columStt_Image;
    public TableColumn<com.techsavvy.admin.entity.Image, String> columnLink_image;
    public TableColumn<com.techsavvy.admin.entity.Image, Void> columnOption_image;
    public Button listOptions_btn;
    public ProgressIndicator loading_upload_image;
    public Button btn_infor_moTa;
    public Button question_btn;
    public Button evaluate_btn;
    private File file;
    private List<Specifications> specificationsList = new ArrayList<>();
    private List<com.techsavvy.admin.entity.Image> imageList = new ArrayList<>();
    private Specifications specifications;
    public ImageView image_view = new ImageView();
    public TextField id_Product;
    public TextField nameProduct_txt;
    public TextField origin_txt;
    public TextField lo_txt;

    public ComboBox<String> combobox_type = new ComboBox<>();
    public ComboBox<String> combobox_status = new ComboBox<>();
    public TextField count_txt;
    public TextField detailSpecifi_txt;
    public Button addSpecifi_btn;
    public TableView<Specifications> table_specifi;
    public Button add_image_btn;
    public TextField nameSpecifi_txt;
    public TableView<com.techsavvy.admin.entity.Image> table_list_image;
    public Button confirm_btn;
    public Button huy_btn;
    public TableColumn<Specifications, Integer> columnSTT_specifi;
    public TableColumn<Specifications, String> columnName_specifi;
    public TableColumn<Specifications, String> columnDiscribes_specifi;
    public TableColumn<Specifications, Void> columnOptions_specifi;
    public Button updateSpec_btn;
    private Product product;

    private final AtomicInteger index = new AtomicInteger(0);

    private Specifications specificationsInTable;


    public Product getProduct() {
        return product;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loading_upload_image.setVisible(false);
        loadImage();
        setCombobox();
        onClick();
    }

    private void showInforOptions() throws IOException, ClassNotFoundException {
        Product product1 = new Product();
        product1.setId(product.getId());
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/ListOptionProduct.fxml"));
        Parent root = fxmlLoader.load();
        ListOptionsProductController controller = fxmlLoader.getController();
        controller.setOptionsListForTable(product1);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();
        List<Options> options = controller.getOptionsList();
        System.out.println("list options: " + options);
    }

    private void showInforMoTa() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/InforMoTa.fxml"));
        Parent root = fxmlLoader.load();
        MoTaController controller = fxmlLoader.getController();
        controller.setMoTa(product.getDescribes());
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();
        product.setDescribes(controller.getMoTa());
    }

    private void onClickTableSpecification() {
        table_specifi.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) {
                specifications = table_specifi.getSelectionModel().getSelectedItem();
                nameSpecifi_txt.setText(specifications.getName());
                detailSpecifi_txt.setText(specifications.getDescribes());
                specificationsInTable = specifications;
                int index1 = table_specifi.getSelectionModel().getSelectedIndex();
                index.set(index1); // Cập nhật giá trị biến index
            }
        });
    }

    private void onClickTableImage() {
        table_list_image.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) {
                com.techsavvy.admin.entity.Image image = table_list_image.getSelectionModel().getSelectedItem();
                Image link = new Image(image.getImageUrl());
                image_view.setImage(link);
            }
        });
    }

    private void onClick() {
        onClickTableSpecification();
        onClickTableImage();
        addSpecifi_btn.setOnAction(actionEvent -> onAddSpecifications());
        updateSpec_btn.setOnAction(actionEvent -> updateSpecificationsInTable());
        add_image_btn.setOnAction(actionEvent -> {
            try {
                addImage();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        confirm_btn.setOnAction(actionEvent -> {
            try {
                updateSpecifi();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        listOptions_btn.setOnAction(actionEvent -> {
            try {
                showInforOptions();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        btn_infor_moTa.setOnAction(actionEvent -> {
            try {
                showInforMoTa();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        evaluate_btn.setOnAction(actionEvent -> {
            try {
                getListEvaluate();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void addImage() throws IOException, ClassNotFoundException {
        if (file != null) {
            Task<com.techsavvy.admin.entity.Image> uploadTask = new Task<>() {
                @Override
                protected com.techsavvy.admin.entity.Image call() throws Exception {
                    // Hiển thị ProgressIndicator trong UI thread
                    Platform.runLater(() -> loading_upload_image.setVisible(true));
                    // Thực hiện upload hình ảnh trong background thread
                    com.techsavvy.admin.entity.Image image = imageApi.uploadImage(file, id_Product.getText());
                    add_image_btn.setDisable(true);
                    return image;
                }

                @Override
                protected void succeeded() {
                    super.succeeded();
                    // Ẩn ProgressIndicator trong UI thread khi hoàn thành
                    loading_upload_image.setVisible(false);
                    add_image_btn.setDisable(false);
                    // Thêm hình ảnh vào danh sách và cập nhật bảng
                    com.techsavvy.admin.entity.Image image = getValue();
                    if (image != null) {
                        imageList.add(image);
                        ObservableList<com.techsavvy.admin.entity.Image> imageObservableList = FXCollections.observableArrayList(imageList);
                        table_list_image.setItems(imageObservableList);
                    }
                    file = null;
                    Image image1 = new Image("D:\\KLTN-2023\\TechSavvy.Me\\Admin\\src\\main\\resources\\Images\\iconImage.png");
                    image_view.setImage(image1);
                }

                @Override
                protected void failed() {
                    super.failed();
                    // Ẩn ProgressIndicator trong UI thread nếu có lỗi xảy ra
                    loading_upload_image.setVisible(false);
                    System.out.println("Lỗi khi upload hình ảnh!");
                }
            };
            new Thread(uploadTask).start();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Lỗi !!");
            alert.setHeaderText("Lỗi");
            alert.setContentText("Chọn ảnh cần upload!!");
        }
    }


    public void setDataInforProduct(Product product) throws IOException, ClassNotFoundException {
        this.product = product;
        specificationsList = productApi.getSpecifiByProduct(product.getId());
        System.out.println(specifications);
        if (specificationsList != null) {
            setTableSpecifications(specificationsList);
        }
        imageList = imageApi.getImageByProduct(product.getId());
        setTableImage(imageList);
        id_Product.setText(product.getId());
        nameProduct_txt.setText(product.getName());
        combobox_type.setValue(product.getType().getName());
        origin_txt.setText(product.getOrigin());
        lo_txt.setText(product.getLo());
        if (product.getType().isStatus()) {
            combobox_status.setValue("Kinh doanh");
        } else {
            combobox_status.setValue("Không kinh doanh");
        }
        count_txt.setText(String.valueOf(product.getCounts()));

    }

    private void onAddSpecifications() {
        Specifications specifications = new Specifications();
        specifications.setDescribes(detailSpecifi_txt.getText());
        specifications.setName(nameSpecifi_txt.getText());
        specifications.setProduct(product);
        specificationsList.add(specifications);
        ObservableList<Specifications> observableList = FXCollections.observableArrayList(specificationsList);
        table_specifi.setItems(observableList);
        setDefaultSpecifi();
    }

    private void setDefaultSpecifi() {
        detailSpecifi_txt.setText("");
        nameSpecifi_txt.setText("");
    }

    private void updateSpecificationsInTable() {
        if (nameSpecifi_txt.getText() != null && detailSpecifi_txt != null) {
            Specifications specifications1 = new Specifications();
            specifications1.setId(specificationsInTable.getId());
            specifications1.setProduct(product);
            specifications1.setName(nameSpecifi_txt.getText());
            specifications1.setDescribes(detailSpecifi_txt.getText());
            specificationsList.set(index.get(), specifications1);
            ObservableList<Specifications> observableList = FXCollections.observableArrayList(specificationsList);
            table_specifi.setItems(observableList);
            setDefaultSpecifi();
            System.out.println(specificationsList);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Lỗi!!");
            alert.setTitle("Lỗi!!");
            alert.setContentText("Nhập đầy đủ mô tả và chi tiết mô tả !!");
        }
    }


    private void setTableSpecifications(List<Specifications> specificationsList) {
        columnSTT_specifi.setCellValueFactory(stt -> new ReadOnlyObjectWrapper<>(table_specifi.getItems().indexOf(stt.getValue()) + 1));
        columnName_specifi.setCellValueFactory(name -> {
            String nameSpecifi = name.getValue().getName();
            return new SimpleStringProperty(nameSpecifi);
        });
        columnDiscribes_specifi.setCellValueFactory(describes -> {
            String describesSpecifi = describes.getValue().getDescribes();
            return new SimpleStringProperty(describesSpecifi);
        });

        Callback<TableColumn<Specifications, Void>, TableCell<Specifications, Void>> cellCallback = new Callback<>() {
            @Override
            public TableCell<Specifications, Void> call(TableColumn<Specifications, Void> specificationsVoidTableColumn) {
                return new TableCell<>() {
                    private final Button deleteButton = new Button("Xóa");

                    {
                        deleteButton.setOnAction((ActionEvent event) -> {
                            Specifications data = getTableView().getItems().get(getIndex());
                            specificationsList.remove(data);
                            List<Specifications> specificationsList1 = new ArrayList<>();
                            for (Specifications specifications1 : specificationsList) {
                                specifications1.setId(specificationsList1.size() + 1);
                                specificationsList1.add(specifications1);
                            }
                            System.out.println(specificationsList1);
                            ObservableList<Specifications> newData = FXCollections.observableArrayList(specificationsList1);
                            table_specifi.setItems(newData);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(deleteButton);
                        }
                    }
                };
            }
        };

        columnOptions_specifi.setCellFactory(cellCallback);
        ObservableList<Specifications> data = FXCollections.observableArrayList(specificationsList);
        table_specifi.setItems(data);
    }


    private void setCombobox() {
        ObservableList<String> type = FXCollections.observableArrayList(
                "Phone",
                "Tablet",
                "Accessory"
        );
        combobox_type.setItems(type);
        combobox_type.setValue("Phone");
        ObservableList<String> status = FXCollections.observableArrayList(
                "Kinh doanh",
                "Không kinh doanh"
        );
        combobox_status.setItems(status);
        combobox_status.setValue("Không kinh doanh");
    }

    private void setTableImage(List<com.techsavvy.admin.entity.Image> imageList) {
        columStt_Image.setCellValueFactory(stt -> new ReadOnlyObjectWrapper<>(table_list_image.getItems().indexOf(stt.getValue()) + 1));
        columnLink_image.setCellValueFactory(link -> {
            String url = link.getValue().getImageUrl();
            return new
                    SimpleStringProperty(url);
        });

        Callback<TableColumn<com.techsavvy.admin.entity.Image, Void>, TableCell<com.techsavvy.admin.entity.Image, Void>> cellCallback = new Callback<>() {
            @Override
            public TableCell<com.techsavvy.admin.entity.Image, Void> call(TableColumn<com.techsavvy.admin.entity.Image, Void> fileVoidTableColumn) {
                return new TableCell<>() {
                    private final Button deleteButton = new Button("Xóa");

                    {
                        deleteButton.setOnAction((ActionEvent event) -> {
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Xác nhận xóa");
                            alert.setHeaderText(null);
                            alert.setContentText("Bạn có chắc chắn muốn xóa ?");
                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.isPresent() && result.get() == ButtonType.OK) {
                                com.techsavvy.admin.entity.Image data = getTableView().getItems().get(getIndex());
                                try {
                                    Boolean isRemove = imageApi.deleteImage(data.getId());
                                    if (isRemove) {
                                        imageList.remove(data);
                                        List<com.techsavvy.admin.entity.Image> images = new ArrayList<>(imageList);
                                        ObservableList<com.techsavvy.admin.entity.Image> newData = FXCollections.observableArrayList(images);
                                        table_list_image.setItems(newData);
                                    }

                                } catch (IOException | InterruptedException | ClassNotFoundException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(deleteButton);
                        }
                    }
                };
            }
        };
        columnOption_image.setCellFactory(cellCallback);
        ObservableList<com.techsavvy.admin.entity.Image> data = FXCollections.observableArrayList(imageList);
        table_list_image.setItems(data);

    }


    private void loadImage() {
        image_view.setOnMouseClicked(mouseEvent -> {
            // Hiển thị hộp thoại chọn tệp
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Chọn ảnh");
            // Lấy tệp được chọn
            File selectedFile = fileChooser.showOpenDialog(null);
            // Kiểm tra xem người dùng đã chọn tệp hay chưa
            if (selectedFile != null) {
                Image image = new Image(selectedFile.toURI().toString());
                image_view.setImage(image);
                file = selectedFile;
            }
        });
    }

    private void updateSpecifi() throws IOException, ClassNotFoundException {
        if (updateProductForDatabase()) {
            for (Specifications specifications1 : specificationsList) {
                if (productApi.createSpecifi(specifications1)) {
                    Stage stage = (Stage) huy_btn.getScene().getWindow();
                    Model.getInstance().getViewFactory().closeStage(stage);
                } else {
                    System.out.println("Specifi update for database: " + specifications1);
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Thông báo");
                    alert.setHeaderText("Xảy ra lỗi khi update thông số !!");
                    alert.showAndWait();
                }
            }
        }
    }

    private boolean updateProductForDatabase() throws IOException, ClassNotFoundException {
        boolean isUpdate;
        product.setLo(lo_txt.getText());
        product.setOrigin(origin_txt.getText());
        product.setName(nameProduct_txt.getText());
        Type type = typeApi.getByName(combobox_type.getValue());
        product.setType(type);
        product.setCounts(Integer.parseInt(count_txt.getText()));
        boolean status;
        status = combobox_status.getValue().equals("Kinh doanh");
        product.setStatus(status);
        product.setDescribes(this.product.getDescribes());
        if (productApi.add(product)) {
            isUpdate = true;
        } else {
            isUpdate = false;
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Thông báo");
            alert.setHeaderText("xảy ra lỗi khi update product!!");
            alert.show();
            System.out.println(product);
        }
        return isUpdate;

    }

    private void getListEvaluate() throws IOException, ClassNotFoundException {
        List<Evaluate> evaluateList = evaluateApi.getListEvaluateByProduct(product.getId());
        if (evaluateList != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/ListEvaluate.fxml"));
            Parent root = fxmlLoader.load();
            ListEvaluateController controller = fxmlLoader.getController();
            controller.setTable_evaluate(evaluateList);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Thông báo");
            alert.setHeaderText("Sản Phẩm chưa có đánh giá nào !");
            alert.show();
        }

    }
}

