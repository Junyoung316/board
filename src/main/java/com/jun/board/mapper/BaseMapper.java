package com.jun.board.mapper;

import java.util.List;

public interface BaseMapper<E> {
    List<E> list();
    E select(Long no);
    E selectById(String id);
    int insert(E boards);
    int update(E boards);
    int updateById(E boards);
    int delete(Long no);
    int deleteById(String id);

}
