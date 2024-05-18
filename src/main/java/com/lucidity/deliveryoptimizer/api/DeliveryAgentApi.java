package com.lucidity.deliveryoptimizer.api;

import com.lucidity.deliveryoptimizer.common.constants.Constant;
import com.lucidity.deliveryoptimizer.domain.entry.DeliveryAgentEntry;
import com.lucidity.deliveryoptimizer.domain.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(Constant.Controller.BASE_URL)
public interface DeliveryAgentApi {


    @PostMapping(Constant.DeliveryAgentController.CREATE_AGENT)
    public ResponseEntity<Response> createAgent(@RequestBody DeliveryAgentEntry input);

    @GetMapping(Constant.DeliveryAgentController.GET_AGENT)
    public ResponseEntity<Response> getAgent(@PathVariable("agentId") Long agentId);

    @PutMapping(Constant.DeliveryAgentController.UPDATE_AGENT_BASIC_DATA)
    public ResponseEntity<Response> updateAgentBasic(@PathVariable("agentId") Long agentId, @RequestBody DeliveryAgentEntry input);


    @PutMapping(Constant.DeliveryAgentController.UPDATE_AGENT_SERVICEABILITY)
    public ResponseEntity<Response> updateAgentServiceability(@PathVariable("agentId") Long agentId, @RequestBody DeliveryAgentEntry input);

    @PutMapping(Constant.DeliveryAgentController.UPDATE_AGENT_CURRENT_LOCATION)
    public ResponseEntity<Response> updateAgentCurrentLocation(@PathVariable("agentId") Long agentId, @RequestBody DeliveryAgentEntry input);

    @GetMapping(Constant.DeliveryAgentController.GET_ALL_AGENTS)
    public ResponseEntity<Response> getAllAgents();

}
