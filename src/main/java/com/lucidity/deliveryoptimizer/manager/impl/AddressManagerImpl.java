package com.lucidity.deliveryoptimizer.manager.impl;

import com.lucidity.deliveryoptimizer.common.exception.ExistingEntityNotFoundException;
import com.lucidity.deliveryoptimizer.common.exception.InvalidInputRequestException;
import com.lucidity.deliveryoptimizer.common.util.BeanTransformUtil;
import com.lucidity.deliveryoptimizer.dao.AddressDao;
import com.lucidity.deliveryoptimizer.domain.entry.AddressEntry;
import com.lucidity.deliveryoptimizer.entity.Address;
import com.lucidity.deliveryoptimizer.manager.AddressManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class AddressManagerImpl implements AddressManager {

    private final AddressDao addressDao;

    public AddressManagerImpl(AddressDao addressDao) {
        this.addressDao = addressDao;
    }


    @Override
    public AddressEntry addAddress(AddressEntry input) {
        validateCreateAddressRequest(input);

        Address address = convertToEntity(input, null);
        address = addressDao.create(address);
        return convertToEntry(address);
    }

    @Override
    public AddressEntry updateAddress(Long id, AddressEntry input) {
        if (id == null || input == null) {
            throw new InvalidInputRequestException("Invalid Input ");
        }
        Address existingAddress = addressDao.findById(id);
        if (existingAddress == null) {
            throw new ExistingEntityNotFoundException("Address not found for id=" + id);
        }

        //transforming input to updated values
        existingAddress = convertToEntity(input, existingAddress);

        //updating
        existingAddress = addressDao.update(existingAddress.getId(), existingAddress);
        return convertToEntry(existingAddress);

    }

    @Override
    public AddressEntry getAddress(Long id) {
        if (id == null) {
            throw new InvalidInputRequestException("Invalid Input ");
        }
        Address address = addressDao.findById(id);
        return convertToEntry(address);
    }


    private void validateCreateAddressRequest(AddressEntry addressInput) {

        if (addressInput == null) {
            throw new InvalidInputRequestException("Invalid Address Input ");
        }

        StringBuilder sb = new StringBuilder();

        if (StringUtils.isEmpty(addressInput.getAddress())) {
            sb.append("address field is missing ");
        }
        if (addressInput.getLatitude() == null) {
            sb.append("latitude field is missing ");
        }
        if (addressInput.getLongitude() == null) {
            sb.append("longitude field is missing ");
        }
        if (!StringUtils.isEmpty(sb.toString())) {
            throw new InvalidInputRequestException(sb.toString());
        }
    }

    private Address convertToEntity(AddressEntry entry, Address entity) {
        if (entry == null)
            return entity;

        if (entity == null) {
            entity = new Address();
        }

        if (!StringUtils.isEmpty(entry.getAddress())) {
            entity.setAddress(entry.getAddress());
        }
        if (entry.getLatitude() != null) {
            entity.setLatitude(entry.getLatitude());
        }
        if (entry.getLatitude() != null) {
            entity.setLongitude(entry.getLongitude());
        }
        return entity;
    }


    private AddressEntry convertToEntry(Address entity) {
        if (entity == null)
            return null;
        AddressEntry entry = new AddressEntry();
        entry.setId(entity.getId());

        BeanTransformUtil.copyProperties(entity, entry);
        return entry;
    }
}
