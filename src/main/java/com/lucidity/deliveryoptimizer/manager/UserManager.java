package com.lucidity.deliveryoptimizer.manager;

import com.lucidity.deliveryoptimizer.domain.entry.AddressEntry;
import com.lucidity.deliveryoptimizer.domain.entry.UserEntry;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserManager {


    //we do not allow address update via createUser or updateUser API->
    //we have separate API to upsert Address for User

    @Transactional(rollbackFor = Exception.class)
    UserEntry createUser(UserEntry input);

    @Transactional(readOnly = true)
    UserEntry getUser(Long id);

    @Transactional(rollbackFor = Exception.class)
    UserEntry updateUser(Long userId, UserEntry input);

    @Transactional(rollbackFor = Exception.class)
    UserEntry upsertUserAddress(Long userId, AddressEntry address);

    //todo add support for pagination and filter here
    @Transactional(readOnly = true)
    List<UserEntry> getAllUsers();
}
