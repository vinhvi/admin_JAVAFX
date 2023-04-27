package com.techsavvy.admin.entity;
import lombok.*;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Image {
    private String id;
    private String name;
    private String imageUrl;
    private Product product;
}
