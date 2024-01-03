package com.devstack.pos.bo.custom;

import com.devstack.pos.dto.CustomerDto;
import com.devstack.pos.dto.ProductDto;
import com.devstack.pos.entity.Product;

import java.sql.SQLException;
import java.util.List;

public interface ProductBo {
    public boolean saveProduct(ProductDto dto) throws SQLException, ClassNotFoundException;

    public boolean updateProduct(ProductDto dto) throws SQLException, ClassNotFoundException;

    public boolean deleteProduct(int code) throws SQLException, ClassNotFoundException;

    public ProductDto findProduct(int code);

    public List<ProductDto> findAllProducts();

    public int getLastProductId() throws SQLException, ClassNotFoundException;
}
