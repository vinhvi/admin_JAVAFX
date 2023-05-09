package com.techsavvy.admin.Controller;

import com.techsavvy.admin.Api.CustomerApi;
import com.techsavvy.admin.Api.QuestionApi;
import com.techsavvy.admin.entity.Customer;
import com.techsavvy.admin.entity.Question;
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
import java.util.List;
import java.util.ResourceBundle;

public class QuestionController implements Initializable {
    private final CustomerApi customerApi = new CustomerApi();
    private final QuestionApi questionApi = new QuestionApi();
    public TableView<Question> table_question;

    public TableColumn<Question, Integer> column_stt;

    public TableColumn<Question, String> column_id;

    public TableColumn<Question, String> column_name;
    public TableColumn<Question, String> column_loHang;

    public TableColumn<Question, String> column_type;
    public TableColumn<Question, String> column_id_customer;
    public TableColumn<Question, String> column_name_customer;

    public TableColumn<Question, String> column_email;
    public TableColumn<Question, String> column_sdt;
    public TableColumn<Question, String> column_question;
    public TableColumn<Question, Void> column_option;
    private Customer customer;

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            getListQuestion();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void getInforCustomer(String email) throws IOException, ClassNotFoundException {
        Customer customer = customerApi.getCustomerByEmail(email);
        if (customer != null) {
            setCustomer(customer);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Thông báo");
            alert.setHeaderText("Không tìm thấy khách hàng " + email);
            alert.show();
        }
    }

    private void getListQuestion() throws IOException, ClassNotFoundException {
        List<Question> questions = questionApi.getQuestionByReply();
        setTable_question(questions);
    }

    private void setTable_question(List<Question> questionList) {
        column_stt.setCellValueFactory(column -> new ReadOnlyObjectWrapper<>(table_question.getItems().indexOf(column.getValue()) + 1));
        column_id.setCellValueFactory(column -> {
            String product_id = column.getValue().getProduct().getId();
            return new SimpleStringProperty(product_id);
        });
        column_name.setCellValueFactory(column -> {
            String name = column.getValue().getProduct().getName();
            return new SimpleStringProperty(name);
        });
        column_loHang.setCellValueFactory(column -> {
            String lo = column.getValue().getProduct().getLo();
            return new SimpleStringProperty(lo);
        });
        column_type.setCellValueFactory(column -> {
            String type = column.getValue().getProduct().getType().getName();
            return new SimpleStringProperty(type);
        });
        column_name_customer.setCellValueFactory(column -> {
            String name = null;
            String email_customer = column.getValue().getAccount().getEmail();
            try {
                getInforCustomer(email_customer);
                if (customer != null) {
                    name = customer.getFirstName() + " " + customer.getLastName();
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            return new SimpleStringProperty(name);
        });
        column_email.setCellValueFactory(column -> {
            String email = "";
            if (customer != null) {
                email = customer.getEmail();
            }
            return new SimpleStringProperty(email);
        });
        column_id_customer.setCellValueFactory(column -> {
            String id = "";
            if (customer != null) {
                id = customer.getId();
            }
            return new SimpleStringProperty(id);
        });
        column_sdt.setCellValueFactory(column -> {
            String sdt = customer.getPhone();
            return new SimpleStringProperty(sdt);
        });
        column_question.setCellValueFactory(column -> {
            String question = column.getValue().getContent();
            return new SimpleStringProperty(question);
        });
        Callback<TableColumn<Question, Void>, TableCell<Question, Void>> cellCallback = new Callback<>() {
            @Override
            public TableCell<Question, Void> call(TableColumn<Question, Void> questionVoidTableColumn) {
                return new TableCell<>() {
                    private final Button infor_btn = new Button("Trả Lời");

                    {
                        infor_btn.setOnAction(actionEvent -> {
                            Question data = getTableView().getItems().get(getIndex());
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/InforQuestion.fxml"));
                            Parent root = null;
                            try {
                                root = fxmlLoader.load();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            InforQuestionController controller = fxmlLoader.getController();
                            try {
                                controller.setInforQuestion(data);
                            } catch (IOException | ClassNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                            Stage stage = new Stage();
                            stage.setScene(new Scene(root));
                            stage.show();
                            try {
                                getListQuestion();
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
        ObservableList<Question> questionObservableList = FXCollections.observableArrayList(questionList);
        table_question.setItems(questionObservableList);
    }

}
