package com.techsavvy.admin.entity;
import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Evaluate {
    private int id;
    private String content;
    private int value;
    private Date evaluationDate;
    private Product product;
    private Account account;
}
