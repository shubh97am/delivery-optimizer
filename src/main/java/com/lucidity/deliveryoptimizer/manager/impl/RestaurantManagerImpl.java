package com.lucidity.deliveryoptimizer.manager.impl;

import com.lucidity.deliveryoptimizer.common.exception.ExistingEntityNotFoundException;
import com.lucidity.deliveryoptimizer.common.exception.InvalidInputRequestException;
import com.lucidity.deliveryoptimizer.common.util.BeanTransformUtil;
import com.lucidity.deliveryoptimizer.common.util.PhoneNumberValidator;
import com.lucidity.deliveryoptimizer.dao.RestaurantDao;
import com.lucidity.deliveryoptimizer.domain.entry.AddressEntry;
import com.lucidity.deliveryoptimizer.domain.entry.RestaurantEntry;
import com.lucidity.deliveryoptimizer.entity.Restaurant;
import com.lucidity.deliveryoptimizer.manager.AddressManager;
import com.lucidity.deliveryoptimizer.manager.RestaurantManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class RestaurantManagerImpl implements RestaurantManager {

    private final RestaurantDao restaurantDao;
    private final AddressManager addressManager;

    public RestaurantManagerImpl(RestaurantDao restaurantDao, AddressManager addressManager) {
        this.restaurantDao = restaurantDao;
        this.addressManager = addressManager;
    }

    @Override
    public RestaurantEntry addRestaurant(RestaurantEntry input) {
        validateCreateRestaurantRequest(input);

        sanitizeRestaurantCreateRequest(input);

        //here we are not validating on phone whether this is already exist or not
        //but we can add this constraint in the code and DB as well

        AddressEntry address = addressManager.addAddress(input.getAddress());
        if (address == null || address.getId() == null) {
            throw new RuntimeException("Error while creating address for restaurant ");
        }
        //setting newly created address to restaurant context
        input.getAddress().setId(address.getId());

        Restaurant restaurant = convertToEntity(input, null);
        restaurant = restaurantDao.create(restaurant);
        return convertToEntry(restaurant, true);
    }


    //creating this method so that all other updating flows can use same method
    //assuming that all updating flows pass sanitize input here
    @Transactional
    public RestaurantEntry update(Long id, RestaurantEntry input) {
        if (id == null || input == null) {
            throw new InvalidInputRequestException("Invalid Restaurant update Request ");
        }

        Restaurant existingEntity = restaurantDao.findById(id);

        if (existingEntity == null || existingEntity.getId() == null) {
            throw new ExistingEntityNotFoundException("Restaurant not found with given id" + id);
        }

        existingEntity = convertToEntity(input, existingEntity);
        existingEntity = restaurantDao.update(existingEntity.getId(), existingEntity);
        return convertToEntry(existingEntity, true);
    }

    @Override
    public RestaurantEntry updateRestaurant(Long id, RestaurantEntry input) {
        if (id == null || input == null) {
            throw new InvalidInputRequestException("Invalid Restaurant update Request ");
        }

        //not updating address from this flow
        sanitizeRestaurantUpdateRequest(input);
        return this.update(id, input);
    }

    @Override
    public RestaurantEntry updateRestaurantServiceability(Long id, RestaurantEntry input) {
        if (id == null || input == null || input.getServiceable() == null) {
            throw new InvalidInputRequestException("Mandatory fields Missing ");
        }

        RestaurantEntry sanitizeInput = new RestaurantEntry();
        sanitizeInput.setServiceable(input.getServiceable());

        return this.update(id, sanitizeInput);
    }

    @Override
    public RestaurantEntry getRestaurant(Long id) {
        if (id == null) {
            throw new InvalidInputRequestException("Invalid input id ");
        }
        Restaurant restaurant = restaurantDao.findById(id);
        return convertToEntry(restaurant, true);
    }

    @Override
    public List<RestaurantEntry> getAllRestaurants() {
        return new ArrayList<>();
    }


    private void validateCreateRestaurantRequest(RestaurantEntry input) {
        if (input == null) {
            throw new InvalidInputRequestException("Empty Input ");
        }

        StringBuilder sb = new StringBuilder();

        if (StringUtils.isEmpty(input.getName())) {
            sb.append("name field missing ");
        }
        if (StringUtils.isEmpty(input.getPhone())) {
            sb.append("phone field missing ");
        }

        if (input.getAddress() == null) {
            sb.append("address input missing inside restaurant input ");
        } else {
            if (StringUtils.isEmpty(input.getAddress().getAddress())) {
                sb.append("address field is missing ");
            }
            if (input.getAddress().getLatitude() == null) {
                sb.append("latitude field is missing ");
            }
            if (input.getAddress().getLongitude() == null) {
                sb.append("longitude field is missing ");
            }
        }
        if (!StringUtils.isEmpty(sb.toString())) {
            throw new InvalidInputRequestException(sb.toString());
        }
    }

    private void sanitizeRestaurantCreateRequest(RestaurantEntry input) {
        if (input == null) {
            return;
        }
        //since we do not want serviceable to be set via request while creating restaurant

        input.setServiceable(null);
    }

    private void sanitizeRestaurantUpdateRequest(RestaurantEntry input) {
        if (input == null) {
            return;
        }
        //since we do not want address to be updated for a restaurant
        input.setAddress(null);
    }

    private Restaurant convertToEntity(RestaurantEntry entry, Restaurant entity) {
        if (entry == null) {
            return entity;
        }

        if (entity == null) {
            entity = new Restaurant();

            //address is not updatable for restaurant for now
            if (entry.getAddress() != null && entry.getAddress().getId() != null) {
                entity.setAddressId(entry.getAddress().getId());
            }
            entity.setServiceable(true);
        }

        if (!StringUtils.isEmpty(entry.getName())) {
            entity.setName(entry.getName());
        }
        if (!StringUtils.isEmpty(entry.getPhone())) {
            entity.setPhone(entry.getPhone());
        }
        if (entry.getServiceable() != null) {
            entity.setServiceable(entry.getServiceable());
        }

        if (!StringUtils.isEmpty(entity.getPhone())) {
            if (!PhoneNumberValidator.validPhone(entity.getPhone())) {
                throw new RuntimeException("Please Enter Valid Phone Number");
            }
        }


        return entity;
    }


    private RestaurantEntry convertToEntry(Restaurant entity, boolean fetchAddress) {
        if (entity == null) {
            return null;
        }

        RestaurantEntry entry = new RestaurantEntry();
        entry.setId(entity.getId());

        BeanTransformUtil.copyProperties(entity, entry);

        if (fetchAddress && entity.getAddressId() != null) {
            AddressEntry address = addressManager.getAddress(entity.getAddressId());
            entry.setAddress(address);
        }
        return entry;
    }
}
