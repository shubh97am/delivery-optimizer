package com.lucidity.deliveryoptimizer.api;

import com.lucidity.deliveryoptimizer.common.constants.Constant;
import com.lucidity.deliveryoptimizer.domain.entry.RestaurantEntry;
import com.lucidity.deliveryoptimizer.domain.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constant.Controller.BASE_URL)
public interface RestaurantApi {


    @PostMapping(Constant.RestaurantController.CREATE_RESTAURANT)
    public ResponseEntity<Response> createRestaurant(@RequestBody RestaurantEntry input);

    @GetMapping(Constant.RestaurantController.GET_RESTAURANT)
    public ResponseEntity<Response> getRestaurant(@PathVariable("restaurantId") Long restaurantId);

    @PutMapping(Constant.RestaurantController.UPDATE_RESTAURANT)
    public ResponseEntity<Response> updateRestaurant(@PathVariable("restaurantId") Long restaurantId, @RequestBody RestaurantEntry input);


    @PutMapping(Constant.RestaurantController.UPDATE_RESTAURANT_SERVICEABILITY)
    public ResponseEntity<Response> updateRestaurantServiceability(@PathVariable("restaurantId") Long restaurantId, @RequestBody RestaurantEntry input);

    @GetMapping(Constant.RestaurantController.GET_ALL_RESTAURANT)
    public ResponseEntity<Response> getAllRestaurants();

}
