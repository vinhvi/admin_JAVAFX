package com.techsavvy.admin.entity;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product implements Serializable {
    private String id;
    private Type type;
    private String name;
    private String origin;
    private float price;
    private float priceImport;
    private int counts;
    private String describes;
    private boolean status;
    private List<Image> images;
    private String lo;
    private int countSale;
}
