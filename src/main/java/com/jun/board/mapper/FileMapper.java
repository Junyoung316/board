package com.jun.board.mapper;

import com.jun.board.domain.Files;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FileMapper extends BaseMapper<Files> {

    // 부모 기준 목록
    List<Files> listByParent(Files file);
    // 부모 기준 삭제
    int deleteByParent(Files file);

    // 선택 삭제 - no
    int deleteFileList(@Param("noList") List<Long> noList);
    int deleteFiles(String noList);

    // 선택 삭제 - id
    int deleteFileListById(@Param("idList") List<String> idList);
    int deleteFilesByIds(String idList);

    // 타입별 파일 조회
    Files selectByType(Files file);
    // 타입별 파일 목록
    List<Files> listByType(Files file);
}
