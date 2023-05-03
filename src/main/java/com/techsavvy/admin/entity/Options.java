package com.techsavvy.admin.entity;

import lombok.*;

import java.io.Serializable;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Options implements Serializable {
    private int id;
    private String nameOptions;
    private String color;
    private float price;
    private float priceImport;
    private int count;
    private Product product;
    private Discount discount;


}
