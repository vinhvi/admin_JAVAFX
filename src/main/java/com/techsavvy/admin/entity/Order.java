package com.techsavvy.admin.entity;

import lombok.*;

import java.util.Date;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    private String id;
    private Employee employee;
    private Date bookingDate;
    private String notes;
    private boolean statusPayment;
    private String statusDelivery;
    private Customer customer;
    private Payments payments;
    private float totalMoney;
    private ShippingCompany shippingCompany;
    private Voucher voucher;
}
