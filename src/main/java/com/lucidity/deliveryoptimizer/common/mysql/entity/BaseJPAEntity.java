package com.lucidity.deliveryoptimizer.common.mysql.entity;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
public abstract class BaseJPAEntity<ID extends Serializable> implements BaseEntity<ID> {

    @Column(name = "created_by", nullable = false, insertable = true, updatable = false)
    protected String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_on", nullable = false, insertable = true, updatable = false)
    protected Date createdOn;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_modified_on", nullable = false, insertable = true, updatable = true)
    protected Date lastModifiedOn;
    @Version
    @Column(name = "version")
    protected Long version = 0L;

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getLastModifiedOn() {
        return lastModifiedOn;
    }

    public void setLastModifiedOn(Date lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @PrePersist
    protected void onCreate() {
        lastModifiedOn = createdOn = ((createdOn == null) ? new Date() : createdOn);
        createdBy="Shubham Malav"; //todo we should pick this from RequestAuthContext but for now hardcoding this value
    }

    @PreUpdate
    protected void onUpdate() {
        lastModifiedOn = new Date();
    }

}
