package com.techsavvy.admin.entity;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Payments {

    private int id;

    private String content;
    private boolean status;

}
