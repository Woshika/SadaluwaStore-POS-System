package com.devstack.pos.controller;

import com.devstack.pos.bo.BoFactory;
import com.devstack.pos.bo.custom.ProductDetailBo;
import com.devstack.pos.dto.ProductDetailDto;
import com.devstack.pos.enums.BoType;
import com.devstack.pos.util.QrDataGenerator;
import com.devstack.pos.view.tm.ProductDetailTm;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;

public class NewBatchFormController {

    public ImageView barcodeImage;
    public TextField txtQty;
    public TextField txtBuyingPrice;
    public TextField txtSellingPrice;
    public TextField txtShowPrice;
    public TextField txtProductCode;
    public TextField txtSelectedProductDescription;
    public RadioButton rBtnYes;
    public ToggleGroup discount;

    BufferedImage bufferedImage = null;
    String uniqueData=null;

    private ProductDetailBo productDetailBo = BoFactory.getInstance().getBo(BoType.PRODUCT_DETAIL);
     Stage stage = null;

    public void initialize() throws WriterException {
        setQRCode();
    }

    private void setQRCode() throws WriterException {
        uniqueData = QrDataGenerator.generate(25);

        //Generate Barcode
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        bufferedImage =
                MatrixToImageWriter.toBufferedImage(
                        qrCodeWriter.encode(
                                uniqueData,
                                BarcodeFormat.QR_CODE,
                                160,160
                        )
                );
        //Generate Barcode
        Image image = SwingFXUtils.toFXImage(bufferedImage,null);
        barcodeImage.setImage(image);
    }

    public void setDetails(int code, String description, Stage stage, boolean state, ProductDetailTm tm) throws WriterException {
        this.stage=stage;
        if(state){
            try {
                ProductDetailDto productDetail = productDetailBo.findProductDetail(tm.getCode());

                if(productDetail!=null){
                    txtQty.setText(String.valueOf(productDetail.getQtyOnHand()));
                    txtBuyingPrice.setText(String.valueOf(productDetail.getBuyingPrice()));
                    txtSellingPrice.setText(String.valueOf(productDetail.getSellingPrice()));
                    txtShowPrice.setText(String.valueOf(productDetail.getShowPrice()));
                    rBtnYes.setSelected(productDetail.isDiscountAvailability());

                    byte[] data = Base64.decodeBase64(productDetail.getBarcode());
                    barcodeImage.setImage(
                            new Image(new ByteArrayInputStream(data))
                    );

                }else{
                    stage.close();
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else{
            setQRCode();
        }
            txtProductCode.setText(String.valueOf(code));
            txtSelectedProductDescription.setText(description);
    }

    public void saveBatch(ActionEvent actionEvent) throws IOException, SQLException, ClassNotFoundException {
        // Check if bufferedImage is null
        if (bufferedImage == null) {
            new Alert(Alert.AlertType.ERROR, "QR Code image is null!").show();
            return;
        }

        // Check if txtProductCode is not null
        if (txtProductCode != null) {
            String productCodeText = txtProductCode.getText();

            // Check if the product code is not null or empty
            if (productCodeText == null || productCodeText.isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Product Code cannot be null or empty!").show();
                return;
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "Product Code field is null!").show();
            return;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", baos);
        byte[] arr = baos.toByteArray();

        ProductDetailDto dto = new ProductDetailDto(
                uniqueData, Base64.encodeBase64String(arr),
                Integer.parseInt(txtQty.getText()), Double.parseDouble(txtSellingPrice.getText()),
                Double.parseDouble(txtSellingPrice.getText()),
                Double.parseDouble(txtBuyingPrice.getText()),
                Integer.parseInt(txtProductCode.getText()),
                rBtnYes.isSelected() ? true : false
        );
        try {
            if (productDetailBo.saveProductDetail(dto)) {
                new Alert(Alert.AlertType.CONFIRMATION, "Product detail saved!").show();
                Thread.sleep(3000);
                this.stage.close();
            } else {
                new Alert(Alert.AlertType.CONFIRMATION, "Try Again!").show();
            }
        } catch (SQLException | ClassNotFoundException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        productDetailBo.saveProductDetail(dto);
    }
}
