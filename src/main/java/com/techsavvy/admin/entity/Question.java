package com.techsavvy.admin.entity;

import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Question {
    private int id;
    private String content;
    private Date questionDate;
    private boolean reply;
    private Product product;
    private Account account;
}
