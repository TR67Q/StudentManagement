INSERT INTO students (name, kana_name, nickname, mail_address, area, age, gender, isDeleted)
VALUES('山田太郎', 'ヤマダタロウ', 'タロ', 'taro@example.com', '東京', 27, '男性', false),
      ('鈴木一郎', 'スズキイチロウ', 'イチ', 'ichiro@example.com', '大阪', 30, '男性', false),
      ('田中花子', 'タナカハナコ', 'ハナ', 'hana@example.com', '北海道', 22, '女性', false),
      ('佐藤良子', 'サトウリョウコ', 'リョウ', 'ryoko@example.com', '福岡', 28, '女性', false),
      ('伊藤悠', 'イトウハルカ', 'ハル', 'haruka@example.com', '愛知', 35, 'その他', false);

INSERT INTO students_courses (student_id, course_name, starting_date, end_date, isDeleted)
VALUES(1, 'Music', '2024-04-01 00:00:00', '2025-03-31 00:00:00', false),
      (1, 'Design', '2024-06-01 00:00:00', '2024-11-30 00:00:00', false),
      (2, 'Java', '2024-05-01 00:00:00', '2025-03-31 00:00:00', false),
      (3, 'English', '2024-08-01 00:00:00', '2025-07-31 00:00:00',false),
      (4, 'Java', '2025-01-01 00:00:00', '2025-03-31 00:00:00', false),
      (4, 'Music', '2023-04-01 00:00:00', '2026-03-31 00:00:00', false),
      (5, 'Marketing', '2024-04-01 00:00:00', '2026-03-31 00:00:00',false);

INSERT INTO student_course_status (course_name, course_status_id, status)
VALUES('Music', 1, '仮申込'),
      ('Design', 2, '仮申込'),
      ('Java', 3, '本申込'),
      ('English', 4, '受講中'),
      ('Java', 5, '仮申込'),
      ('Music', 6, '受講終了'),
      ('Marketing', 7, '仮申込');