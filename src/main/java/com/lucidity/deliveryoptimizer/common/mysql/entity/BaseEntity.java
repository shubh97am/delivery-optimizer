package com.lucidity.deliveryoptimizer.common.mysql.entity;

import java.io.Serializable;

public interface BaseEntity<ID extends Serializable> extends Serializable {

    public ID getId();

    public void setId(ID id);

}
