DROP TABLE IF EXISTS files;

CREATE TABLE files (
    no bigint not null auto_increment primary key, -- 파일 번호
    id varchar(64) not null, -- UK
    p_id varchar(64) not null, -- 부모 ID (UID)
    file_name text not null, -- 저장된 파일 명
    origin_name text, -- 원본 파일 명
    file_path text not null, -- 파일 경로
    file_size bigint not null default '0', -- 파일 크기
    created_at timestamp not null default current_timestamp, -- 등록 일시
    updated_at timestamp not null default current_timestamp, -- 수정 일시
    type ENUM('MAIN','SUB') not null default 'SUB', -- 타입
    seq bigint null default 0 -- 순서
) comment="파일";