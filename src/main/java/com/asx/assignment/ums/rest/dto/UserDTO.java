package com.asx.assignment.ums.rest.dto;

import com.asx.assignment.ums.model.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private long id;
    private String title;
    private String firstName;
    private String lastName;
    private Gender gender;
    private AddressDTO address;
}
