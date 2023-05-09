package com.techsavvy.admin.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

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
    private Date dateImport;
    private int counts;
    private String describes;
    private boolean status;
    private String lo;


}
