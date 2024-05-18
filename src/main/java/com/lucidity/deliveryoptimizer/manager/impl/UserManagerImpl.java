package com.lucidity.deliveryoptimizer.manager.impl;

import com.lucidity.deliveryoptimizer.common.exception.ExistingEntityNotFoundException;
import com.lucidity.deliveryoptimizer.common.exception.InvalidInputRequestException;
import com.lucidity.deliveryoptimizer.common.util.BeanTransformUtil;
import com.lucidity.deliveryoptimizer.common.util.PhoneNumberValidator;
import com.lucidity.deliveryoptimizer.dao.UserDao;
import com.lucidity.deliveryoptimizer.domain.entry.AddressEntry;
import com.lucidity.deliveryoptimizer.domain.entry.UserEntry;
import com.lucidity.deliveryoptimizer.entity.User;
import com.lucidity.deliveryoptimizer.manager.AddressManager;
import com.lucidity.deliveryoptimizer.manager.UserManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserManagerImpl implements UserManager {

    //initializing via constructor than @Autowired annotation

    private final UserDao userDao;
    private final AddressManager addressManager;

    public UserManagerImpl(UserDao userDao, AddressManager addressManager) {
        this.userDao = userDao;
        this.addressManager = addressManager;
    }


    @Override
    public UserEntry createUser(UserEntry input) {
        validateCreateUserInput(input);
        sanitizeUserCreateUpdateInput(input);

        User userWithSamePhone = userDao.findUserByPhone(input.getPhone());

        if (userWithSamePhone != null) {
            throw new RuntimeException("User Already Exist with this phone number ");
        }

        User user = convertToEntity(input, null);
        user = userDao.create(user);

        return convertToEntry(user, false);
    }

    @Override
    public UserEntry getUser(Long id) {
        if (id == null) {
            throw new RuntimeException("Invalid Request ");
        }

        User user = userDao.findById(id);
        return convertToEntry(user, true);

    }

    @Override
    public UserEntry updateUser(Long userId, UserEntry input) {
        if (userId == null || input == null) {
            throw new RuntimeException("Invalid Request");
        }
        sanitizeUserCreateUpdateInput(input);

        User user = userDao.findById(userId);
        if (user == null) {
            throw new RuntimeException("User Not Found");
        }
        user = convertToEntity(input, user);
        user = userDao.update(user.getId(), user);
        return convertToEntry(user, false);
    }

    @Override
    public UserEntry upsertUserAddress(Long userId, AddressEntry request) {
        if (userId == null) {
            throw new InvalidInputRequestException("userId Fields Missing ");
        }

        User user = userDao.findById(userId);
        if (user == null) {
            throw new ExistingEntityNotFoundException("User Not Found ");
        }

        validateUpsertUserAddressRequest(request);
        if (user.getAddressId() != null) {
            //that means address already present for that user
            //update that address
            addressManager.updateAddress(user.getAddressId(), request);
        } else {
            //creating address for user
            AddressEntry address = addressManager.addAddress(request);
            if (address != null && address.getId() != null) {

                //adding newly created address for this user
                UserEntry userEntry = new UserEntry();
                userEntry.setAddress(address);
                user = convertToEntity(userEntry, user);
                user = userDao.update(user.getId(), user);
            }
        }
        return convertToEntry(user, true);
    }


    @Override
    public List<UserEntry> getAllUsers() {
        return new ArrayList<>();
    }


    private void validateCreateUserInput(UserEntry input) {

        if (input == null) {
            throw new RuntimeException("Empty User Create Input ");
        }

        StringBuilder sb = new StringBuilder();
        if (StringUtils.isEmpty(input.getName())) {
            sb.append("name field missing in input ");
        }
        if (StringUtils.isEmpty(input.getPhone())) {
            sb.append("phone field missing in input ");
        }

        if (!StringUtils.isEmpty(sb.toString())) {
            throw new InvalidInputRequestException(sb.toString());
        }

    }

    //setting address null since we do not want address to be created/updated via createUser/updateUser Api
    private void sanitizeUserCreateUpdateInput(UserEntry input) {
        if (input == null) {
            return;
        }
        input.setAddress(null);
    }

    private void validateUpsertUserAddressRequest(AddressEntry addressInput) {
        if (addressInput == null) {
            throw new RuntimeException("Invalid Address Input ");
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

    private User convertToEntity(UserEntry entry, User entity) {
        if (entry == null) {
            return entity;
        }

        if (entity == null) {
            entity = new User();
            entity.setPhone(entry.getPhone());
        }

        if (!StringUtils.isEmpty(entry.getName())) {
            entity.setName(entry.getName());
        }
        if (entry.getGender() != null) {
            entity.setGender(entry.getGender());
        }
        if (entry.getAge() != null) {
            entity.setAge(entry.getAge());
        }
        if (entry.getAddress() != null && entry.getAddress().getId() != null) {
            entity.setAddressId(entry.getAddress().getId());
        }

        if (!StringUtils.isEmpty(entity.getPhone())) {
            if (!PhoneNumberValidator.validPhone(entity.getPhone())) {
                throw new RuntimeException("Please Enter Valid Phone Number ");
            }
        }

        return entity;
    }


    private UserEntry convertToEntry(User entity, boolean fetchAddress) {
        if (entity == null) {
            return null;
        }

        UserEntry entry = new UserEntry();
        entry.setId(entity.getId());

        BeanTransformUtil.copyProperties(entity, entry);

        if (fetchAddress && entity.getAddressId() != null) {
            AddressEntry address = addressManager.getAddress(entity.getAddressId());
            entry.setAddress(address);
        }

        return entry;
    }
}
