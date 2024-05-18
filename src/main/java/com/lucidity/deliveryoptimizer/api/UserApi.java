package com.lucidity.deliveryoptimizer.api;

import com.lucidity.deliveryoptimizer.common.constants.Constant;
import com.lucidity.deliveryoptimizer.domain.entry.AddressEntry;
import com.lucidity.deliveryoptimizer.domain.entry.UserEntry;
import com.lucidity.deliveryoptimizer.domain.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constant.Controller.BASE_URL)
public interface UserApi {


    @PostMapping(Constant.UserController.CREATE_USER)
    public ResponseEntity<Response> createUser(@RequestBody UserEntry input);

    @GetMapping(Constant.UserController.GET_USER)
    public ResponseEntity<Response> getUser(@PathVariable("userId") Long userId);


    @PutMapping(Constant.UserController.UPDATE_USER)
    public ResponseEntity<Response> updateUser(@PathVariable("userId") Long userId, @RequestBody UserEntry input);


    @PutMapping(Constant.UserController.ADD_UPDATE_ADDRESS_FOR_USER)
    public ResponseEntity<Response> upsertUserAddress(@PathVariable("userId") Long userId, @RequestBody AddressEntry input);


    //system api to check all existing users
    @GetMapping(Constant.UserController.GET_ALL_USERS)
    public ResponseEntity<Response> getAllUsers();

}
