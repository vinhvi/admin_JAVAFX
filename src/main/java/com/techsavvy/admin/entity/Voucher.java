package com.techsavvy.admin.entity;

import lombok.*;

import java.util.Date;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Voucher {
    private String id;
    private String name;
    private int scoreNeed;
    private float discount;
    private Date startDate;
    private Date endDate;
    private String content;
}
