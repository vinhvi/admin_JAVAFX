package com.techsavvy.admin.Controller;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MoTaController implements Initializable {
    public Button btn_confirm;
    public HTMLEditor html_txt;

    private String moTa;

    public String getMoTa() {
        return moTa;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_confirm.setOnAction(actionEvent -> {
            getMoTaInText();
            Stage stage = (Stage) btn_confirm.getScene().getWindow();
            stage.close();
        });
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
        html_txt.setHtmlText(moTa);
    }

    public void getMoTaInText() {
        this.moTa = html_txt.getHtmlText();
    }


}
