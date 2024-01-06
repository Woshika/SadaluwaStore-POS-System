package com.devstack.pos.bo.custom.impl;

import com.devstack.pos.bo.custom.ProductBo;
import com.devstack.pos.dao.CrudUtil;
import com.devstack.pos.dao.DaoFactory;
import com.devstack.pos.dao.custom.ProductDao;
import com.devstack.pos.dto.ProductDto;
import com.devstack.pos.entity.Product;
import com.devstack.pos.enums.DaoType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
    public Product findProduct(int code) throws SQLException, ClassNotFoundException {

        ResultSet resultSet = CrudUtil.execute("SELECT * FROM customer WHERE code=?",code);
        if(resultSet.next()){
            return new Product(
                    resultSet.getInt(1),
                    resultSet.getString(2)
            );
        }
        return null;
    }

    @Override
    public List<ProductDto> findAllProducts(String searchText) throws SQLException, ClassNotFoundException {
        List<ProductDto> dtos = new ArrayList<>();
        for(Product p : productDao.findAll()){
            dtos.add(new ProductDto(p.getCode(), p.getDescription()));
        }
        return dtos;
    }

    public int getLastProductId() throws ClassNotFoundException, SQLException {
        return productDao.getLastProductId();
    }

    @Override
    public List<ProductDto> searchProducts(String searchText) throws SQLException, ClassNotFoundException {
        searchText = "%"+searchText + "%";
        List<ProductDto> dtos = new ArrayList<>();
        for(Product c:productDao.searchProducts(searchText)){
            dtos.add (new ProductDto(
                    c.getCode(),
                    c.getDescription()
            ));
        }
        return dtos;
    }
}
