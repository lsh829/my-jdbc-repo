-- 20200402
-----------== ȸ�� ���ν��� �ۼ� ==-----------
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


EXEC insertMember3('ccc',455,'������','20200401','tjsghk','030');
SELECT * FROM member1;
SELECT * FROM member2;


            
-- member1 ����(�߰�) ���ν��� �ۼ� 
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
    -- update�� delete �ϰ�� �ٸ� ����ó���� ������Ѵ�.
    COMMIT;
END;
/

EXEC insertMember1('ccc',455,'������');
SELECT * FROM member1;

-- member2 ����(�߰�) ���ν��� �ۼ� 
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
    -- update�� delete �ϰ�� �ٸ� ����ó���� ������Ѵ�.
    COMMIT;
END;
/

EXEC insertMember2('ccc','20200401','tjsghk','010');
SELECT * FROM member2;

--------------------------------
-- ���� ���ν��� 
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
        -- NOTFOUND�� COMMIT ���� �ؾ��ϸ�, 
        -- ���� �ڵ�� '20000' ��� ������� �Ѵ�. 
        RAISE_APPLICATION_ERROR(-20100, '��ϵ� �ڷᰡ �ƴմϴ�.');
    END IF;
    
    COMMIT;-- ���ν����� Ŀ���� �ʼ�.
END;
/

-- ���� ���ν��� Ȯ�� 
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
        -- NOTFOUND�� COMMIT ���� �ؾ��ϸ�, 
        -- ���� �ڵ�� '20000' ��� ������� �Ѵ�. 
        RAISE_APPLICATION_ERROR(-20100, '��ϵ� �ڷᰡ �ƴմϴ�.');
    END IF;
    
    COMMIT;-- ���ν����� Ŀ���� �ʼ�.
END;
/

-- ���� ���ν��� Ȯ�� 
EXEC updateMember2('ccc','20200401', 'remi', '033');
SELECT * FROM member2;

-------------------------------------
-- ���� ���ν��� �ۼ�
CREATE OR REPLACE PROCEDURE deleteMember2
(
    pId IN member2.id%TYPE
    
)
IS
BEGIN
    DELETE FROM member2 WHERE id=pId;
    -- ���� ���� ó������ ���, ���� ó�� 
    IF SQL%NOTFOUND THEN
        -- NOTFOUND�� COMMIT ���� �ؾ��ϸ�, 
        -- ���� �ڵ�� '20000' ��� ������� �Ѵ�. 
        RAISE_APPLICATION_ERROR(-20100, '��ϵ� �ڷᰡ �ƴմϴ�.');
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
    -- ���� ���� ó������ ���, ���� ó�� 
    IF SQL%NOTFOUND THEN
        -- NOTFOUND�� COMMIT ���� �ؾ��ϸ�, 
        -- ���� �ڵ�� '20000' ��� ������� �Ѵ�. 
        RAISE_APPLICATION_ERROR(-20100, '��ϵ� �ڷᰡ �ƴմϴ�.');
    END IF;
    COMMIT;
END;
/

EXEC deleteMember1('ccc');
SELECT * FROM member1;


-- ��ü ����Ʈ ���ν��� 
-- SELECT �����ؼ� ¥���Ѵ�. �ֳ��ϸ� �׽��� �Ұ��̱� �����̴�. 

CREATE OR REPLACE PROCEDURE listMember
(
    pResult OUT SYS_REFCURSOR --pResult �� ����ִ� ��.
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
-- �̸� �˻� ���ν��� 
CREATE OR REPLACE PROCEDURE findByNameMember
(
    pResult OUT SYS_REFCURSOR --pResult �� ����ִ� ��.
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
-- ���̵� �˻� ���ν��� 
CREATE OR REPLACE PROCEDURE findByIdMember
(
    pResult OUT SYS_REFCURSOR --pResult �� ����ִ� ��.
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