package com.asx.assignment.ums.rest.dto;

import com.asx.assignment.ums.model.Gender;
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
public class UserDTO {
    private long id;
    @NotEmpty
    private String title;
    @NotEmpty
    @JsonProperty("firstname")
    private String firstName;
    @NotEmpty
    @JsonProperty("lastname")
    private String lastName;
    @NotNull
    @Valid
    private Gender gender;
    @Valid
    private AddressDTO address;

}
