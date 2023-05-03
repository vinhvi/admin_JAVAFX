package com.techsavvy.admin.entity;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail {
    private int id;
    private Order order;
    private Options options;
    private int quantity;
    private float unitPrice;
}
