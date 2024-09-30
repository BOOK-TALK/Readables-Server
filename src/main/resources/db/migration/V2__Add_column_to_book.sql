-- bookname 컬럼 추가
SET @col_exists_bookname = (
    SELECT COUNT(*)
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_NAME = 'book' AND COLUMN_NAME = 'bookname'
);

SET @sql_bookname = IF(@col_exists_bookname = 0, 'ALTER TABLE book ADD COLUMN bookname VARCHAR(255);', 'SELECT "bookname column already exists";');
PREPARE stmt_bookname FROM @sql_bookname;
EXECUTE stmt_bookname;
DEALLOCATE PREPARE stmt_bookname;

-- book_imageurl 컬럼 추가
SET @col_exists_book_imageurl = (
    SELECT COUNT(*)
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_NAME = 'book' AND COLUMN_NAME = 'book_imageurl'
);

SET @sql_book_imageurl = IF(@col_exists_book_imageurl = 0, 'ALTER TABLE book ADD COLUMN book_imageurl VARCHAR(255);', 'SELECT "book_imageurl column already exists";');
PREPARE stmt_book_imageurl FROM @sql_book_imageurl;
EXECUTE stmt_book_imageurl;
DEALLOCATE PREPARE stmt_book_imageurl;


/* 특정 테이블 칼럼 삭제
 -- 1. 컬럼 존재 여부 확인
SELECT COUNT(*) INTO @col_exists
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_NAME = 'book' AND COLUMN_NAME = 'bookname';

-- 2. 컬럼 삭제
SET @sql = IF(@col_exists > 0, 'ALTER TABLE book DROP COLUMN bookname;', 'SELECT "Column does not exist";');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
 */
