package com.techsavvy.admin.Controller;

import com.techsavvy.admin.Api.AccountApi;
import com.techsavvy.admin.Api.EmployeeApi;
import com.techsavvy.admin.Models.LocalStorage;
import com.techsavvy.admin.Models.Model;
import com.techsavvy.admin.entity.Account;
import com.techsavvy.admin.entity.Employee;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    private final AccountApi api = new AccountApi();
    private final LocalStorage localStorage = new LocalStorage();
    private final EmployeeApi employeeApi = new EmployeeApi();
    public Button btn_login;
    public Label error_lbl;
    public TextField name_txt;
    public PasswordField password_txt;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        name_txt.setText("vinh@gmail.com");
        password_txt.setText("12345678");

        btn_login.setOnAction(actionEvent -> {
            try {
                onLogin();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }



    private void onLogin() throws IOException {
        Account account = getAccount();
        String token = api.login(account);
        if (token!=null){
            System.out.println(token);
            localStorage.saveToken(token);
            Employee employee = employeeApi.getByEmail(name_txt.getText());
            localStorage.saveEmployee(employee.getId());
            Stage stage = (Stage) error_lbl.getScene().getWindow();
            Model.getInstance().getViewFactory().closeStage(stage);
            Model.getInstance().getViewFactory().showViews();
        }
       else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setContentText("Tài khoản hoặc mật khẩu không đúng!");
            alert.showAndWait();
        }
    }

    private Account getAccount() {
        Account account = new Account();
        account.setEmail(name_txt.getText());
        account.setPassWordAccount(password_txt.getText());
        return account;
    }


}
