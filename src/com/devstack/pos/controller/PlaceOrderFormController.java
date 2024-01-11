package com.devstack.pos.controller;

import com.devstack.pos.bo.BoFactory;
import com.devstack.pos.bo.custom.CustomerBo;
import com.devstack.pos.dto.CustomerDto;
import com.devstack.pos.enums.BoType;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class PlaceOrderFormController {

    public AnchorPane context;
    public TextField txtEmail;
    public TextField txtName;
    public TextField txtContact;
    public TextField txtSalary;
    public Hyperlink urlNewLoyalty;
    public Label lblLoyaltyType;
    public ToggleGroup model;

    CustomerBo bo = BoFactory.getInstance().getBo(BoType.CUSTOMER);

    public void btnBackToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("DashboardForm",false);
    }

    public void btnAddNewCustomerOnAction(ActionEvent actionEvent) throws IOException {
        setUi("CustomerForm",true);
    }

    public void btnAddNewProductOnAction(ActionEvent actionEvent) throws IOException {
        setUi("ProductMainForm",true);
    }


    private void setUi(String url,boolean state) throws IOException {
        Stage stage = null;
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../view/"+url+".fxml")));

        if(state){
            stage = new Stage();
            stage.setScene(scene);
            stage.show();
        }else{
            stage =  (Stage)context.getScene().getWindow();
            stage.centerOnScreen();
            stage.setScene(scene);
        }
    }

    public void SearchCustomer(ActionEvent actionEvent){
        try{
            CustomerDto customer = bo.findCustomer(txtEmail.getText());
            if(customer!=null){
                txtName.setText(customer.getName());
                txtSalary.setText(String.valueOf((customer.getSalary())));
                txtContact.setText(customer.getContact());

                fetchLoyaltyCardData(txtEmail.getText());
            }else{
                new Alert(Alert.AlertType.WARNING,"Can't Find the Customer");
            }
        }catch(SQLException | ClassNotFoundException e){
            new Alert(Alert.AlertType.WARNING,"Can't Find the Customer");
            throw  new RuntimeException(e);
        }
    }

    private void fetchLoyaltyCardData(String email) {
        urlNewLoyalty.setText("+ New Loyalty");
        urlNewLoyalty.setVisible(true);
    }

    public void newLoyaltyOnAction(ActionEvent actionEvent) {
    }
}

