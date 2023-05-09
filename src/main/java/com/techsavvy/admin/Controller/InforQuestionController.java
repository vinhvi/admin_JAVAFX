package com.techsavvy.admin.Controller;

import com.techsavvy.admin.Api.AccountApi;
import com.techsavvy.admin.Api.AnswerApi;
import com.techsavvy.admin.Api.CustomerApi;
import com.techsavvy.admin.Api.QuestionApi;
import com.techsavvy.admin.Models.LocalStorage;
import com.techsavvy.admin.entity.Account;
import com.techsavvy.admin.entity.Answer;
import com.techsavvy.admin.entity.Customer;
import com.techsavvy.admin.entity.Question;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

public class InforQuestionController implements Initializable {

    private final CustomerApi customerApi = new CustomerApi();
    private final QuestionApi questionApi = new QuestionApi();
    private final AccountApi accountApi = new AccountApi();
    private final AnswerApi answerApi = new AnswerApi();
    private final LocalStorage localStorage = new LocalStorage();

    public TextField name_txt;

    public TextField email_txt;

    public TextField phone_txt;
    public TextArea content_txt;
    public DatePicker datePicker;
    public Button reply_btn;
    public TextArea reply_txt;
    public TextField id_product_txt;
    public TextField name_product_txt;
    public TextField loHang_txt;
    private Question question;

    public void setQuestion(Question question) {
        this.question = question;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        onListener();
    }

    private void onListener() {
        reply_btn.setOnAction(actionEvent -> {
            try {
                replyQuestion();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void setInforQuestion(Question question) throws IOException, ClassNotFoundException {
        setQuestion(question);
        Customer customer = customerApi.getCustomerByEmail(question.getAccount().getEmail());
        name_txt.setText(customer.getFirstName() + " " + customer.getLastName());
        email_txt.setText(customer.getEmail());
        phone_txt.setText(customer.getPhone());
        content_txt.setText(question.getContent());
        name_product_txt.setText(question.getProduct().getName());
        id_product_txt.setText(question.getProduct().getId());
        loHang_txt.setText(question.getProduct().getLo());
        Date date = question.getQuestionDate();
        LocalDate dateQuestion = LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault());
        datePicker.setValue(dateQuestion);
    }

    private void replyQuestion() throws IOException, ClassNotFoundException {
        Question newQuestion = new Question();
        newQuestion.setId(this.question.getId());
        boolean b = questionApi.updateQuestion(newQuestion.getId());
        if (!b) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText("Lỗi UpDate question");
            alert.show();
            return;
        }
        Answer answer = new Answer();
        answer.setQuestion(newQuestion);
        answer.setContent(reply_txt.getText());
        String email = localStorage.getEmailEmployeeInLocal();
        Account account = accountApi.getByEmail(email);
        answer.setAccount(account);
        boolean a = answerApi.createAnswer(answer);
        if (!a) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText("Lỗi trả lời câu hỏi");
            alert.show();
        } else {
            Stage stage = (Stage) reply_btn.getScene().getWindow();
            stage.close();
        }
    }

}
