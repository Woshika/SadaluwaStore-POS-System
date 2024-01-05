package com.devstack.pos.bo.custom;

import com.devstack.pos.bo.SuperBo;

import com.devstack.pos.dto.CustomerDto;
import com.devstack.pos.dto.ProductDto;
import com.devstack.pos.entity.Product;

import java.sql.SQLException;
import java.util.List;

public interface ProductBo extends SuperBo {
    public boolean saveProduct(ProductDto dto) throws SQLException, ClassNotFoundException;

    public boolean updateProduct(ProductDto dto) throws SQLException, ClassNotFoundException;

    public boolean deleteProduct(int code) throws SQLException, ClassNotFoundException;

    public Product findProduct(int code) throws SQLException, ClassNotFoundException;

    public List<ProductDto> findAllProducts() throws SQLException, ClassNotFoundException;

    public int getLastProductId() throws SQLException, ClassNotFoundException;

    public List<ProductDto> searchProducts(String searchText) throws SQLException, ClassNotFoundException;

}
