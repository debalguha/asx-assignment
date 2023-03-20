package com.asx.assignment.ums.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_detail")
public class User {
    @Id
    @GeneratedValue
    private long id;
    private String title;
    private String firstName;
    private String lastName;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    public User copyFrom(User other) {
        this.setAddress(this.getAddress().copyFrom(other.getAddress()));
        this.setGender(other.getGender());
        this.setTitle(other.getTitle());
        this.setFirstName(other.getFirstName());
        this.setLastName(other.getLastName());
        return this;
    }
}
