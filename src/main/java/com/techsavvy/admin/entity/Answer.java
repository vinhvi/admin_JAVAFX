package com.techsavvy.admin.entity;

import lombok.*;

import java.util.Date;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Answer {
    private int id;
    private String content;
    private Date replyDate;
    private Question question;
    private Account account;
}
