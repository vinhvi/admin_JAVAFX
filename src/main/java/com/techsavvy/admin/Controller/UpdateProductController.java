package com.techsavvy.admin.Controller;

import com.techsavvy.admin.Api.ProductApi;
import com.techsavvy.admin.Api.TypeApi;
import com.techsavvy.admin.entity.Product;
import com.techsavvy.admin.entity.Specifications;
import com.techsavvy.admin.entity.Type;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class UpdateProductController implements Initializable {
    private final ProductApi productApi = new ProductApi();
    private final TypeApi typeApi = new TypeApi();
    public TableColumn<File, Integer> columStt_Image;
    public TableColumn<File, String> columnLink_image;
    public TableColumn<File, Void> columnOption_image;
    private File file;
    private final List<File> fileList = new ArrayList<>();
    private final List<Specifications> specificationsList = new ArrayList<>();
    private Specifications specifications;
    public HTMLEditor moTa_txt;
    public ImageView image_view = new ImageView();
    public TextField id_Product;
    public TextField nameProduct_txt;
    public TextField price_txt;
    public TextField origin_txt;
    public TextField lo_txt;
    public TextField priceSale_txt;
    public ComboBox<String> combobox_type = new ComboBox<>();
    public ComboBox<String> combobox_status = new ComboBox<>();
    public TextField count_txt;
    public TextField detailSpecifi_txt;
    public Button addSpecifi_btn;
    public TableView<Specifications> table_specifi;
    public Button add_image_btn;
    public TextField nameSpecifi_txt;
    public TableView<File> table_list_image;
    public Button confim_btn;
    public Button huy_btn;
    public TableColumn<Specifications, Integer> columnSTT_specifi;
    public TableColumn<Specifications, String> columnName_specifi;
    public TableColumn<Specifications, String> columnDiscribes_specifi;
    public TableColumn<Specifications, Void> columnOptions_specifi;
    public Button updateSpec_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadImage();
        setCombobox();
        try {
            getIdProduct();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        onClick();
    }

    private void getIdProduct() throws IOException {
        String idProduct = productApi.getRandomId();
        id_Product.setText(idProduct);
    }

    private void onClick() {
        table_specifi.setOnMouseClicked(mouseEvent -> {
            specifications = table_specifi.getSelectionModel().getSelectedItem();
            if (specifications != null) {
                nameSpecifi_txt.setText(specifications.getName());
                detailSpecifi_txt.setText(specifications.getDescribes());
            }

        });
        addSpecifi_btn.setOnAction(actionEvent -> onAddSpecifications());
        updateSpec_btn.setOnAction(actionEvent -> updateSpecificationsInTable());
        add_image_btn.setOnAction(actionEvent -> setTableImage());
        confim_btn.setOnAction(actionEvent -> {
            try {
                setProduct();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void onAddSpecifications() {
        Specifications specifications = new Specifications();
        specifications.setId(specificationsList.size() + 1);
        specifications.setDescribes(detailSpecifi_txt.getText());
        specifications.setName(nameSpecifi_txt.getText());
        setTableSpecifications(specifications);
        setDefaultSpecifi();
    }

    private void setDefaultSpecifi() {
        detailSpecifi_txt.setText("");
        nameSpecifi_txt.setText("");
    }

    private void updateSpecificationsInTable() {
        if (specifications != null) {
            List<Specifications> newList = new ArrayList<>();
            for (Specifications specifications1 : specificationsList) {
                if (specifications.getId() == specifications1.getId()) {
                    specifications1.setName(nameSpecifi_txt.getText());
                    specifications1.setDescribes(detailSpecifi_txt.getText());
                }
                newList.add(specifications1);
            }
            ObservableList<Specifications> newData = FXCollections.observableArrayList(newList);
            table_specifi.setItems(newData);
            table_specifi.refresh();
            setDefaultSpecifi();
            specifications = null;

        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText("");
            alert.setContentText("Chọn thông số cần sửa trong bảng !!");
        }

    }


    private void setTableSpecifications(Specifications specifications) {
        specificationsList.add(specifications);
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

    private void setTableImage() {
        if (file != null) {
            fileList.add(file);
            columStt_Image.setCellValueFactory(stt -> new ReadOnlyObjectWrapper<>(table_list_image.getItems().indexOf(stt.getValue()) + 1));
            columnLink_image.setCellValueFactory(link -> {
                String url = link.getValue().getName();
                return new
                        SimpleStringProperty(url);
            });

            Callback<TableColumn<File, Void>, TableCell<File, Void>> cellCallback = new Callback<>() {

                @Override
                public TableCell<File, Void> call(TableColumn<File, Void> fileVoidTableColumn) {
                    return new TableCell<>() {
                        private final Button deleteButton = new Button("Xóa");

                        {
                            deleteButton.setOnAction((ActionEvent event) -> {
                                File data = getTableView().getItems().get(getIndex());
                                fileList.remove(data);
                                List<File> files = new ArrayList<>(fileList);
                                System.out.println(files);
                                ObservableList<File> newData = FXCollections.observableArrayList(files);
                                table_list_image.setItems(newData);
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
            ObservableList<File> data = FXCollections.observableArrayList(fileList);
            table_list_image.setItems(data);
            file = null;
            Image image1 = new Image("D:\\KLTN-2023\\TechSavvy.Me\\Admin\\src\\main\\resources\\Images\\iconImage.png");
            image_view.setImage(image1);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText("");
            alert.setContentText("Chọn ảnh cần thêm !!");
        }
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


    private void setProduct() throws IOException {
        Product product = new Product();
        product.setId(id_Product.getText());
        product.setLo(lo_txt.getText());
        product.setOrigin(origin_txt.getText());
        product.setName(nameProduct_txt.getText());
        Type type = typeApi.getByName(combobox_type.getValue());
        product.setType(type);
        product.setCounts(Integer.parseInt(count_txt.getText()));
        product.setPrice(Float.parseFloat(price_txt.getText()));
        boolean status;
        if (combobox_status.getValue().equals("Kinh doanh")) {
            status = true;
        } else {
            status = false;
        }
        product.setStatus(status);
        System.out.println(product);
    }

    private List<File> getListFileImage() {
        return fileList;
    }

}

