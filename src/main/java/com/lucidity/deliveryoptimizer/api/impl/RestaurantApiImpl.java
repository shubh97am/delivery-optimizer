package com.lucidity.deliveryoptimizer.api.impl;

import com.lucidity.deliveryoptimizer.api.RestaurantApi;
import com.lucidity.deliveryoptimizer.common.annotations.EnableLogging;
import com.lucidity.deliveryoptimizer.domain.entry.RestaurantEntry;
import com.lucidity.deliveryoptimizer.domain.response.APIResponse;
import com.lucidity.deliveryoptimizer.domain.response.Response;
import com.lucidity.deliveryoptimizer.manager.RestaurantManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RestaurantApiImpl implements RestaurantApi {

    private final RestaurantManager restaurantManager;

    public RestaurantApiImpl(RestaurantManager restaurantManager) {
        this.restaurantManager = restaurantManager;
    }

    @EnableLogging
    @Override
    public ResponseEntity<Response> createRestaurant(RestaurantEntry input) {
        RestaurantEntry restaurant = restaurantManager.addRestaurant(input);
        return APIResponse.renderSuccess(restaurant, 200, HttpStatus.OK);
    }

    @EnableLogging
    @Override
    public ResponseEntity<Response> getRestaurant(Long restaurantId) {
        RestaurantEntry restaurant = restaurantManager.getRestaurant(restaurantId);
        return APIResponse.renderSuccess(restaurant, 200, HttpStatus.OK);
    }

    @EnableLogging
    @Override
    public ResponseEntity<Response> updateRestaurant(Long restaurantId, RestaurantEntry input) {
        RestaurantEntry restaurant = restaurantManager.updateRestaurant(restaurantId, input);
        return APIResponse.renderSuccess(restaurant, 200, HttpStatus.OK);
    }

    @EnableLogging
    @Override
    public ResponseEntity<Response> updateRestaurantServiceability(Long restaurantId, RestaurantEntry input) {
        RestaurantEntry restaurant = restaurantManager.updateRestaurantServiceability(restaurantId, input);
        return APIResponse.renderSuccess(restaurant, 200, HttpStatus.OK);
    }

    @EnableLogging
    @Override
    public ResponseEntity<Response> getAllRestaurants() {
        List<RestaurantEntry> restaurants = restaurantManager.getAllRestaurants();
        return APIResponse.renderSuccess(restaurants, 200, HttpStatus.OK);
    }
}
