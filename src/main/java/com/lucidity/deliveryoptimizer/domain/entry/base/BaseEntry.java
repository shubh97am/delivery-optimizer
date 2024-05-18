package com.lucidity.deliveryoptimizer.domain.entry.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ToString
public abstract class BaseEntry implements Serializable {

    private String createdBy;
    private Date createdOn;
    private Date lastModifiedOn;
    private Long version;
}
