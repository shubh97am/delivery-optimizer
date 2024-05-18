package com.lucidity.deliveryoptimizer.domain.entry;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lucidity.deliveryoptimizer.domain.entry.base.IDBaseEntry;
import com.lucidity.deliveryoptimizer.domain.enumuration.Gender;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserEntry extends IDBaseEntry<Long> {
    private Long id;
    private String name;
    private Gender gender;
    private Integer age;
    private String phone;

    //@here we can add functionality to have multiple address for a user (List<Address> address)
    // for simplicity we are assuming user can have only one address at a time

    private AddressEntry address;


    @Override
    public String toString() {
        return "UserEntry{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gender=" + gender +
                ", age=" + age +
                ", phone='" + phone + '\'' +
                '}';
    }
}
