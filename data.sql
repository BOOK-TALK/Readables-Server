-- 장르 초기화 (한국십진분류법(KDC) 6판 기준)
USE booktalk;
CREATE TABLE IF NOT EXISTS genre (
     genre_id        bigint auto_increment
         primary key,
     kdc_num         varchar(255) null,
     name            varchar(255) null,
     parent_genre_id bigint       null,
     constraint FKroi0p36nixht1l7b3hadih34l
         foreign key (parent_genre_id) references genre (genre_id)
)engine=InnoDB DEFAULT CHARSET=uft8; #한글 인코딩

INSERT IGNORE INTO genre (parent_genre_id, kdc_num, name)
values
       -- Level 1
       (NULL, 0, '총류'),
       (NULL, 1, '철학'),
       (NULL, 2, '종교'),
       (NULL, 3, '사회과학'),
       (NULL, 4, '자연과학'),
       (NULL, 5, '기술 과학'),
       (NULL, 6, '예술'),
       (NULL, 7, '언어'),
       (NULL, 8, '문학'),
       (NULL, 9, '역사'),


       -- Level 2
       (1, 0, '총류'),
       (1, 1, '도서학, 서지학'),
       (1, 2, '문헌정보학'),
       (1, 3, '백과사전'),
       (1, 4, '강연집, 수필집, 연설문집'),
       (1, 5, '일반연속간행물'),
       (1, 6, '일반 학회, 단체, 협회, 기관, 연구기관'),
       (1, 7, '신문, 저널리즘'),
       (1, 8, '일반 전집, 총서'),
       (1, 9, '향토자료'),

       (2, 0, '철학'),
       (2, 1, '형이상학'),
       (2, 2, '인식론, 인과론, 인간학'),
       (2, 3, '철학의 체계'),
       (2, 4, '경학'),
       (2, 5, '동양철학, 동양사상'),
       (2, 6, '서양철학'),
       (2, 7, '논리학'),
       (2, 8, '심리학'),
       (2, 9, '윤리학, 도덕철학'),

       (3, 0, '종교'),
       (3, 1, '비교종교학'),
       (3, 2, '불교'),
       (3, 3, '기독교'),
       (3, 4, '도교'),
       (3, 5, '천도교'),
       (3, 6, ''),
       (3, 7, '힌두교, 브라만교'),
       (3, 8, '이슬람교(회교)'),
       (3, 9, '기타 제종교'),

       (4, 0, '사회과학'),
       (4, 1, '통계자료'),
       (4, 2, '경제학'),
       (4, 3, '사회학, 사회문제'),
       (4, 4, '정치학'),
       (4, 5, '행정학'),
       (4, 6, '법률, 법학'),
       (4, 7, '교육학'),
       (4, 8, '풍습, 예절, 민속학'),
       (4, 9, '국방, 군사학'),

       (5, 0, '자연과학'),
       (5, 1, '수학'),
       (5, 2, '물리학'),
       (5, 3, '화학'),
       (5, 4, '천문학'),
       (5, 5, '지학'),
       (5, 6, '광물학'),
       (5, 7, '생명과학'),
       (5, 8, '식물학'),
       (5, 9, '동물학'),

       (6, 0, '기술과학'),
       (6, 1, '의학'),
       (6, 2, '농업, 농학'),
       (6, 3, '공학, 공업일반, 토목공학, 환경공학'),
       (6, 4, '건축, 건축학'),
       (6, 5, '기계공학'),
       (6, 6, '전기공학, 통신공학, 전자공학'),
       (6, 7, '화학공학'),
       (6, 8, '제조업'),
       (6, 9, '생활과학'),

       (7, 0, '예술'),
       (7, 1, ''),
       (7, 2, '조각, 조형미술'),
       (7, 3, '공예'),
       (7, 4, '서예'),
       (7, 5, '회화, 도화, 디자인'),
       (7, 6, '사진예술'),
       (7, 7, '음악'),
       (7, 8, '공연예술, 매체예술'),
       (7, 9, '오락, 스포츠'),

       (8, 0, '언어'),
       (8, 1, '한국어'),
       (8, 2, '중국어'),
       (8, 3, '일본어'),
       (8, 4, '영어'),
       (8, 5, '독일어'),
       (8, 6, '프랑스어'),
       (8, 7, '스페인어 및 포르투갈어'),
       (8, 8, '이탈리아어'),
       (8, 9, '기타 제어'),

       (9, 0, '문학'),
       (9, 1, '한국문학'),
       (9, 2, '중국문학'),
       (9, 3, '일본문학 및 기타 아시아 제문학'),
       (9, 4, '영미문학'),
       (9, 5, '독일문학'),
       (9, 6, '프랑스문학'),
       (9, 7, '스페인문학 및 포르투갈문학'),
       (9, 8, '이탈리아문학'),
       (9, 9, '기타 제문학'),

       (10, 0, '역사'),
       (10, 1, '아시아'),
       (10, 2, '유럽'),
       (10, 3, '아프리카'),
       (10, 4, '북아메리카'),
       (10, 5, '남아메리카'),
       (10, 6, '오세아니아, 양극지방'),
       (10, 7, ''),
       (10, 8, '지리'),
       (10, 9, '전기');
