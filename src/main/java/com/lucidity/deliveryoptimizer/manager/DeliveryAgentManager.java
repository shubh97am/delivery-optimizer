package com.lucidity.deliveryoptimizer.manager;

import com.lucidity.deliveryoptimizer.domain.entry.DeliveryAgentEntry;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DeliveryAgentManager {

    @Transactional(rollbackFor = Exception.class)
    public DeliveryAgentEntry addAgent(DeliveryAgentEntry input);

    //will update agent basic details like name, phone
    @Transactional(rollbackFor = Exception.class)
    public DeliveryAgentEntry updateAgentBasicDetails(Long id, DeliveryAgentEntry input);

    //this will be in use to update agent availability
    @Transactional(rollbackFor = Exception.class)
    public DeliveryAgentEntry updateAgentOnDutyStatus(Long id, DeliveryAgentEntry input);


    //this will be in use to update current location of delivery agent
    @Transactional(rollbackFor = Exception.class)
    public DeliveryAgentEntry updateAgentCurrentLocation(Long id, DeliveryAgentEntry input);

    @Transactional(readOnly = true)
    public DeliveryAgentEntry getAgent(Long id);


    //here we can add other api to get serviceableRestaurants for a location
    //the logic will return list of serviceable restaurants for a user's location within a distance range


    //todo add support for pagination and filter here
    @Transactional(readOnly = true)
    List<DeliveryAgentEntry> getAllAgents();
}
