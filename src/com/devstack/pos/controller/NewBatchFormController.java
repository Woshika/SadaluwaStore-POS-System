package com.devstack.pos.controller;

import com.devstack.pos.util.QrDataGenerator;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.image.BufferedImage;

public class NewBatchFormController {

    public ImageView barcodeImage;
    public TextField txtQty;
    public TextField txtBuyingPrice;
    public TextField txtSellingPrice;
    public TextField txtShowPrice;
    public TextField txtProductCode;
    public TextField txtSelectedProductDescription;
    public RadioButton rBtnYes;

    public void initialize() throws WriterException {
        setQRCode();
    }

    private void setQRCode() throws WriterException {
        StringBuilder uniqueData = QrDataGenerator.generate(25);

        //Generate Barcode
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BufferedImage bufferedImage =
                MatrixToImageWriter.toBufferedImage(
                        qrCodeWriter.encode(
                                String.valueOf(uniqueData),
                                BarcodeFormat.QR_CODE,
                                160,160
                        )
                );
        //Generate Barcode


        Image image = SwingFXUtils.toFXImage(bufferedImage,null);
        barcodeImage.setImage(image);
    }

    public void setProductCode(int code, String description){
        txtProductCode.setText(String.valueOf(code));
        txtSelectedProductDescription.setText(description);
    }

    public void saveBatch(ActionEvent actionEvent) {

    }
}
