-- 페이지 별로 회원정보들을 출력(조회)

-- 1) 페이지 당 출력 레코드 수 :  
-- 아래의 SQL의 "FLOOR((ROWNUM - 1) / 10 + 1)"  구문에서 "10"이 페이지당 10개의 레코드를 의미함.
-- 2) page는 위의 표현의 별칭(alias)로써 실제 페이지 번호를 의미
-- 3) rownum 은 오라클의 의사 컬럼(pseudo column)으로써, 레코드의 번호(순서 번호)를 의미
--    전체적으로는 인라인 뷰(view)로 구성되어 있으며, 페이징과 관련된 구문에서는 공식처럼 응용해서 사용할 것.

SELECT *  
  FROM (SELECT m.*,  
               FLOOR((ROWNUM - 1) / 10 + 1) PAGE 
      	  FROM (
             	SELECT *
			 	  FROM MEMBER_TBL
           	   ) m  
       )  
WHERE PAGE = 1;

-- 전체 회원 조회
SELECT * FROM MEMBER_TBL;

-- n번째 회원 조회

-- 첫번째 회원 조회
SELECT *  
  FROM (SELECT m.*, rownum rowNo
		  FROM (
	             SELECT *
	    		   FROM member_tbl
	           ) m  
       )      
WHERE rowNo = 1;    
		
-- 마지막(100번째) 회원 조회
SELECT *  
  FROM (SELECT m.*, rownum rowNo
		  FROM (
	             SELECT *
	    		   FROM member_tbl
	           ) m  
       )      
WHERE rowNo = 100;