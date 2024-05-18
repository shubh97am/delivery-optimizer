package com.lucidity.deliveryoptimizer.dao.impl;

import com.lucidity.deliveryoptimizer.common.mysql.dao.impl.CrudJPADAOImpl;
import com.lucidity.deliveryoptimizer.dao.AddressDao;
import com.lucidity.deliveryoptimizer.entity.Address;
import org.springframework.stereotype.Component;


@Component
public class AddressDaoImpl extends CrudJPADAOImpl<Address, Long> implements AddressDao {
}