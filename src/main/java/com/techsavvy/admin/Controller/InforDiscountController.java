package com.techsavvy.admin.Controller;

import entity.*;
import com.techsavvy.admin.Api.OptionsApi;
import com.techsavvy.admin.Api.ProductApi;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class InforDiscountController implements Initializable {
    private final OptionsApi optionsApi = new OptionsApi();
    private final ProductApi productApi = new ProductApi();
    public Label lbl_title;
    public TextField id_discount_txt;
    public DatePicker start_date;
    public DatePicker end_date;
    public ComboBox<String> combobox_status;
    public TextField value_discount_txt;
    public ComboBox<String> combobox_type;
    public TextArea content_discount_txt;
    public Button confirm_btn;
    public Button cancel_btn;
    public TableView<Product> table_product;
    public TableColumn<Product, Integer> column_stt_product;
    public TableColumn<Product, String> column_id_product;
    public TableColumn<Product, String> column_name_product;
    public TableColumn<Product, String> column_lh_product;
    public TableColumn<Product, String> column_date_product;
    public TableColumn<Product, Void> column_option_product;
    public TableView<Options> table_options;
    public TableColumn<Options, Integer> column_stt_option;

    public TableColumn<Options, String> column_name_option;
    public TableColumn<Options, String> column_color_option;
    public TableColumn<Options, String> column_price_option;
    public TableColumn<Options, String> column_count_option;
    public TableColumn<Options, Void> column_option;
    public TableColumn<Options, String> column_status_option;
    private Discount discount;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setValueCombobox();
    }

    public void setInforDiscount(Discount discount) throws IOException, ClassNotFoundException {
        this.discount = discount;
        id_discount_txt.setText(discount.getId());
        value_discount_txt.setText(String.valueOf(discount.getDiscountForProduct()));
        Date date1 = discount.getStart();
        LocalDate localDate1 = LocalDate.ofInstant(date1.toInstant(), ZoneId.systemDefault());
        start_date.setValue(localDate1);
        Date date2 = discount.getEnd();
        LocalDate localDate2 = LocalDate.ofInstant(date2.toInstant(), ZoneId.systemDefault());
        end_date.setValue(localDate2);
        content_discount_txt.setText(discount.getContent());
        combobox_type.setValue(discount.getTypeDiscount());
        List<Options> optionsList = optionsApi.getByDiscount(discount.getId());
        List<Product> productList = new ArrayList<>();
        if (optionsList.isEmpty()) {
            System.out.println("optionsList null !!");
        } else {
            for (Options options : optionsList) {
                Product product = productApi.getProductById(options.getProduct().getId());

                // Check if the product already exists in the list
                boolean productExists = false;
                for (Product product1 : productList) {
                    if (product1.getId().equals(product.getId())) {
                        productExists = true;
                        break;
                    }
                }

                // If the product doesn't exist, add it to the list
                if (!productExists) {
                    productList.add(product);
                }
            }
        }

        setTable_product(productList);
    }

    private void setValueCombobox() {
        ObservableList<String> status = FXCollections.observableArrayList();
        status.addAll("Đang kích họoạt", "Không kích hoạt");
        combobox_status.setValue("Không kích hoạt");
        combobox_status.setItems(status);

        ObservableList<String> type = FXCollections.observableArrayList();
        type.addAll("%", "đ");
        combobox_type.setValue("đ");
        combobox_type.setItems(type);
    }

    private void setTable_product(List<Product> productList) {
        column_stt_product.setCellValueFactory(column -> new ReadOnlyObjectWrapper<>(table_product.getItems().indexOf(column.getValue()) + 1));
        column_id_product.setCellValueFactory(column -> {
            String id = column.getValue().getId();
            return new SimpleStringProperty(id);
        });
        column_name_product.setCellValueFactory(column -> {
            String name = column.getValue().getName();
            return new SimpleStringProperty(name);
        });
        column_lh_product.setCellValueFactory(column -> {
            String lh = column.getValue().getLo();
            return new SimpleStringProperty(lh);
        });
        column_date_product.setCellValueFactory(column -> {
            Date date = column.getValue().getDateImport();
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
        Callback<TableColumn<Product, Void>, TableCell<Product, Void>> cellCallback = new Callback<>() {

            @Override
            public TableCell<Product, Void> call(TableColumn<Product, Void> productVoidTableColumn) {
                return new TableCell<>() {
                    private final Button infor_btn = new Button("Xem");

                    {
                        infor_btn.setOnAction(actionEvent -> {
                            Product data = getTableView().getItems().get(getIndex());
                            try {
                                setTable_options(data.getId());
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
                            setGraphic(infor_btn);
                        }
                    }
                };
            }
        };
        column_option_product.setCellFactory(cellCallback);
        ObservableList<Product> productObservableList = FXCollections.observableArrayList(productList);
        table_product.setItems(productObservableList);
    }

    private void setTable_options(String product_id) throws IOException, ClassNotFoundException {
        List<Options> optionsList = optionsApi.getOptionsByProduct(product_id);
        if (optionsList.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Thông Báo");
            alert.setHeaderText("Sản Phẩm chưa có phiên bản");
            alert.show();
        } else {
            column_stt_option.setCellValueFactory(column -> new ReadOnlyObjectWrapper<>(table_options.getItems().indexOf(column.getValue()) + 1));
            column_name_option.setCellValueFactory(column -> {
                String name = column.getValue().getNameOptions();
                return new SimpleStringProperty(name);
            });
            column_color_option.setCellValueFactory(column -> {
                String color = column.getValue().getColor();
                return new SimpleStringProperty(color);
            });
            column_price_option.setCellValueFactory(column -> {
                String price = String.valueOf(column.getValue().getPrice());
                return new SimpleStringProperty(price);
            });
            column_count_option.setCellValueFactory(column -> {
                String count = String.valueOf(column.getValue().getCount());
                return new SimpleStringProperty(count);
            });
            column_status_option.setCellValueFactory(column -> {
                Set<Discount> discountSet = column.getValue().getDiscounts();
                String status = "Chưa Áp Dụng";
                for (Discount discount1 : discountSet) {
                    if (discount1.getId().equals(discount.getId())) {
                        status = "Đang Áp Dụng";
                        break;
                    }
                }
                return new SimpleStringProperty(status);
            });
            Callback<TableColumn<Options, Void>, TableCell<Options, Void>> cellCallback = new Callback<>() {

                @Override
                public TableCell<Options, Void> call(TableColumn<Options, Void> optionsVoidTableColumn) {
                    return new TableCell<>() {
                        private final Button remove_btn = new Button("Xem");
                        private final Button add_btn = new Button("Thêm");

                        {
                            remove_btn.setOnAction(actionEvent -> {
                                Options data = getTableView().getItems().get(getIndex());
                                Set<Discount> discountSet = data.getDiscounts();
                                discountSet.remove(discount);
                                data.setDiscounts(discountSet);
                            });
                            add_btn.setOnAction(actionEvent -> {
                                Options data = getTableView().getItems().get(getIndex());
                                Set<Discount> discountSet = data.getDiscounts();
                                discountSet.add(discount);
                                data.setDiscounts(discountSet);
                            });
                        }

                        @Override
                        public void updateItem(Void item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                            } else {
                                HBox container = new HBox(remove_btn, add_btn);
                                container.setSpacing(5);
                                setGraphic(container);
                            }
                        }
                    };
                }
            };
            column_option.setCellFactory(cellCallback);
            ObservableList<Options> observableList = FXCollections.observableArrayList(optionsList);
            table_options.setItems(observableList);
        }
    }

}
