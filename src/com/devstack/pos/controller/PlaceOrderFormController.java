package com.devstack.pos.controller;

import com.devstack.pos.bo.BoFactory;
import com.devstack.pos.bo.custom.CustomerBo;
import com.devstack.pos.bo.custom.ProductDetailBo;
import com.devstack.pos.dto.CustomerDto;
import com.devstack.pos.dto.ProductDetailJoinDto;
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
    public TextField txtBarcode;
    public TextField txtDescription;
    public TextField txtSellingPrice;
    public TextField txtDiscount;
    public TextField txtShowPrice;
    public TextField txtBuyingPrice;
    public Label lblDiscount;
    public TextField txtQtyOnHand;
    public TextField txtQty;

    CustomerBo bo = BoFactory.getInstance().getBo(BoType.CUSTOMER);
    private ProductDetailBo productDetailBo = BoFactory.getInstance().getBo(BoType.PRODUCT_DETAIL);

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

    public void loadProduct(ActionEvent actionEvent) {
        try{
            ProductDetailJoinDto p = productDetailBo.findProductJoinDetail(
                    txtBarcode.getText()
            );
            if(p!=null){
                txtDescription.setText(p.getDescription());
                txtDiscount.setText(String.valueOf(0));
                txtSellingPrice.setText(String.valueOf(p.getDto().getSellingPrice()));
                txtShowPrice.setText(String.valueOf(p.getDto().getShowPrice()));
                txtQtyOnHand.setText(String.valueOf(p.getDto().getQtyOnHand()));
                txtBuyingPrice.setText(String.valueOf(p.getDto().getBuyingPrice()));
                txtQty.requestFocus();
            }else{
                new Alert(Alert.AlertType.WARNING,"Can't Find the Product!").show();
            }
        }catch(SQLException | ClassNotFoundException e){
            new Alert(Alert.AlertType.WARNING,"Can't Find the Product!").show();
            throw new RuntimeException(e);
        }
    }
}

