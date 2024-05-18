package com.lucidity.deliveryoptimizer.entity;


import com.lucidity.deliveryoptimizer.common.mysql.entity.LongIDJPAEntity;
import com.lucidity.deliveryoptimizer.domain.enumuration.Gender;
import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "user")
@NamedQueries(value = {
        @NamedQuery(name = User.FETCH_USER_BY_PHONE, query = "from User where phone=:phone order by id asc "),
})
@Entity
public class User extends LongIDJPAEntity {

    public static final String FETCH_USER_BY_PHONE = "FETCH_USER_BY_PHONE";

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "age")
    private Integer age;

    //keeping non updatable for now
    // should be unique
    @Column(name = "phone")
    private String phone;

    //for now assuming a user can have only one address at a time
    // extension scope -> to have multiple address for single user
    @Column(name = "address_id")
    private Long addressId;


    //todo we can add more fields here like profileVerified/loggedIn/DateOfBirth......
}
