package com.asx.assignment.ums.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String street;
    private String city;
    @Enumerated(EnumType.STRING)
    private State state;
    private int postCode;
    @OneToOne(mappedBy = "address")
    private User user;
}
