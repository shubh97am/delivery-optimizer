package com.lucidity.deliveryoptimizer.dao.impl;

import com.lucidity.deliveryoptimizer.common.mysql.dao.impl.CrudJPADAOImpl;
import com.lucidity.deliveryoptimizer.dao.RestaurantDao;
import com.lucidity.deliveryoptimizer.entity.Restaurant;
import org.springframework.stereotype.Component;

@Component
public class RestaurantDaoImpl extends CrudJPADAOImpl<Restaurant, Long> implements RestaurantDao {
}