package com.lucidity.deliveryoptimizer.api.impl;

import com.lucidity.deliveryoptimizer.api.HealthCheckApi;
import com.lucidity.deliveryoptimizer.common.annotations.EnableLogging;
import com.lucidity.deliveryoptimizer.common.annotations.ValidateRequestAccess;
import com.lucidity.deliveryoptimizer.domain.response.APIResponse;
import com.lucidity.deliveryoptimizer.domain.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class HealthCheckApiImpl implements HealthCheckApi {

    public static boolean beat = false;


    @EnableLogging
    @ValidateRequestAccess
    @Override
    public ResponseEntity<Response> heartbeat() {
        if (beat) {
            return APIResponse.renderSuccess("Delivery Cost Optimization service is UP|| Developed By Shubham Malav", 200, HttpStatus.OK);
        } else {
            return APIResponse.renderSuccess("Delivery Cost Optimization service is Down|| Developed By Shubham Malav", 410, HttpStatus.GONE);
        }
    }

    @EnableLogging
    @ValidateRequestAccess
    @Override
    public ResponseEntity<Response> heartbeat(boolean heartbeat) {
        beat = heartbeat;

        return APIResponse.renderSuccess("Delivery Cost Optimization service Heartbeat setup done|| Developed By Shubham Malav", 200, HttpStatus.OK);
    }
}
