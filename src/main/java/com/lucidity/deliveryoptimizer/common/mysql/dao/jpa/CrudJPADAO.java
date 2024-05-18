package com.lucidity.deliveryoptimizer.common.mysql.dao.jpa;

import com.lucidity.deliveryoptimizer.common.mysql.dao.CrudDAO;
import com.lucidity.deliveryoptimizer.common.mysql.entity.BaseJPAEntity;

import java.io.Serializable;

public interface CrudJPADAO<T extends BaseJPAEntity<ID>, ID extends Serializable> extends CrudDAO<T, ID> {

	public void detachEntity(T entity);
}
