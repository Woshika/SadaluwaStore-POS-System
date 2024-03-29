package com.devstack.pos.controller;

import com.devstack.pos.bo.BoFactory;
import com.devstack.pos.bo.custom.CustomerBo;
import com.devstack.pos.bo.custom.OrderDetailBo;
import com.devstack.pos.bo.custom.ProductDetailBo;
import com.devstack.pos.dto.CustomerDto;
import com.devstack.pos.dto.ItemDetailDto;
import com.devstack.pos.dto.OrderDetailDto;
import com.devstack.pos.dto.ProductDetailJoinDto;
import com.devstack.pos.enums.BoType;
import com.devstack.pos.util.UserSessionData;
import com.devstack.pos.view.tm.CartTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;


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
    public TableView<CartTm> tblCart;
    public TableColumn colCode;
    public TableColumn ColDesc;
    public TableColumn colSellPrice;
    public TableColumn colSelDisc;
    public TableColumn colSelShPrice;
    public TableColumn colSelQty;
    public TableColumn ColSelTotal;
    public TableColumn colSelOperation;
    public Label txtTotal;

    CustomerBo bo = BoFactory.getInstance().getBo(BoType.CUSTOMER);
    private ProductDetailBo productDetailBo = BoFactory.getInstance().getBo(BoType.PRODUCT_DETAIL);
    private OrderDetailBo orderDetailBo = BoFactory.getInstance().getBo(BoType.ORDER_DETAIL);

    public void initialize() throws SQLException, ClassNotFoundException {
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        ColDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        colSellPrice.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));
        colSelDisc.setCellValueFactory(new PropertyValueFactory<>("discount"));
        colSelShPrice.setCellValueFactory(new PropertyValueFactory<>("showPrice"));
        colSelQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        ColSelTotal.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
        colSelOperation.setCellValueFactory(new PropertyValueFactory<>("btn"));

    }

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

    ObservableList<CartTm> tms = FXCollections.observableArrayList();
    public void addToCart(ActionEvent actionEvent) {
        int qty = Integer.parseInt(txtQty.getText());
        double sellingPrice = Double.parseDouble(txtSellingPrice.getText());
        double totalCost = qty * sellingPrice;
        CartTm selectedCartTm = isExists(txtBarcode.getText());
        if(selectedCartTm!=null){
            selectedCartTm.setQty(qty+selectedCartTm.getQty());
            selectedCartTm.setTotalCost(totalCost+selectedCartTm.getTotalCost());
            tblCart.refresh();
        }else{
            Button btn = new Button("Remove");

            CartTm tm = new CartTm(txtBarcode.getText(),
                    txtDescription.getText(),
                    Double.parseDouble(txtDiscount.getText()),
                    sellingPrice,
                    Double.parseDouble(txtShowPrice.getText()),
                    qty,
                    totalCost,
                    btn);
            btn.setOnAction((e) ->{
                tms.remove(tm);
                tblCart.refresh();
                setTotal();
            });
            tms.add(tm);
            clear();
            tblCart.setItems(tms);
            setTotal();
        }

    }
    private void clear(){
        txtDescription.clear();
        txtSellingPrice.clear();
        txtDiscount.clear();
        txtShowPrice.clear();
        txtQtyOnHand.clear();
        txtBuyingPrice.clear();
        txtQty.clear();
        txtBarcode.clear();
        txtBarcode.requestFocus();
    }

    private CartTm isExists(String code){
        for(CartTm tm : tms){
            if(tm.getCode().equals(code)){
                return tm;
            }
        }
        return null;
    }

    private void setTotal(){
        double total = 0;
        for(CartTm tm : tms){
            total  += tm.getTotalCost();

        }
        txtTotal.setText(total+ " /=");
    }

    public void btnCompleteOrder(ActionEvent actionEvent) {
        ArrayList<ItemDetailDto> dtoList = new ArrayList<>();
        double discount =0;
        for (CartTm tm:tms
             ) {
            dtoList.add(new ItemDetailDto(tm.getCode(),tm.getQty(),tm.getDiscount(),tm.getTotalCost()));
            discount += tm.getDiscount();
        }
        OrderDetailDto dto = new OrderDetailDto(
                new Random().nextInt(100001),
                new Date(),Double.parseDouble(txtTotal.getText().split(" /=")[0]),
                txtEmail.getText(),discount, UserSessionData.email,dtoList
        );

        try{
           if(orderDetailBo.makeOrder(dto)){
               new Alert(Alert.AlertType.CONFIRMATION,"Customer Saved!").show();
               clearFields();
           }else{
               new Alert(Alert.AlertType.WARNING,"Try Again!").show();
           }
        }catch(SQLException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.WARNING,"Try Again!").show();
        }
    }

    private void clearFields() {
    }
}

