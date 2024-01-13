package com.devstack.pos.dao.custom;

import com.devstack.pos.dao.CrudDao;
import com.devstack.pos.entity.User;

import java.sql.SQLException;


public interface UserDao extends CrudDao<User, String> {

    public boolean delete(String userId) throws SQLException, ClassNotFoundException;
}
