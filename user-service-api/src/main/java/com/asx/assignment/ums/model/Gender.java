package com.asx.assignment.ums.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Optional;

public enum Gender {
    MALE, FEMALE, NA;

    @JsonCreator
    public static Gender factory(String value) {
        switch(Optional.ofNullable(value).orElse("").toUpperCase()) {
            case "MALE": return MALE;
            case "FEMALE": return FEMALE;
            default: return NA;
        }
    }

    @JsonValue
    public String getGenderValue() {
        return this.name().toLowerCase();
    }
}
