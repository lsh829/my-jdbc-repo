-- 2020-4-2 

-- 저장(추가) 프로시져 작성 
CREATE OR REPLACE PROCEDURE insertScore 
(
    pHak IN score.hak%TYPE
    ,pName IN score.name%TYPE
    ,pBirth IN score.birth%TYPE
    ,pKor IN score.kor%TYPE
    ,pEng IN score.eng%TYPE
    ,pMat IN score.mat%TYPE
)
IS
BEGIN
    INSERT INTO score(hak, name, birth, kor, eng, mat)
    VALUES ( pHak, pName, pBirth, pKor, pEng, pMat);
    -- update와 delete 하고는 다른 예외처리를 해줘야한다.
    COMMIT;
END;
/

-- 프로시져 확인 
EXEC insertScore('9999','나나나','2000-10-10',80,90,70);
SELECT * FROM score;


--------------------------------
-- 수정 프로시져 
CREATE OR REPLACE PROCEDURE updateScore 
(
    pHak IN score.hak%TYPE
    ,pName IN score.name%TYPE
    ,pBirth IN score.birth%TYPE
    ,pKor IN score.kor%TYPE
    ,pEng IN score.eng%TYPE
    ,pMat IN score.mat%TYPE
)
IS
BEGIN
    UPDATE score 
    SET name = pName, birth = pBirth, kor = pKor,eng = pEng, mat = pMat
    WHERE hak = pHak;
    
    IF SQL%NOTFOUND THEN
        -- NOTFOUND는 COMMIT 전에 해야하며, 
        -- 에러 코드는 '20000' 대로 정해줘야 한다.
        RAISE_APPLICATION_ERROR(-20100, '등록된 자료가 아닙니다.');
    END IF;
    
    COMMIT;-- 프로시져는 커밋이 필수.
END;
/

-- 수정 프로시져 확인 
EXEC updateScore('9999','나나나','2001-11-11',70,60,70);
SELECT * FROM score;

-------------------------------------
-- 삭제 프로시져 작성
CREATE OR REPLACE PROCEDURE deleteScore 
(
    pHak IN score.hak%TYPE
    
)
IS
BEGIN
    DELETE FROM score WHERE hak=pHak;
   -- 없는 것을 처리했을 경우, 예외 처리 
    IF SQL%NOTFOUND THEN
         -- NOTFOUND는 COMMIT 전에 해야하며, 
         -- 에러 코드는 '20000' 대로 정해줘야 한다. 
        RAISE_APPLICATION_ERROR(-20100, '등록된 자료가 아닙니다.');
    END IF;
    COMMIT;
END;
/

EXEC deleteScore('9999');
SELECT * FROM score;

---------------------------------------
-- 전체 리스트 프로시져 
-- SELECT 주의해서 짜야한다. 왜냐하면 테스팅 불가이기 때문이다.  

CREATE OR REPLACE PROCEDURE listScore
(
    pResult OUT SYS_REFCURSOR --pResult 에 담아주는 것..
)
IS
BEGIN
    OPEN pResult FOR
         SELECT hak, name, birth, kor, eng, mat,
                (kor+eng+mat) tot, (kor+eng+mat)/3 ave,
                RANK() OVER(ORDER BY (kor+eng+mat) DESC) rank
         FROM score;
END;
/


--------------------------------------------------
-- 이름 검색 프로시져 
CREATE OR REPLACE PROCEDURE findByNameScore
(
    pResult OUT SYS_REFCURSOR --pResult 에 담아주는 것.
    , pName IN VARCHAR2
)
IS
BEGIN
    OPEN pResult FOR
         SELECT hak, name, birth, kor, eng, mat,
                (kor+eng+mat) tot, (kor+eng+mat)/3 ave
         FROM score
         WHERE INSTR(name, pName) >= 1;
END;
/


--------------------------------------------------
-- 학번 검색 프로시져 
CREATE OR REPLACE PROCEDURE findByHakScore
(
    pResult OUT SYS_REFCURSOR --pResult 에 담아주는 것.
    , pHak IN VARCHAR2
)
IS
BEGIN
    OPEN pResult FOR
         SELECT hak, name, birth, kor, eng, mat,
                (kor+eng+mat) tot, (kor+eng+mat)/3 ave
         FROM score
         WHERE hak = pHak;
END;
/


---------------------------------------------------------------
-- 과목별 전체 평균 프로시져 
CREATE OR REPLACE PROCEDURE averageScore
(
    pKor OUT NUMBER
    ,pEng OUT NUMBER
    ,pMat OUT NUMBER
)
IS
BEGIN
    SELECT NVL(AVG(kor),0), NVL(AVG(eng),0), NVL(AVG(mat),0)
           INTO pKor, pEng, pMat
    FROM score;
END;
/









