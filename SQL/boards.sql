DROP TABLE if exists boards;

create table boards (
  no bigint not null auto_increment primary key comment 'PK',
  id varchar(64) not null comment 'UK',
  title varchar(100) not null comment '제목',
  writer varchar(100) not null comment '작성자',
  content TEXT null comment '내용',
  created_at timestamp not null default CURRENT_TIMESTAMP comment '등록일자',
  updated_at timestamp not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '수정일자'
) comment '게시판';

-- 샘플 데이터

insert into boards (id, title, writer, content)
select
    uuid(),
    concat('게시판 샘플 데이터 ', t.num),
    concat('작성자 ', t.num),
    concat('내용 샘플 데이터 ', t.num)
from (
    select @row := @row + 1 as num
    from information_schema.tables, (select @row := 0) r
    limit 100
     ) t;