package com.lucidity.deliveryoptimizer.dao.impl;

import com.lucidity.deliveryoptimizer.common.mysql.dao.impl.CrudJPADAOImpl;
import com.lucidity.deliveryoptimizer.dao.UserDao;
import com.lucidity.deliveryoptimizer.entity.User;
import org.springframework.stereotype.Component;

import javax.persistence.NoResultException;
import javax.persistence.Query;

@Component
public class UserDaoImpl extends CrudJPADAOImpl<User, Long> implements UserDao {
    @Override
    public User findUserByPhone(String phone) {
        try {
            Query query = getEntityManager().createNamedQuery(User.FETCH_USER_BY_PHONE);
            query.setParameter("phone", phone);
            query.setMaxResults(1);
            return (User) query.getSingleResult();
        } catch (Exception e) {
            if (e instanceof NoResultException)
                return null;
            else
                throw new RuntimeException(e);
        }
    }
}