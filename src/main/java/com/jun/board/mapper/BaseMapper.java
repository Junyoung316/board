package com.jun.board.mapper;

import java.util.List;

public interface BaseMapper<E> {
    List<E> list();
    E select(int no);
    E selectById(String id);
    int insert(E boards);
    int update(E boards);
    int updateById(E boards);
    int delete(int no);
    int deleteById(String id);

}
