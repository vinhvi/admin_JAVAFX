package com.techsavvy.admin.entity;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShippingCompany {

    private int id;
    private String name;
    private String email;
    private String phone;

    private Address address;
}
