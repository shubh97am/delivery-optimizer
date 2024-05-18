package com.lucidity.deliveryoptimizer.dao.impl;

import com.lucidity.deliveryoptimizer.common.mysql.dao.impl.CrudJPADAOImpl;
import com.lucidity.deliveryoptimizer.dao.DeliveryAgentDao;
import com.lucidity.deliveryoptimizer.entity.DeliveryAgent;
import org.springframework.stereotype.Component;

@Component
public class DeliveryAgentDaoImpl extends CrudJPADAOImpl<DeliveryAgent, Long> implements DeliveryAgentDao {
}
