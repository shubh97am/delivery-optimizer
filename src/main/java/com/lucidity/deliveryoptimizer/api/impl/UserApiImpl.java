package com.lucidity.deliveryoptimizer.api.impl;

import com.lucidity.deliveryoptimizer.api.UserApi;
import com.lucidity.deliveryoptimizer.common.annotations.EnableLogging;
import com.lucidity.deliveryoptimizer.domain.entry.AddressEntry;
import com.lucidity.deliveryoptimizer.domain.entry.UserEntry;
import com.lucidity.deliveryoptimizer.domain.response.APIResponse;
import com.lucidity.deliveryoptimizer.domain.response.Response;
import com.lucidity.deliveryoptimizer.manager.UserManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserApiImpl implements UserApi {

    private final UserManager userManager;

    public UserApiImpl(UserManager userManager) {
        this.userManager = userManager;
    }


    @Override
    @EnableLogging
    public ResponseEntity<Response> createUser(UserEntry input) {
        UserEntry userEntry = userManager.createUser(input);
        return APIResponse.renderSuccess(userEntry, 200, HttpStatus.OK);
    }

    @Override
    @EnableLogging
    public ResponseEntity<Response> getUser(Long userId) {
        UserEntry userEntry = userManager.getUser(userId);
        return APIResponse.renderSuccess(userEntry, 200, HttpStatus.OK);
    }

    @Override
    @EnableLogging
    public ResponseEntity<Response> updateUser(Long userId, UserEntry input) {
        UserEntry userEntry = userManager.updateUser(userId, input);
        return APIResponse.renderSuccess(userEntry, 200, HttpStatus.OK);
    }

    @Override
    @EnableLogging
    public ResponseEntity<Response> upsertUserAddress(Long userId, AddressEntry input) {
        UserEntry userEntry = userManager.upsertUserAddress(userId, input);
        return APIResponse.renderSuccess(userEntry, 200, HttpStatus.OK);
    }

    @Override
    @EnableLogging
    public ResponseEntity<Response> getAllUsers() {
        List<UserEntry> users = userManager.getAllUsers();
        return APIResponse.renderSuccess(users, 200, HttpStatus.OK);
    }
}
