package com.lucidity.deliveryoptimizer.dao;

import com.lucidity.deliveryoptimizer.common.mysql.dao.jpa.CrudJPADAO;
import com.lucidity.deliveryoptimizer.entity.User;


public interface UserDao extends CrudJPADAO<User, Long> {

    public User findUserByPhone(String phone);

}
