package com.asx.assignment.ums.rest.dto;

import com.asx.assignment.ums.model.Gender;
import com.asx.assignment.ums.rest.validation.IdValidator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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
@JsonPropertyOrder({ "id", "title", "firstname", "lastname", "gender", "address" })
public class UserDTO {
    @Valid
    @IdValidator
    private String id;
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
