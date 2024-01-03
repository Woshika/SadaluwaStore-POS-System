package com.devstack.pos.bo.custom.impl;

import com.devstack.pos.bo.custom.ProductBo;
import com.devstack.pos.dao.DaoFactory;
import com.devstack.pos.dao.custom.ProductDao;
import com.devstack.pos.dto.ProductDto;
import com.devstack.pos.entity.Product;
import com.devstack.pos.enums.DaoType;

import java.sql.SQLException;
import java.util.List;

public class ProductBoImpl implements ProductBo {

    ProductDao productDao =  DaoFactory.getInstance().getDao(DaoType.PRODUCT);

    @Override
    public boolean saveProduct(ProductDto dto) throws SQLException, ClassNotFoundException {
        return productDao.save(new Product(dto.getCode(), dto.getDescription()));
    }

    @Override
    public boolean updateProduct(ProductDto dto) throws SQLException, ClassNotFoundException {
        return productDao.update(new Product(dto.getCode(), dto.getDescription()));
    }

    @Override
    public boolean deleteProduct(int code) throws SQLException, ClassNotFoundException {
        return productDao.delete(code);
    }

    @Override
    public ProductDto findProduct(int code) {
        return null;
    }

    @Override
    public List<ProductDto> findAllProducts() {
        return null;
    }

    public int getLastProductId() throws ClassNotFoundException, SQLException {
        return productDao.getLastProductId();
    }
}
