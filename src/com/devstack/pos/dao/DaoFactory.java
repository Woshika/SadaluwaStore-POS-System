package com.devstack.pos.dao;

import com.devstack.pos.dao.custom.impl.CustomerDaoImpl;
import com.devstack.pos.dao.custom.impl.ProductDaoImpl;
import com.devstack.pos.dao.custom.impl.UserDaoImpl;
import com.devstack.pos.enums.DaoType;

public class DaoFactory {
    private static DaoFactory daoFactory;

    private DaoFactory(){}

    public static DaoFactory getInstance(){
        return (daoFactory==null)?daoFactory = new DaoFactory() : daoFactory;
    }

    public SuperDao getDao(DaoType daoType){
        switch(daoType){
            case USER:
                return new UserDaoImpl();
            case CUSTOMER:
                return new CustomerDaoImpl();
            case PRODUCT:
                return new ProductDaoImpl();
            default:
                return null;
        }
    }
}
