package com.techsavvy.admin.entity;

import lombok.*;

import java.io.Serializable;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Address  implements Serializable {
    private int id;
    private String city;
    private String district;
    private String wards;
    private String street;
}
