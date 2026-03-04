package com.jun.board.controller;

import com.jun.board.domain.Boards;
import com.jun.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping()
    public ResponseEntity<?> getAll() {
        try {
            List<Boards> list = boardService.list();
            return new ResponseEntity<>(list,  HttpStatus.OK);
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

}
