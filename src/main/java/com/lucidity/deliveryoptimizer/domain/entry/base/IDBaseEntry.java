package com.lucidity.deliveryoptimizer.domain.entry.base;

import java.io.Serializable;

public abstract class IDBaseEntry<ID extends Serializable> extends BaseEntry {

    public abstract ID getId();

    public abstract void setId(ID id);

}