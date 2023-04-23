package com.techsavvy.admin.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImportOrder implements Serializable {
    private String id;
    private Supplier supplier;
    private Employee employee;
    private Date importDate;

}
