package com.techsavvy.admin.entity;

import lombok.*;

import java.util.Date;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Discount {
    private String id;
    private String content;
    private String typeDiscount;
    private Date start;
    private Date end;
    private float discountForProduct;
    private boolean enable;
}
