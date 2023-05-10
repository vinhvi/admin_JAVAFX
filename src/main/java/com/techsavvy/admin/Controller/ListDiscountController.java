package com.techsavvy.admin.Controller;

import com.techsavvy.admin.Api.DiscountApi;
import com.techsavvy.admin.Api.OptionsApi;
import entity.Discount;
import entity.Options;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class ListDiscountController implements Initializable {
    private final DiscountApi discountApi = new DiscountApi();
    private final OptionsApi optionsApi = new OptionsApi();
    public Button search_btn;
    public TextField search_txt;
    public Button add_discount_btn;
    public TableView<Discount> table_discount;
    public TableColumn<Discount, Integer> column_stt;
    public TableColumn<Discount, String> column_id;
    public TableColumn<Discount, String> column_content;
    public TableColumn<Discount, String> column_value_discount;
    public TableColumn<Discount, String> column_type;
    public TableColumn<Discount, String> column_start_date;
    public TableColumn<Discount, String> column_end_date;
    public TableColumn<Discount, String> column_status;
    public TableColumn<Discount, String> column_count_use;
    public TableColumn<Discount, Void> column_option;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            List<Discount> discountList = discountApi.getListDiscount();
            setTable_discount(discountList);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    private void setTable_discount(List<Discount> discountList) {
        column_stt.setCellValueFactory(column -> new ReadOnlyObjectWrapper<>(table_discount.getItems().indexOf(column.getValue()) + 1));
        column_id.setCellValueFactory(column -> {
            String id = column.getValue().getId();
            return new SimpleStringProperty(id);
        });
        column_content.setCellValueFactory(column -> {
            String content = column.getValue().getContent();
            return new SimpleStringProperty(content);
        });
        column_value_discount.setCellValueFactory(column -> {
            String value = String.valueOf(column.getValue().getDiscountForProduct());
            return new SimpleStringProperty(value);
        });
        column_type.setCellValueFactory(column -> {
            String type = column.getValue().getTypeDiscount();
            return new SimpleStringProperty(type);
        });
        column_start_date.setCellValueFactory(column -> {
            Date date = column.getValue().getStart();
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
        column_end_date.setCellValueFactory(column -> {
            Date date = column.getValue().getEnd();
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
        column_status.setCellValueFactory(column -> {
            String status;
            if (column.getValue().isEnable()) {
                status = "Đang Áp Dụng";
            } else {
                status = "Không Áp Dụng";
            }
            return new SimpleStringProperty(status);
        });
        column_count_use.setCellValueFactory(column -> {
            String count;
            try {
                List<Options> optionsList = optionsApi.getByDiscount(column.getValue().getId());
                if (optionsList.isEmpty()) {
                    count = "0";
                } else {
                    count = String.valueOf(optionsList.size());
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            return new SimpleStringProperty(count);
        });
        Callback<TableColumn<Discount, Void>, TableCell<Discount, Void>> cellCallback = new Callback<>() {

            @Override
            public TableCell<Discount, Void> call(TableColumn<Discount, Void> discountVoidTableColumn) {
                return new TableCell<>() {
                    private final Button infor_btn = new Button("Xem thông tin");
                    {
                        infor_btn.setOnAction(actionEvent -> {
                            Discount data = getTableView().getItems().get(getIndex());
                            try {
                                infor(data);
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
        column_option.setCellFactory(cellCallback);
        ObservableList<Discount> observableList = FXCollections.observableArrayList(discountList);
        table_discount.setItems(observableList);
    }

    private void infor(Discount discount) throws IOException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/InforDiscount.fxml"));
        Parent root = loader.load();
        InforDiscountController controller = loader.getController();
        controller.setInforDiscount(discount);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
