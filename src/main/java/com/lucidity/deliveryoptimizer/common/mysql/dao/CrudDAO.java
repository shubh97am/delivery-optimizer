package com.lucidity.deliveryoptimizer.common.mysql.dao;


import com.lucidity.deliveryoptimizer.common.mysql.entity.BaseEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public interface CrudDAO <T extends BaseEntity<ID>, ID extends Serializable> {

	public T create (T entity);

	public List<T> bulkCreate (List<T> entities);

	public T update (ID id, T entity);
	
	public List<T> update (List<T> entities);
	
	public T findById(ID id);
	
	public List<T> findByIds(Set<ID> ids);
	
	public List<T> findAll();

	
	public void deleteById(ID id);
	
}
