package com.devstack.pos.dao.custom;

import com.devstack.pos.entity.Product;

import java.util.List;

public interface ProductDao {

    public boolean saveProduct(Product customer);

    public boolean updateProduct(Product customer);

    public boolean deleteProduct(int code);

    public Product findProduct(int code);

    public List<Product> findAllProducts();
}
