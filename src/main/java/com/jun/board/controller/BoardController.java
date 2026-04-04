package com.jun.board.controller;

import com.github.pagehelper.PageInfo;
import com.jun.board.domain.Boards;
import com.jun.board.domain.Files;
import com.jun.board.domain.Pagination;
import com.jun.board.service.BoardService;
import com.jun.board.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// XSS 공격 지점: 필터를 사용해 자바스크립트 코드를 문자로 치환
// MyBatis #{} 문법: SQL Injection 방어에는 효과적, XSS는 방어 X

@Slf4j
//@CrossOrigin(origins = "http://localhost:5173") // 특정 url이나 포트만 허용
@CrossOrigin("*") // 전체 요청 허용 (학습용)
@RequiredArgsConstructor // 의존성 자동 주입
@RestController
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;
    private final FileService fileService;

    // 요청 예시: /boards?page=1&size=10
    @GetMapping()
    public ResponseEntity<?> getAll(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            Pagination pagination
    ) {
        try {
            // 페이징 처리 후 데이터 전송
            PageInfo<Boards> pageInfo = boardService.page(page, size);
            pagination.setPage(page);
            pagination.setSize(size);
            pagination.setTotal(pageInfo.getTotal());
            Map<String, Object> response = new HashMap<>();
            response.put("list", pageInfo.getList());
            response.put("pagination", pagination);

            return new ResponseEntity<>(response,  HttpStatus.OK);

            // 페이징 처리x 전체 데이터 전송
//            List<Boards> list = boardService.list();
//            return new ResponseEntity<>(list,  HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable("id") String id) {
        try {
            Boards board = boardService.selectById(id);
            return new ResponseEntity<>(board,  HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping()
    public ResponseEntity<?> create(@RequestBody Boards board) {
        try {
            boolean result = boardService.insert(board);
            Boards saveBoard = boardService.selectById(board.getId());
            if (result) {
                return new ResponseEntity<>(saveBoard, HttpStatus.CREATED); // 201
            } else {
                return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST); // 400
            }
        }  catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500
        }
    }

    @PutMapping()
    public ResponseEntity<?> update(@RequestBody Boards board) {
        try {
            boolean result = boardService.updateById(board);
            if (result) {
                return new ResponseEntity<>("SUCCESS", HttpStatus.OK); // 200
            } else {
                return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST); // 400
            }
        }  catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> destroy(@PathVariable("id") String id) {
        try {
            boolean result = boardService.deleteById(id);
            if (result) {
                return new ResponseEntity<>("SUCCESS", HttpStatus.OK); // 200
            } else {
                return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST); // 400
            }
        }  catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500
        }
    }

    /**
     * 게시글 첨부 파일 목록 조회
     *
     * GET /boards/{id}/files
     *
     * @param id   게시글 ID
     * @param type 파일 타입 (null: 전체, "MAIN": 메인 1개, "SUB": 서브 목록)
     * @return type=null  -> List<Files> (0 ~ n)
     *         type=MAIN  -> Files (단일 객체)
     *         type=SUB   -> List<Files> (0 ~ n)
     */
    @GetMapping("/{id}/files")
    public ResponseEntity<?> boardFileList(
            @PathVariable("id") String id,
            @RequestParam(value = "type", required = false) String type
    ) {
        try {
            Files file = new Files();
            file.setPId(id);
            file.setType(type);

            // type이 없을 때 -> 부모 기준 모든 파일
            if (type == null) {
                List<Files> list = fileService.listByParent(file);
                return new ResponseEntity<>(list, HttpStatus.OK);
            }

            // type: "MAIN" -> 메인 파일 1개
            if (type.equals("MAIN")) {
                Files mainFile = fileService.selectByType(file);
                return new ResponseEntity<>(mainFile, HttpStatus.OK);
            } else { // type: "SUB", ? -> 타입별 파일 목록
                List<Files> list = fileService.listByType(file);
                return new ResponseEntity<>(list, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
