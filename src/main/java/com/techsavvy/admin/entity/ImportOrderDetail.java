package com.techsavvy.admin.entity;

import lombok.*;

import java.io.Serializable;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImportOrderDetail implements Serializable {

    private int id;
    private ImportOrder importOrder;
    private Product product;
    private int count;
    private float price;

}
