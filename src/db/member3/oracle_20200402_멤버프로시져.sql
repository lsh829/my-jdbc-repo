-- 20200402
-----------== 회원 프로시져 작성 ==-----------
desc member1;
desc member2;


            
CREATE OR REPLACE PROCEDURE insertMember3
(
    pId IN member1.id%TYPE
    ,pPwd IN member1.pwd%TYPE
    ,pName IN member1.name%TYPE
    ,pBirth IN member2.birth%TYPE
    ,pEmail IN member2.email%TYPE
    ,pTel IN member2.tel%TYPE
    
)
IS
BEGIN

    INSERT ALL 
	INTO member1(id,pwd,name) VALUES(pId, pPwd, pName)
	INTO member2(id,birth,email,tel) VALUES(pId ,pBirth, pEmail, pTel)
	SELECT * FROM dual;
    
    COMMIT;
END;
/  


EXEC insertMember3('ccc',455,'도레미','20200401','tjsghk','030');
SELECT * FROM member1;
SELECT * FROM member2;


            
-- member1 저장(추가) 프로시져 작성 
CREATE OR REPLACE PROCEDURE insertMember1
(
    pId IN member1.id%TYPE
    ,pPwd IN member1.pwd%TYPE
    ,pName IN member1.name%TYPE
    
)
IS
BEGIN
    INSERT INTO member1(id, pwd, name)
    VALUES ( pId, pPwd, pName);
    -- update와 delete 하고는 다른 예외처리를 해줘야한다.
    COMMIT;
END;
/

EXEC insertMember1('ccc',455,'도레미');
SELECT * FROM member1;

-- member2 저장(추가) 프로시져 작성 
CREATE OR REPLACE PROCEDURE insertMember2
(
    pId IN member2.id%TYPE
    ,pBirth IN member2.birth%TYPE
    ,pEmail IN member2.email%TYPE
    ,pTel IN member2.tel%TYPE
)
IS
BEGIN
    INSERT INTO member2(id, birth, email, tel)
    VALUES ( pId, pBirth, pEmail, pTel);
    -- update와 delete 하고는 다른 예외처리를 해줘야한다.
    COMMIT;
END;
/

EXEC insertMember2('ccc','20200401','tjsghk','010');
SELECT * FROM member2;

--------------------------------
-- 수정 프로시져 
CREATE OR REPLACE PROCEDURE updateMember1
(
   pId IN member1.id%TYPE
   ,pPwd IN member1.pwd%TYPE
)
IS
BEGIN
    UPDATE member1 
    SET pwd = pPwd
    WHERE id = pId;
    
    IF SQL%NOTFOUND THEN
        -- NOTFOUND는 COMMIT 전에 해야하며, 
        -- 에러 코드는 '20000' 대로 정해줘야 한다. 
        RAISE_APPLICATION_ERROR(-20100, '등록된 자료가 아닙니다.');
    END IF;
    
    COMMIT;-- 프로시져는 커밋이 필수.
END;
/

-- 수정 프로시져 확인 
EXEC updateMember1('ccc','5678');
SELECT * FROM member1;


-- ==========================================
CREATE OR REPLACE PROCEDURE updateMember2
(
   pId IN member2.id%TYPE
    ,pBirth IN member2.birth%TYPE
    ,pEmail IN member2.email%TYPE
    ,pTel IN member2.tel%TYPE
)
IS
BEGIN
    UPDATE member2 
    SET birth = pBirth, email = pEmail, tel = pTel
    WHERE id = pId;
    
    IF SQL%NOTFOUND THEN
        -- NOTFOUND는 COMMIT 전에 해야하며, 
        -- 에러 코드는 '20000' 대로 정해줘야 한다. 
        RAISE_APPLICATION_ERROR(-20100, '등록된 자료가 아닙니다.');
    END IF;
    
    COMMIT;-- 프로시져는 커밋이 필수.
END;
/

-- 수정 프로시져 확인 
EXEC updateMember2('ccc','20200401', 'remi', '033');
SELECT * FROM member2;

-------------------------------------
-- 삭제 프로시져 작성
CREATE OR REPLACE PROCEDURE deleteMember2
(
    pId IN member2.id%TYPE
    
)
IS
BEGIN
    DELETE FROM member2 WHERE id=pId;
    -- 없는 것을 처리했을 경우, 예외 처리 
    IF SQL%NOTFOUND THEN
        -- NOTFOUND는 COMMIT 전에 해야하며, 
        -- 에러 코드는 '20000' 대로 정해줘야 한다. 
        RAISE_APPLICATION_ERROR(-20100, '등록된 자료가 아닙니다.');
    END IF;
    COMMIT;
END;
/

EXEC deleteMember2('ccc');
SELECT * FROM member2;




CREATE OR REPLACE PROCEDURE deleteMember1
(
    pId IN member1.id%TYPE
    
)
IS
BEGIN
    DELETE FROM member1 WHERE id=pId;
    -- 없는 것을 처리했을 경우, 예외 처리 
    IF SQL%NOTFOUND THEN
        -- NOTFOUND는 COMMIT 전에 해야하며, 
        -- 에러 코드는 '20000' 대로 정해줘야 한다. 
        RAISE_APPLICATION_ERROR(-20100, '등록된 자료가 아닙니다.');
    END IF;
    COMMIT;
END;
/

EXEC deleteMember1('ccc');
SELECT * FROM member1;


-- 전체 리스트 프로시져 
-- SELECT 주의해서 짜야한다. 왜냐하면 테스팅 불가이기 때문이다. 

CREATE OR REPLACE PROCEDURE listMember
(
    pResult OUT SYS_REFCURSOR --pResult 에 담아주는 것.
)
IS
BEGIN
    OPEN pResult FOR
        SELECT m1.id, pwd, name
                ,TO_CHAR(birth, 'YYYY-MM-DD') birth, email, tel 
		FROM member1 m1
		LEFT OUTER JOIN member2 m2 ON m1.id = m2.id;
END;
/
--------------------------------------------------------
-- 이름 검색 프로시져 
CREATE OR REPLACE PROCEDURE findByNameMember
(
    pResult OUT SYS_REFCURSOR --pResult 에 담아주는 것.
    , pName IN VARCHAR2
)
IS
BEGIN
    OPEN pResult FOR
         SELECT m1.id, pwd, name
			    ,TO_CHAR(birth, 'YYYY-MM-DD') birth, email, tel
         FROM member1 m1 
		 LEFT OUTER JOIN member2 m2 ON m1.id = m2.id
	     WHERE INSTR(name, pName )>=1;
END;
/

--------------------------------------------------
-- 아이디 검색 프로시져 
CREATE OR REPLACE PROCEDURE findByIdMember
(
    pResult OUT SYS_REFCURSOR --pResult 에 담아주는 것.
    , pId IN VARCHAR2
)
IS
BEGIN
    OPEN pResult FOR
         SELECT m1.id, pwd, name
		        ,TO_CHAR(birth, 'YYYY-MM-DD') birth, email, tel 
		 FROM member1 m1
		 LEFT OUTER JOIN member2 m2 ON m1.id = m2.id
         WHERE m1.id= pId;
END;
/