package com.asx.assignment.ums.rest.dto;

import com.asx.assignment.ums.model.State;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {
    private String street;
    private String city;
    private State state;
    private int postCode;
}
