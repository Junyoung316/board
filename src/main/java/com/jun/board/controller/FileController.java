package com.jun.board.controller;

import com.jun.board.domain.Files;
import com.jun.board.service.FileService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;

    // sp-crud

    @GetMapping()
    public ResponseEntity<?> getAll() {
        try {
            List<Files> list = fileService.list();
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
             return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable("id") String id) {
        try {
            Files file = fileService.selectById(id);
            return new ResponseEntity<>(file, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE) // Multipart form data 전용
    public ResponseEntity<?> createForm(@RequestBody Files file) {
        try {
            boolean result = fileService.upload(file);
            if (result) return new ResponseEntity<>(file, HttpStatus.CREATED);
            else return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE) // json 전용
    public ResponseEntity<?> createJson(@RequestBody Files file) {
        try {
            boolean result = fileService.upload(file);
            if (result) return new ResponseEntity<>(file, HttpStatus.CREATED);
            else return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping()
    public ResponseEntity<?> update(@RequestBody Files file) {
        try {
            boolean result = fileService.updateById(file);
            if (result) return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
            else return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> destroy(@PathVariable("id") String id) {
        try {
            boolean result = fileService.deleteById(id);
            if (result) return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
            else return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 파일 선택 삭제
    @DeleteMapping("")
    public ResponseEntity<?> deleteFiles(
            @RequestParam(value = "noList", required = false) List<Long> noList,
            @RequestParam(value = "idList", required = false) List<String> idList
    ) {
        log.info("noList[]: {}", noList);
        log.info("idList[]: {}", idList);
        int result = 0;
        if (noList != null) result = fileService.deleteFileList(noList);
        if (idList != null) result = fileService.deleteFileListById(idList);
        if (result > 0) return new ResponseEntity<>(HttpStatus.OK);

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // 파일 다운로드
    @GetMapping("/download/{id}")
    public void fileDownload(@PathVariable("id") String id, HttpServletResponse response) throws Exception {
        fileService.download(id, response);
    }

    // 썸네일 이미지
    @GetMapping("/img/{id}")
    public void thumbnailImg(
            @PathVariable("id") String id,
            HttpServletResponse response
    ) throws Exception {
        boolean result = fileService.thumbnail(id, response);
        if (result) log.info("썸네일 응답 완료");
        else log.info("썸네일 응답 실패");
    }
}
