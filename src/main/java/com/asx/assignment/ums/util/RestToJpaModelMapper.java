package com.asx.assignment.ums.util;

import com.asx.assignment.ums.model.Address;
import com.asx.assignment.ums.model.User;
import com.asx.assignment.ums.rest.dto.AddressDTO;
import com.asx.assignment.ums.rest.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RestToJpaModelMapper {
    static final RestToJpaModelMapper INSTANCE = Mappers.getMapper(RestToJpaModelMapper.class);
    Address map(AddressDTO addressDTO);
    User map(UserDTO userDTO);
}
