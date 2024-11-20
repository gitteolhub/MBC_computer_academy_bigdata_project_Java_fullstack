/* ----------필수 테이블 생성 코드 시작------------ */
CREATE TABLE USER_TBL (
	NUM VARCHAR2(50) PRIMARY KEY,
    ID VARCHAR2(20) NOT NULL UNIQUE,
    PW VARCHAR2(60) NOT NULL,
    NAME VARCHAR2(100) NOT NULL,
    GENDER VARCHAR2(10) NOT NULL,
	EMAIL VARCHAR2(50) NOT NULL UNIQUE,
    PHONE VARCHAR2(13) NOT NULL,
    BIRTHDAY DATE,
    JOINDATE DATE DEFAULT current_date,
    ENABLED NUMBER(1) DEFAULT 0,
    FOODMENU VARCHAR2(500)
);

CREATE SEQUENCE USER_NUM_SEQ
    START WITH 1
    INCREMENT BY 1
    MAXVALUE 99999999
    NOCYCLE;

COMMENT ON COLUMN USER_TBL.num            IS '회원번호';
COMMENT ON COLUMN USER_TBL.id             IS '아이디';
COMMENT ON COLUMN USER_TBL.pw             IS '비밀번호';
COMMENT ON COLUMN USER_TBL.name           IS '이름';
COMMENT ON COLUMN USER_TBL.gender         IS '성별';
COMMENT ON COLUMN USER_TBL.email          IS '이메일';
COMMENT ON COLUMN USER_TBL.phone          IS '연락처(휴대폰)';
COMMENT ON COLUMN USER_TBL.birthday       IS '생년월일';
COMMENT ON COLUMN USER_TBL.joindate       IS '가입일';
COMMENT ON COLUMN USER_TBL.ENABLED 		  IS '회원정보 사용 여부';
COMMENT ON COLUMN USER_TBL.FOODMENU 	  IS '식단 메뉴';

/* 기존 데이터에 대한 ENABLES 필드의 사용가능 여부 변경(1)*/
UPDATE USER_TBL SET ENABLED = 1;

/* ----------필수 테이블 생성 코드 끝------------ */

/* 참고) 성별 제약조건은 아래와 같이 작성할 수도 있습니다. */
ALTER TABLE USER_TBL ADD constraint USER_TBL_GENDER_CK check (gender = '남자' OR gender = '여자');
