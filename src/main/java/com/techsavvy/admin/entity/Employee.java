package com.techsavvy.admin.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Employee implements Serializable {
    private String id;

    private String firstName;

    private String lastName;

    private String email;

    private Date dateOfBirth;

    private int sex;

    private String phone;

    private Address address;

    private Date importDate;

    private Account account;



}
