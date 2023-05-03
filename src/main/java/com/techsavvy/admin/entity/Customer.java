package com.techsavvy.admin.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Customer implements Serializable {

    private String id;

    private String firstName;

    private String lastName;

    private String email;

    private Date dateOfBirth;

    private int sex;

    private String phone;

    private Address address;

    private String typeCustomer;

    private int score;

    private Account account;
}
