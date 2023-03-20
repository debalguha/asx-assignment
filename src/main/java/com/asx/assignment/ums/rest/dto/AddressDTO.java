package com.asx.assignment.ums.rest.dto;

import com.asx.assignment.ums.model.State;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
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
    @JsonProperty("postcode")
    private int postCode;
}
