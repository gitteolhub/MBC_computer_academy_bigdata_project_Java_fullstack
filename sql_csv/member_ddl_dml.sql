CREATE TABLE member_tbl (
id varchar2(20) PRIMARY KEY,
pw varchar2(20) NOT NULL,
name varchar2(100) NOT NULL,
gender CHAR NOT NULL,
email varchar2(50) NOT NULL UNIQUE,
mobile varchar2(13) NOT NULL UNIQUE,
phone varchar2(13) NOT NULL,
zip CHAR(5),
road_address nvarchar2(100),
jibun_address nvarchar2(100),
detail_address nvarchar2(50),
birthday DATE,
joindate DATE DEFAULT current_date
);
 
comment ON COLUMN member_tbl.id IS '아이디';
comment ON COLUMN member_tbl.pw IS '패쓰워드';
comment ON COLUMN member_tbl.name IS '이름';
comment ON COLUMN member_tbl.gender IS '성별';
comment ON COLUMN member_tbl.email IS '이메일';
comment ON COLUMN member_tbl.mobile IS '연락처1(휴대폰)';
comment ON COLUMN member_tbl.phone IS '연락처2(일반전화)';
comment ON COLUMN member_tbl.zip IS '우편번호';
comment ON COLUMN member_tbl.road_address IS '도로명 주소';
comment ON COLUMN member_tbl.jibun_address IS '지번 주소';
comment ON COLUMN member_tbl.detail_address IS '상세 주소';
comment ON COLUMN member_tbl.birthday IS '생년월일';
comment ON COLUMN member_tbl.joindate IS '가입일';
 
DROP TABLE member_tbl;

----------------------------------------------------------

CREATE TABLE member_tbl (
id varchar2(20) CONSTRAINT member_tbl_id_pk PRIMARY KEY,
pw varchar2(20) CONSTRAINT member_tbl_pw_nn NOT NULL,
name varchar2(100) CONSTRAINT member_tbl_name_nn NOT NULL,
gender CHAR CONSTRAINT member_tbl_gender_nn NOT NULL,
email varchar2(50) CONSTRAINT member_tbl_email_nn NOT NULL,
mobile varchar2(13) CONSTRAINT member_tbl_mobile_nn NOT NULL,
phone varchar2(13) CONSTRAINT member_tbl_phone_nn NOT NULL,
zip CHAR(5),
road_address nvarchar2(100),
jibun_address nvarchar2(100),
detail_address nvarchar2(50),
birthday DATE,
joindate DATE DEFAULT current_date,
CONSTRAINT member_tbl_email_u UNIQUE(email),
CONSTRAINT member_tbl_mobile_u UNIQUE(mobile)
);

comment ON COLUMN member_tbl.id IS '아이디';
comment ON COLUMN member_tbl.pw IS '패쓰워드';
comment ON COLUMN member_tbl.name IS '이름';
comment ON COLUMN member_tbl.gender IS '성별';
comment ON COLUMN member_tbl.email IS '이메일';
comment ON COLUMN member_tbl.mobile IS '연락처1(휴대폰)';
comment ON COLUMN member_tbl.phone IS '연락처2(일반전화)';
comment ON COLUMN member_tbl.zip IS '우편번호';
comment ON COLUMN member_tbl.road_address IS '도로명 주소';
comment ON COLUMN member_tbl.jibun_address IS '지번 주소';
comment ON COLUMN member_tbl.detail_address IS '상세 주소';
comment ON COLUMN member_tbl.birthday IS '생년월일';
comment ON COLUMN member_tbl.joindate IS '가입일';
 
DROP TABLE member_tbl;

------------------------------------------------------------------

CREATE TABLE member_tbl (
id varchar2(20),
pw varchar2(20) CONSTRAINT member_tbl_pw_nn NOT NULL,
name varchar2(100) CONSTRAINT member_tbl_name_nn NOT NULL,
gender CHAR CONSTRAINT member_tbl_gender_nn NOT NULL,
email varchar2(50) CONSTRAINT member_tbl_email_nn NOT NULL,
mobile varchar2(13) CONSTRAINT member_tbl_mobile_nn NOT NULL,
phone varchar2(13) CONSTRAINT member_tbl_phone_nn NOT NULL,
zip CHAR(5),
road_address nvarchar2(100),
jibun_address nvarchar2(100),
detail_address nvarchar2(50),
birthday DATE,
joindate DATE DEFAULT current_date,
CONSTRAINT member_tbl_id_pk PRIMARY KEY(id),
CONSTRAINT member_tbl_email_u UNIQUE(email),
CONSTRAINT member_tbl_mobile_u UNIQUE(mobile)
);
 
