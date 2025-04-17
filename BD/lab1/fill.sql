INSERT INTO locations (name, position) VALUES ('Пустая часть комнаты', Point(140, 25));
INSERT INTO locations (name, position) VALUES ('Аппартаменты отеля', Point(55, 5));
INSERT INTO locations (name, position) VALUES ('ИТМО', Point(0, 0));

INSERT INTO humans (name, location_id, condition) VALUES ('Боумен', 1, 'в трансе');
INSERT INTO humans (name, location_id, condition) VALUES ('Виталик', 1, 'нейтрален');

INSERT INTO objects (name, location_id) VALUES ('капсула', 1);
INSERT INTO objects (name, location_id) VALUES ('кофейный столик', 2);
INSERT INTO objects (name, location_id) VALUES ('видеофон системы Белла', 2);
INSERT INTO objects (name, location_id) VALUES ('телефонная книга', 2);
INSERT INTO objects (name, location_id) VALUES ('герметическая перчатка', NULL);


INSERT INTO movement_types (name) VALUES ('пешком');
INSERT INTO movement_types (name) VALUES ('на машине');
INSERT INTO movement_types (name) VALUES ('на рояле');

INSERT INTO movements (start_location_id, final_location_id, human_id, movement_type_id) VALUES (1, 2, 1, 1);
INSERT INTO movements (start_location_id, final_location_id, human_id, movement_type_id) VALUES (3, 2, 2, 3);

INSERT INTO disappearences (object_id) VALUES (1);

INSERT INTO placements (main_object_id, placed_object_id, placement_type) VALUES (2, 4, 'лежит');
INSERT INTO placements (main_object_id, placed_object_id, placement_type) VALUES (2, 3, 'стоит');

INSERT INTO ownerships (human_id, object_id) VALUES (1, 5);

INSERT INTO liftings (human_id, object_id, accuracy) VALUES (1, 4, 'неуклюже');

INSERT INTO stops (human_id, object_id) VALUES (1, 2);