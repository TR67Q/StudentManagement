INSERT INTO students (name, kana_name, nickname, mail_address, area, age, gender)
VALUES('山田太郎', 'ヤマダタロウ', 'タロ', 'taro@example.com', '東京', 27, '男性'),
      ('鈴木一郎', 'スズキイチロウ', 'イチ', 'ichiro@example.com', '大阪', 30, '男性'),
      ('田中花子', 'タナカハナコ', 'ハナ', 'hana@example.com', '北海道', 22, '女性'),
      ('佐藤良子', 'サトウリョウコ', 'リョウ', 'ryoko@example.com', '福岡', 28, '女性'),
      ('伊藤悠', 'イトウハルカ', 'ハル', 'haruka@example.com', '愛知', 35, 'その他');

INSERT INTO students_courses (student_id, course_name, starting_date, end_date)
VALUES(1, 'Music', '2024-04-01 00:00:00', '2025-03-31 00:00:00'),
      (1, 'Design', '2024-06-01 00:00:00', '2024-11-30 00:00:00'),
      (2, 'Java', '2024-05-01 00:00:00', '2025-03-31 00:00:00'),
      (3, 'English', '2024-08-01 00:00:00', '2025-07-31 00:00:00'),
      (4, 'Java', '2025-01-01 00:00:00', '2025-03-31 00:00:00'),
      (4, 'Music', '2023-04-01 00:00:00', '2026-03-31 00:00:00'),
      (5, 'Marketing', '2024-04-01 00:00:00', '2026-03-31 00:00:00');

