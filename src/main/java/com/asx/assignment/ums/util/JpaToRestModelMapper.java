package com.asx.assignment.ums.util;

import com.asx.assignment.ums.model.Address;
import com.asx.assignment.ums.model.User;
import com.asx.assignment.ums.rest.dto.AddressDTO;
import com.asx.assignment.ums.rest.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface JpaToRestModelMapper {
    JpaToRestModelMapper INSTANCE = Mappers.getMapper(JpaToRestModelMapper.class);
    AddressDTO map(Address address);
    UserDTO map(User user);
}
