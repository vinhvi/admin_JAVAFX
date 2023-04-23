package com.techsavvy.admin.entity;
import lombok.*;

import java.io.Serializable;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Role implements Serializable {
    private int id;

    private String name;


}
