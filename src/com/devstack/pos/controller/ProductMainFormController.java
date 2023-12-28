package com.devstack.pos.controller;

import com.devstack.pos.dao.DatabaseAccessCode;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;

import java.sql.SQLException;

public class ProductMainFormController {
    public JFXTextField txtProductCode;
    public TextArea txtProductDescription;
    public JFXButton btnSaveUpdate;

    private String searchText = "";

    public void initialize() throws SQLException, ClassNotFoundException {
        //----load new product Id----
        loadProductId();
    }

    private void loadProductId() throws SQLException, ClassNotFoundException {
        txtProductCode.setText(String.valueOf(new DatabaseAccessCode().getLastProductId()));
    }

    public void btnBackToHomeOnAction(ActionEvent actionEvent) {

    }
    //Save Product
    public void btnNewProductOnAction(ActionEvent actionEvent) {
        try {
            if (btnSaveUpdate.getText().equals("Save Product")) {
                if (new DatabaseAccessCode().saveProduct(Integer.parseInt(txtProductCode.getText()), txtProductDescription.getText())) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Product Saved!").show();
                    clearFields();
                    loadAllProducts(searchText);
                } else {
                    new Alert(Alert.AlertType.WARNING, "Try Again!").show();
                }
            } else {
                if (new DatabaseAccessCode().saveProduct(Integer.parseInt(txtProductCode.getText()), txtProductDescription.getText())) {
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

    private void loadAllProducts(String searchText) {
    }

    //Add new Product
    public void btnAddNewOnAction(ActionEvent actionEvent) {
    }
}