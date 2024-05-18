package com.lucidity.deliveryoptimizer.manager;

import com.lucidity.deliveryoptimizer.domain.entry.RestaurantEntry;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RestaurantManager {


    //this method will create restaurant
    //while creating the restaurant address is mandatory
    //address is not updatable for restaurants for now
    //no constraint on phone number on restaurant for now
    @Transactional(rollbackFor = Exception.class)
    public RestaurantEntry addRestaurant(RestaurantEntry input);

    //will update restaurant basic details like name, phone
    //also will be in use to set restaurant serviceable true or false
    @Transactional(rollbackFor = Exception.class)
    public RestaurantEntry updateRestaurant(Long id, RestaurantEntry input);


    @Transactional(rollbackFor = Exception.class)
    public RestaurantEntry updateRestaurantServiceability(Long id, RestaurantEntry input);

    @Transactional(readOnly = true)
    public RestaurantEntry getRestaurant(Long id);


    //here we can add other api to get serviceableRestaurants for a location
    //the logic will return list of serviceable restaurants for a user's location within a distance range


    //todo add support for pagination and filter here
    @Transactional(readOnly = true)
    List<RestaurantEntry> getAllRestaurants();
}
