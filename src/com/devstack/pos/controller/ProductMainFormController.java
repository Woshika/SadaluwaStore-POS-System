package com.devstack.pos.controller;

import com.devstack.pos.bo.BoFactory;
import com.devstack.pos.bo.custom.ProductBo;
import com.devstack.pos.bo.custom.impl.ProductBoImpl;
import com.devstack.pos.dto.ProductDto;
import com.devstack.pos.enums.BoType;
import com.devstack.pos.view.tm.ProductTm;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class ProductMainFormController {
    public JFXTextField txtProductCode;
    public TextArea txtProductDescription;
    public JFXButton btnSaveUpdate;
    public TableView<ProductTm> tbl;
    public TableColumn colProductId;
    public TableColumn colProductDesc;
    public TableColumn colProductShowMore;
    public TableColumn colProductDelete;
    public TextField txtSelectedProdId;
    public TextArea txtSelectedProdDescription;
    public TextField txtSearch;
    public JFXButton btnNewBatch;

    private String searchText = "";

    ProductBo bo = BoFactory.getInstance().getBo(BoType.PRODUCT);

    public void initialize() throws SQLException, ClassNotFoundException {

        colProductId.setCellValueFactory(new PropertyValueFactory<>("code"));
        colProductDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        colProductShowMore.setCellValueFactory(new PropertyValueFactory<>("showMore"));
        colProductDelete.setCellValueFactory(new PropertyValueFactory<>("delete"));


        //----load new product Id----
        loadProductId();
        loadAllProducts(searchText);


        tbl.getSelectionModel().selectedItemProperty().addListener((observable, OldValue, newValue) ->{

            setData(newValue);
        });

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            searchText = newValue;
            try{
                loadAllProducts(searchText);
            }catch(SQLException | ClassNotFoundException e){
                throw new RuntimeException(e);
            }
        });
    }

    private void setData(ProductTm newValue) {
        txtSelectedProdId.setText(String.valueOf(newValue.getCode()));
        txtSelectedProdDescription.setText(newValue.getDescription());
        btnNewBatch.setDisable(false);  //new Batch disable
    }

    private void loadProductId() throws SQLException, ClassNotFoundException {
        txtProductCode.setText(String.valueOf(new ProductBoImpl().getLastProductId()));
    }

    public void btnBackToHomeOnAction(ActionEvent actionEvent) throws IOException {

    }
    //Save Product
    public void btnNewProductOnAction(ActionEvent actionEvent) {
        try {
            if (btnSaveUpdate.getText().equals("Save Product")) {
                if (bo.saveProduct(new ProductDto(Integer.parseInt(txtProductCode.getText()), txtProductDescription.getText()))) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Product Saved!").show();
                    clearFields();
                    loadAllProducts(searchText);
                } else {
                    new Alert(Alert.AlertType.WARNING, "Try Again!").show();
                }
            } else {
                if (bo.saveProduct(new ProductDto(Integer.parseInt(txtProductCode.getText()), txtProductDescription.getText()))) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Product Updated!").show();
                    clearFields();
                    loadAllProducts(searchText);
                    //----------
                    btnSaveUpdate.setText("Save Product");
                } else {
                    new Alert(Alert.AlertType.WARNING, "Try Again!").show();
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.WARNING, e.getMessage()).show();
        }
    }

    private void clearFields() throws SQLException, ClassNotFoundException {
        txtProductCode.clear();
        txtProductDescription.clear();
        loadProductId();
    }

    private void loadAllProducts(String searchText) throws SQLException, ClassNotFoundException {
        ObservableList<ProductTm> tms = FXCollections.observableArrayList();
        int counter=1;
        for(ProductDto dto : bo.findAllProducts()){

            Button showMore = new Button("Show More");
            Button delete = new Button("Delete");
            ProductTm tm = new ProductTm(dto.getCode(), dto.getDescription(), showMore,delete);
            tms.add(tm);

            delete.setOnAction((e) ->{
                try{
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are You Sure?",ButtonType.YES,ButtonType.NO);
                    Optional<ButtonType> selectedButtonType = alert.showAndWait();
                    if(selectedButtonType.get().equals(ButtonType.YES)){
                        if (bo.deleteProduct(dto.getCode())){
                            new Alert(Alert.AlertType.CONFIRMATION,"Customer Deleted!").show();
                            clearFields();
                            loadAllProducts(searchText);
                        }else{
                            new Alert(Alert.AlertType.WARNING,"Try Again!").show();
                        }
                    }
                }catch(SQLException | ClassNotFoundException exception){
                    exception.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, exception.getMessage()).show();
                }
            });
        }

        tbl.setItems(tms);
    }

    //Add new Product
    public void btnAddNewOnAction(ActionEvent actionEvent) {
    }

    public void newBatchOnAction(ActionEvent actionEvent) throws IOException {

        if(!txtSelectedProdId.getText().isEmpty()){
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/NewBatchForm.fxml"));
            Parent parent = fxmlLoader.load();
            NewBatchFormController controller = fxmlLoader.getController();
            controller.setProductCode(Integer.parseInt(txtSelectedProdId.getText()),
                    txtSelectedProdDescription.getText());
            Stage stage =  new Stage();
            stage.centerOnScreen();
            stage.setScene(new Scene(parent));
            stage.show();
        }else{
            new Alert(Alert.AlertType.WARNING,"Please select a valid one!");
        }

    }
}