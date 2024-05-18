package com.lucidity.deliveryoptimizer.common.mysql.entity;

import javax.persistence.*;

@MappedSuperclass
public abstract class LongIDJPAEntity extends BaseJPAEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, insertable = false)
    protected Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "LongIDEntity{" +
                "id=" + id +
                '}';
    }

}