comment ON COLUMN member_tbl.id IS '아이디';
comment ON COLUMN member_tbl.pw IS '패쓰워드';
comment ON COLUMN member_tbl.name IS '이름';
comment ON COLUMN member_tbl.gender IS '성별';
comment ON COLUMN member_tbl.email IS '이메일';
comment ON COLUMN member_tbl.mobile IS '연락처1(휴대폰)';
comment ON COLUMN member_tbl.phone IS '연락처2(일반전화)';
comment ON COLUMN member_tbl.zip IS '우편번호';
comment ON COLUMN member_tbl.road_address IS '도로명 주소';
comment ON COLUMN member_tbl.jibun_address IS '지번 주소';
comment ON COLUMN member_tbl.detail_address IS '상세 주소';
comment ON COLUMN member_tbl.birthday IS '생년월일';
comment ON COLUMN member_tbl.joindate IS '가입일';

DROP TABLE member_tbl;

-------------------------------------------------------------

CREATE TABLE member_tbl (
id varchar(20),
pw nvarchar2(20),
name nvarchar2(100),
gender CHAR,
email nvarchar2(50),
mobile nvarchar2(13),
phone nvarchar2(13),
zip CHAR(5),
road_address nvarchar2(100),
jibun_address nvarchar2(100),
detail_address nvarchar2(50),
birthday DATE,
joindate DATE DEFAULT current_date
);
  
ALTER TABLE member_tbl
ADD CONSTRAINT member_tbl_id_pk PRIMARY KEY(id);

ALTER TABLE member_tbl
ADD CONSTRAINT member_tbl_email_u UNIQUE(email);
 
ALTER TABLE member_tbl
ADD CONSTRAINT member_tbl_mobile_u UNIQUE(mobile);
 
ALTER TABLE member_tbl
MODIFY (pw CONSTRAINT member_tbl_pw_nn NOT NULL);
 
ALTER TABLE member_tbl
MODIFY (name CONSTRAINT member_tbl_name_nn NOT NULL);
 
ALTER TABLE member_tbl
MODIFY (gender CONSTRAINT member_tbl_gender_nn NOT NULL);

ALTER TABLE member_tbl
ADD CONSTRAINT member_tbl_gender_ck CHECK ( gender IN ( 'm', 'f' ) );
 
ALTER TABLE member_tbl
MODIFY (email CONSTRAINT member_tbl_email_nn NOT NULL);
 
ALTER TABLE member_tbl
MODIFY (mobile CONSTRAINT member_tbl_mobile_nn NOT NULL);
 
ALTER TABLE member_tbl
MODIFY (phone  CONSTRAINT member_tbl_phone_nn NOT NULL);

-- 참고 제약조건(constraint) 삭제 예시

ALTER TABLE member_tbl DROP CONSTRAINT MEMBER_TBL_EMAIL_U;

ALTER TABLE member_tbl DROP CONSTRAINT MEMBER_TBL_MOBILE_U;

ALTER TABLE member_tbl DROP CONSTRAINT MEMBER_TBL_ID_PK; 

DROP TABLE member_tbl;


-------------------------------------------------------------

/* 참고) 성별 제약조건은 아래와 같이 작성할 수도 있습니다. */
ALTER TABLE member_tbl ADD constraint member_tbl_gender_ck check (gender = 'm' OR gender = 'f');

--------------------------------------------------------------

-------------------------------------------------------------------------------

SELECT TO_DATE('July 8, 2024, 2:10 P.M.',
               'Month dd, YYYY, HH:MI P.M.',
               'NLS_DATE_LANGUAGE = American')
FROM DUAL;

-------------------------------------------------------------------------------

INSERT INTO member_tbl VALUES
('abcd1111',
 '#Abcd1234',
 '홍길동',
 'm',
 'abcd1111@abcd.com',
 '010-1111-3333',
 '02-1111-2222',
 '08290',
 '서울특별시 관악구 신림로 340',
 '서울특별시 관악구 신림동 1422-5 르네상스 복합쇼핑몰',
 '6층 MBC 아카데미',
 '2000-01-01',
  sysdate);
  
  
INSERT INTO member_tbl VALUES
('abcd2222',
 '#Abcd1234',
 '류관순',
 'f',
 'abcd2222@efgh.com',
 '010-2222-7777',
 '02-1111-2222',
 '08290',
 '서울특별시 관악구 신림로 340',
 '서울특별시 관악구 신림동 1422-5 르네상스 복합쇼핑몰',
 '6층 MBC 컴퓨터 아카데미',
 '2000-01-01',
 TO_DATE('August 4, 2023, 2:00 P.M.',
        'Month dd, YYYY, HH:MI P.M.',
        'NLS_DATE_LANGUAGE = American'));

 UPDATE member_tbl SET
 pw='#Abcd1234',
 email='java@djkangnam.com',
 zip='08290',
 jibun_address='서울특별시 관악구 신림동 1433-120번지',
 detail_address='MBC 아카데미 신림점 별관 8층'
 WHERE id='abcd1111';
 
 SELECT * FROM member_tbl;
 
 -- 개별 회원정보 조회
 SELECT * FROM member_tbl
 WHERE id='abcd1111';
 
 DELETE member_tbl;
 
 DELETE member_tbl
 WHERE id='abcd1111';

