package com.lucidity.deliveryoptimizer.api.impl;

import com.lucidity.deliveryoptimizer.api.DeliveryAgentApi;
import com.lucidity.deliveryoptimizer.common.annotations.EnableLogging;
import com.lucidity.deliveryoptimizer.domain.entry.DeliveryAgentEntry;
import com.lucidity.deliveryoptimizer.domain.response.APIResponse;
import com.lucidity.deliveryoptimizer.domain.response.Response;
import com.lucidity.deliveryoptimizer.manager.DeliveryAgentManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DeliveryAgentApiImpl implements DeliveryAgentApi {

    private final DeliveryAgentManager deliveryAgentManager;

    public DeliveryAgentApiImpl(DeliveryAgentManager deliveryAgentManager) {
        this.deliveryAgentManager = deliveryAgentManager;
    }

    @EnableLogging
    @Override
    public ResponseEntity<Response> createAgent(DeliveryAgentEntry input) {
        DeliveryAgentEntry agent = deliveryAgentManager.addAgent(input);
        return APIResponse.renderSuccess(agent, 200, HttpStatus.OK);
    }

    @EnableLogging
    @Override
    public ResponseEntity<Response> getAgent(Long agentId) {
        DeliveryAgentEntry agent = deliveryAgentManager.getAgent(agentId);
        return APIResponse.renderSuccess(agent, 200, HttpStatus.OK);
    }

    @EnableLogging
    @Override
    public ResponseEntity<Response> updateAgentBasic(Long agentId, DeliveryAgentEntry input) {
        DeliveryAgentEntry agent = deliveryAgentManager.updateAgentBasicDetails(agentId, input);
        return APIResponse.renderSuccess(agent, 200, HttpStatus.OK);
    }

    @EnableLogging
    @Override
    public ResponseEntity<Response> updateAgentServiceability(Long agentId, DeliveryAgentEntry input) {
        DeliveryAgentEntry agent = deliveryAgentManager.updateAgentOnDutyStatus(agentId, input);
        return APIResponse.renderSuccess(agent, 200, HttpStatus.OK);
    }

    @EnableLogging
    @Override
    public ResponseEntity<Response> updateAgentCurrentLocation(Long agentId, DeliveryAgentEntry input) {
        DeliveryAgentEntry agent = deliveryAgentManager.updateAgentCurrentLocation(agentId, input);
        return APIResponse.renderSuccess(agent, 200, HttpStatus.OK);
    }

    @EnableLogging
    @Override
    public ResponseEntity<Response> getAllAgents() {
        List<DeliveryAgentEntry> agents = deliveryAgentManager.getAllAgents();
        return APIResponse.renderSuccess(agents, 200, HttpStatus.OK);
    }
}
