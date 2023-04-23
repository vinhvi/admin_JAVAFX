package com.techsavvy.admin.entity;

import java.io.Serializable;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Type implements Serializable {
    private int id;

    private String name;

    private boolean status;
}
