-- Study Service 데이터베이스 생성
CREATE DATABASE IF NOT EXISTS studydb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 테스트 데이터베이스 생성
CREATE DATABASE IF NOT EXISTS studydb_test CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 권한 설정
GRANT ALL PRIVILEGES ON studydb.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON studydb_test.* TO 'root'@'%';
FLUSH PRIVILEGES;