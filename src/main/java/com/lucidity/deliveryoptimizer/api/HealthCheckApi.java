package com.lucidity.deliveryoptimizer.api;


import com.lucidity.deliveryoptimizer.common.constants.Constant;
import com.lucidity.deliveryoptimizer.domain.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constant.Controller.BASE_URL)
public interface HealthCheckApi {

    //call this api every 5 seconds to check service up/down status
    @GetMapping(Constant.HeartBeatController.HEARTBEAT_BASE_PATH)
    public ResponseEntity<Response> heartbeat();


    //call this api whenever deploy service
    // while killing and bringing out of box set beat as false
    // while bringing back to box set beat as true
    @PutMapping(Constant.HeartBeatController.HEARTBEAT_BASE_PATH)
    public ResponseEntity<Response> heartbeat(@RequestParam("beat") boolean beat);
}
