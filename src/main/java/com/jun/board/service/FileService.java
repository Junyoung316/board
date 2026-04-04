package com.jun.board.service;

import com.jun.board.domain.Files;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface FileService extends BaseService<Files> {

    // 파일 업로드
    boolean upload(Files file) throws Exception;
    int upload(List<Files> fileList) throws Exception;

    // 파일 다운로드
    boolean download(String id, HttpServletResponse response) throws Exception;

    // 썬네일
    boolean thumbnail(String id, HttpServletResponse response) throws Exception;

    // 부모 기준 목록
    List<Files> listByParent(Files file);
    // 부모 기준 삭제
    int deleteByParent(Files file);

    // 선택 삭제(String) - no
    int deleteFiles(String noList);
    int deleteFileList(List<Long> noList);
    // 선택 삭제(String) - id
    int deleteFilesById(String idList);
    int deleteFileListById(List<String> idList);

    // 타입별 파일 조회
    Files selectByType(Files file);
    // 타입별 파일 목록
    List<Files> listByType(Files file);
}
