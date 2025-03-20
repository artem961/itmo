BEGIN;

--creation
CREATE TYPE human_conditions AS ENUM ('нейтрален','в трансе');
CREATE TYPE placement_types AS ENUM ('лежит', 'стоит', 'валяется');
CREATE TYPE accuracy_types AS ENUM ('неуклюже', 'аккуратно', 'резко');

CREATE TABLE IF NOT EXISTS locations(
id SERIAL PRIMARY KEY,
name VARCHAR(100) NOT NULL,
position POINT
);

CREATE TABLE IF NOT EXISTS humans(
id SERIAL PRIMARY KEY,
name VARCHAR(50) NOT NULL,
location_id INT REFERENCES locations(id) NOT NULL,
condition human_conditions
);

CREATE TABLE IF NOT EXISTS objects(
id SERIAL PRIMARY KEY,
name VARCHAR(100) NOT NULL,
location_id INT REFERENCES locations(id)
);

CREATE TABLE IF NOT EXISTS movement_types(
id SERIAL PRIMARY KEY,
name VARCHAR(30) NOT NULL
);

CREATE TABLE IF NOT EXISTS movements(
id SERIAL PRIMARY KEY,
start_location_id INT REFERENCES locations(id) NOT NULL,
final_location_id INT REFERENCES locations(id) NOT NULL,
human_id INT REFERENCES humans(id) NOT NULL,
movement_type_id INT REFERENCES movement_types(id) NOT NULL
);

CREATE TABLE IF NOT EXISTS disappearences(
object_id INT REFERENCES objects(id) PRIMARY KEY,
time TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS placements(
id SERIAL PRIMARY KEY,
main_object_id INT REFERENCES objects(id) NOT NULL,
placed_object_id INT REFERENCES objects(id) NOT NULL,
placement_type placement_types NOT NULL
);

CREATE TABLE ownerships(
human_id INT REFERENCES humans(id) NOT NULL,   
object_id INT REFERENCES objects(id),
PRIMARY KEY (human_id, object_id)
);

CREATE TABLE IF NOT EXISTS liftings(
--id SERIAL PRIMARY KEY,
human_id INT REFERENCES humans(id) PRIMARY KEY,   
object_id INT REFERENCES objects(id) NOT NULL,
accuracy accuracy_types NOT NULL
);

CREATE TABLE IF NOT EXISTS stops(
human_id INT REFERENCES humans(id) NOT NULL,   
object_id INT REFERENCES objects(id),
PRIMARY KEY (human_id, object_id)
);
COMMIT;