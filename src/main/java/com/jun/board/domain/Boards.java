package com.jun.board.domain;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
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

    // 파일
    private MultipartFile mainFile;
    private List<MultipartFile> files;

    // 파일 정보
    private Files file; // 메일 파일 1:1
    private List<Files> fileList; // 첨부 파일 1:N


    public Boards() {
        this.id = UUID.randomUUID().toString();
    }
}
