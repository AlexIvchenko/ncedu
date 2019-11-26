package com.ncedu.nc_edu.services;

import com.ncedu.nc_edu.models.ItemCategory;
import com.ncedu.nc_edu.models.RationItem;
import com.ncedu.nc_edu.models.Receipt;
import com.ncedu.nc_edu.models.User;
import com.ncedu.nc_edu.repositories.RationItemRepository;

import java.util.Date;
import java.util.UUID;

public interface RationItemService {

    RationItem create (User user, Date date, Receipt receipt, ItemCategory category);
}
