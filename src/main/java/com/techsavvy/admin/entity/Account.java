package com.techsavvy.admin.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Account implements Serializable {
    private Integer id;
    private String email;
    private String passWordAccount;
    private boolean enable;
    private Set<Role> roles = new LinkedHashSet<>();
}
