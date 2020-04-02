-- 2020-4-2 

-- ����(�߰�) ���ν��� �ۼ� 
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
    -- update�� delete �ϰ�� �ٸ� ����ó���� ������Ѵ�.
    COMMIT;
END;
/

-- ���ν��� Ȯ�� 
EXEC insertScore('9999','������','2000-10-10',80,90,70);
SELECT * FROM score;


--------------------------------
-- ���� ���ν��� 
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
        -- NOTFOUND�� COMMIT ���� �ؾ��ϸ�, 
        -- ���� �ڵ�� '20000' ��� ������� �Ѵ�. 
        RAISE_APPLICATION_ERROR(-20100, '��ϵ� �ڷᰡ �ƴմϴ�.');
    END IF;
    
    COMMIT;-- ���ν����� Ŀ���� �ʼ�.
END;
/

-- ���� ���ν��� Ȯ�� 
EXEC updateScore('9999','����','2001-11-11',70,60,70);
SELECT * FROM score;

-------------------------------------
-- ���� ���ν��� �ۼ�
CREATE OR REPLACE PROCEDURE deleteScore 
(
    pHak IN score.hak%TYPE
    
)
IS
BEGIN
    DELETE FROM score WHERE hak=pHak;
    -- ���� ���� ó������ ���, ���� ó�� 
    IF SQL%NOTFOUND THEN
        -- NOTFOUND�� COMMIT ���� �ؾ��ϸ�, 
        -- ���� �ڵ�� '20000' ��� ������� �Ѵ�. 
        RAISE_APPLICATION_ERROR(-20100, '��ϵ� �ڷᰡ �ƴմϴ�.');
    END IF;
    COMMIT;
END;
/

EXEC deleteScore('9999');
SELECT * FROM score;

---------------------------------------
-- ��ü ����Ʈ ���ν��� 
-- SELECT �����ؼ� ¥���Ѵ�. �ֳ��ϸ� �׽��� �Ұ��̱� �����̴�. 

CREATE OR REPLACE PROCEDURE listScore
(
    pResult OUT SYS_REFCURSOR --pResult �� ����ִ� ��.
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
-- �̸� �˻� ���ν��� 
CREATE OR REPLACE PROCEDURE findByNameScore
(
    pResult OUT SYS_REFCURSOR --pResult �� ����ִ� ��.
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
-- �й� �˻� ���ν��� 
CREATE OR REPLACE PROCEDURE findByHakScore
(
    pResult OUT SYS_REFCURSOR --pResult �� ����ִ� ��.
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
-- ���� ��ü ��� ���ν��� 

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









