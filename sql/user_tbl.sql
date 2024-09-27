CREATE TABLE user_tbl (
	num varchar2(50)PRIMARY KEY,        id varchar2(20) NOT NULL,    pw varchar2(20)NOT NULL,  name varchar2(100) NOT NULL,       gender varchar2(10) NOT NULL, 
	email varchar2(50) NOT NULL UNIQUE, phone varchar2(13) NOT NULL, birthday DATE,            joindate DATE DEFAULT current_date
);

CREATE SEQUENCE USER_NUM_SEQ
    START WITH 1
    INCREMENT BY 1
    MAXVALUE 99999999
    NOCYCLE; 

comment ON COLUMN user_tbl.num            IS '회원번호';
comment ON COLUMN user_tbl.id             IS '아이디';
comment ON COLUMN user_tbl.pw             IS '비밀번호';
comment ON COLUMN user_tbl.name           IS '이름';
comment ON COLUMN user_tbl.gender         IS '성별';

comment ON COLUMN user_tbl.email          IS '이메일';
comment ON COLUMN user_tbl.phone          IS '연락처(휴대폰)';
comment ON COLUMN user_tbl.birthday       IS '생년월일';
comment ON COLUMN user_tbl.joindate       IS '가입일';

/* 참고) 성별 제약조건은 아래와 같이 작성할 수도 있습니다. */
ALTER TABLE user_tbl ADD constraint user_tbl_gender_ck check (gender = '남자' OR gender = '여자');

INSERT INTO user_tbl VALUES(
	'001',              'abcd1111',      '#Abcd1234', '홍길동',    '남자', 
	'abcd1111@abcd.com','010-1111-3333', '2000-01-01', sysdate);
  
  
INSERT INTO user_tbl VALUES(
	'002'               'abcd2222',      '#Abcd1234',  '류관순', 								   '여자', 
	'abcd2222@efgh.com','010-2222-7777', '2000-01-01', TO_DATE('August 4, 2023, 2:00 P.M.',
        					 	   		                       'Month dd, YYYY, HH:MI P.M.',
INSERT INTO user_tbl VALUES(
	USER_NUM_SEQ.NEXTVAL,              'cd1111',      'cd1234', '홍길동',    '남자', 
	'abcd0000@abcd.com','010-1111-0000', '2001-01-01', sysdate);        					 			                    

 UPDATE user_tbl SET
 pw   ='#Abcd1234',
 email='java@djkangnam.com',
 WHERE id='abcd1111';
 
 SELECT * FROM user_tbl;
 
 -- 개별 회원정보 조회(검색) --
 SELECT * FROM user_tbl
 WHERE id='abcd1111';
 
 DELETE user_tbl;
 
 DELETE user_tbl
 WHERE id='abcd1111';
 
 
/* 암호화를 위해 비밀번호 필드 자료현(자릿수) 변경 */
ALTER TABLE USER_TBL MODIFY PW VARCHAR2(60);

/* USER_TBL에 ENABLED열 추가, 기본값: 0 */
ALTER TABLE USER_TBL ADD ENABLED NUMBER(1) DEFAULT 0;

COMMENT ON COLUMN USER_TBL.ENABLED IS '회원정보 사용 여부';

/* 기존 데이터에 대한 ENABLES 필드의 사용가 변경(1)*/
UPDATE USER_TBL SET ENABLED = 1;

DROP TABLE user_tbl;

