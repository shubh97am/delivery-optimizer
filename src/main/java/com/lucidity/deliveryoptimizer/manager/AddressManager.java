package com.lucidity.deliveryoptimizer.manager;

import com.lucidity.deliveryoptimizer.domain.entry.AddressEntry;
import org.springframework.transaction.annotation.Transactional;

public interface AddressManager {

    //there is no provision of updating particular address
    //it will cause more handling
    // whenever user wants to update the address we will create new address entry for that and then set new address for user
    @Transactional(rollbackFor = Exception.class)
    AddressEntry addAddress(AddressEntry input);


    @Transactional(rollbackFor = Exception.class)
    AddressEntry updateAddress(Long id, AddressEntry input);

    @Transactional(readOnly = true)
    AddressEntry getAddress(Long id);
}
