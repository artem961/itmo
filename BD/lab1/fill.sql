INSERT INTO locations (name, position) VALUES ('Пустая часть комнаты', Point(140, 25));
INSERT INTO locations (name, position) VALUES ('Аппартаменты отеля', Point(55, 5));
INSERT INTO locations (name, position) VALUES ('ИТМО', Point(0, 0));

INSERT INTO humans (name, location, condition) VALUES ('Боумен', 1, 'в трансе'), ('Виталик', 1, 'нейтрален');

INSERT INTO objects (name, location) VALUES ('капсула', 1), ('кофейный столик', 2), ('видеофон системы Белла', 2), ('телефонная книга', 2), ('герметическая перчатка', NULL);

INSERT INTO movement_types (name) VALUES ('пешком'), ('на машине'), ('на рояле');

INSERT INTO movements (start_location, final_location, human, movement_type) VALUES (1, 2, 1, 1), (3, 2, 2, 3);

INSERT INTO disappearences (object) VALUES (1);

INSERT INTO placements (main_object, placed_object, placement_type, location) VALUES (2, 4, 'лежит', 2), (2, 3, 'стоит', 2);

INSERT INTO ownerships (human, object) VALUES (1, 5);

INSERT INTO liftings (human, object, accuracy) VALUES (1, 4, 'неуклюже');

INSERT INTO stops (human, object) VALUES (1, 2);