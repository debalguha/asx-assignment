package com.asx.assignment.ums.rest.dto;

import com.asx.assignment.ums.model.State;
import com.asx.assignment.ums.rest.validation.IdValidator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({ "id", "street", "city", "state", "postcode" })
public class AddressDTO {
    @Valid
    @IdValidator
    private String id;
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
