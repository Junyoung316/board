package com.jun.board.domain;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class Boards {
    private Long no;
    private String id;
    private String title;
    private String writer;
    private String content;
    private Date createdAt;
    private Date updatedAt;

    public Boards() {
        this.id = UUID.randomUUID().toString();
    }
}
