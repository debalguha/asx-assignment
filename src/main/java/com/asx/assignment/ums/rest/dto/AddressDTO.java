package com.asx.assignment.ums.rest.dto;

import com.asx.assignment.ums.model.State;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {
    @NotEmpty
    private String street;
    @NotEmpty
    private String city;
    @NotNull
    @Valid
    private State state;
    @Min(1111)
    private int postCode;
}
