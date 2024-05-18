package com.lucidity.deliveryoptimizer.manager.impl;

import com.lucidity.deliveryoptimizer.common.exception.InvalidInputRequestException;
import com.lucidity.deliveryoptimizer.common.util.BeanTransformUtil;
import com.lucidity.deliveryoptimizer.common.util.PhoneNumberValidator;
import com.lucidity.deliveryoptimizer.dao.DeliveryAgentDao;
import com.lucidity.deliveryoptimizer.domain.entry.DeliveryAgentEntry;
import com.lucidity.deliveryoptimizer.entity.DeliveryAgent;
import com.lucidity.deliveryoptimizer.manager.DeliveryAgentManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class DeliveryAgentManagerImpl implements DeliveryAgentManager {

    private final DeliveryAgentDao deliveryAgentDao;

    public DeliveryAgentManagerImpl(DeliveryAgentDao deliveryAgentDao) {
        this.deliveryAgentDao = deliveryAgentDao;
    }

    @Override
    public DeliveryAgentEntry addAgent(DeliveryAgentEntry input) {
        validateCreateAgentRequest(input);
        DeliveryAgent agent = convertToEntity(input, null);
        agent = deliveryAgentDao.create(agent);
        return convertToEntry(agent);
    }

    //creating this method so that all other updating flows can use same method
    //assuming that all updating flows pass sanitize input here
    @Transactional(rollbackFor = Exception.class)
    public DeliveryAgentEntry update(Long id, DeliveryAgentEntry input) {

        if (id == null || input == null) {
            throw new InvalidInputRequestException("Invalid Delivery Agent Update Request");
        }

        DeliveryAgent agent = deliveryAgentDao.findById(id);
        if (agent == null) {
            throw new RuntimeException("Agent Not Found For Id=" + id);
        }

        agent = convertToEntity(input, agent);
        agent = deliveryAgentDao.update(agent.getId(), agent);

        return convertToEntry(agent);
    }


    @Override
    public DeliveryAgentEntry updateAgentBasicDetails(Long id, DeliveryAgentEntry input) {
        if (id == null || input == null) {
            throw new RuntimeException("Please provide all the fields");
        }
        DeliveryAgentEntry sanitizeInput = new DeliveryAgentEntry();
        sanitizeInput.setName(input.getName());
        sanitizeInput.setPhone(input.getPhone());

        return this.update(id, sanitizeInput);
    }

    @Override
    public DeliveryAgentEntry updateAgentOnDutyStatus(Long id, DeliveryAgentEntry input) {
        if (id == null || input == null || input.getOnDuty() == null) {
            throw new RuntimeException("Please provide all the fields");
        }
        DeliveryAgentEntry sanitizeInput = new DeliveryAgentEntry();
        sanitizeInput.setOnDuty(input.getOnDuty());

        return this.update(id, sanitizeInput);

    }

    @Override
    public DeliveryAgentEntry updateAgentCurrentLocation(Long id, DeliveryAgentEntry input) {
        if (id == null || input == null) {
            throw new RuntimeException("Empty Input Please provide valid input");
        }

        validateLocationUpdateRequest(input);

        DeliveryAgentEntry sanitizeInput = new DeliveryAgentEntry();
        sanitizeInput.setLatitude(input.getLatitude());
        sanitizeInput.setLongitude(input.getLongitude());

        return this.update(id, sanitizeInput);
    }

    @Override
    public DeliveryAgentEntry getAgent(Long id) {
        if (id == null) {
            throw new RuntimeException("Invalid input");
        }
        DeliveryAgent agent = deliveryAgentDao.findById(id);
        return convertToEntry(agent);
    }

    @Override
    public List<DeliveryAgentEntry> getAllAgents() {
        return new ArrayList<>();
    }


    private void validateCreateAgentRequest(DeliveryAgentEntry input) {
        if (input == null) {
            throw new RuntimeException("Invalid Input");
        }
        StringBuilder sb = new StringBuilder();

        if (StringUtils.isEmpty(input.getName())) {
            sb.append("name field missing in input ");
        }
        if (input.getLongitude() == null) {
            sb.append("longitude field missing in input ");
        }
        if (input.getLatitude() == null) {
            sb.append("latitude field missing in input ");
        }
        if (StringUtils.isEmpty(input.getPhone())) {
            sb.append("phone field missing in input ");
        }
        if (input.getGender() == null) {
            sb.append("gender field missing in input ");
        }

        if (!StringUtils.isEmpty(sb.toString())) {
            throw new InvalidInputRequestException(sb.toString());
        }
    }


    private void validateLocationUpdateRequest(DeliveryAgentEntry input) {
        if (input == null) {
            throw new RuntimeException("Empty Input Please provide valid input");
        }

        StringBuilder sb = new StringBuilder();
        if (input.getLongitude() == null) {
            sb.append("longitude is missing in input ");
        }
        if (input.getLatitude() == null) {
            sb.append("latitude is missing in input ");
        }
        if (!StringUtils.isEmpty(sb.toString())) {
            throw new InvalidInputRequestException(sb.toString());
        }
    }

    private DeliveryAgent convertToEntity(DeliveryAgentEntry entry, DeliveryAgent entity) {
        if (entry == null) {
            return entity;
        }

        if (entity == null) {
            entity = new DeliveryAgent();
            entity.setOnDuty(true);
        }

        if (!StringUtils.isEmpty(entry.getName())) {
            entity.setName(entry.getName());
        }
        if (entry.getGender() != null) {
            entity.setGender(entry.getGender());
        }
        if (!StringUtils.isEmpty(entry.getPhone())) {
            entity.setPhone(entry.getPhone());
        }
        if (entry.getLatitude() != null) {
            entity.setLatitude(entry.getLatitude());
        }
        if (entry.getLongitude() != null) {
            entity.setLongitude(entry.getLongitude());
        }
        if (entry.getOnDuty() != null) {
            entity.setOnDuty(entry.getOnDuty());
        }

        if (!StringUtils.isEmpty(entity.getPhone())) {
            if (!PhoneNumberValidator.validPhone(entity.getPhone())) {
                throw new RuntimeException("Please Enter Valid Phone Number ");
            }
        }
        return entity;
    }


    private DeliveryAgentEntry convertToEntry(DeliveryAgent entity) {
        if (entity == null) {
            return null;
        }

        DeliveryAgentEntry entry = new DeliveryAgentEntry();
        entry.setId(entity.getId());

        BeanTransformUtil.copyProperties(entity, entry);
        return entry;
    }
}
