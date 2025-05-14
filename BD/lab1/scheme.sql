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
location INT REFERENCES locations(id) NOT NULL,
condition human_conditions
);

CREATE TABLE IF NOT EXISTS objects(
id SERIAL PRIMARY KEY,
name VARCHAR(100) NOT NULL,
location INT REFERENCES locations(id)
);

CREATE TABLE IF NOT EXISTS movement_types(
id SERIAL PRIMARY KEY,
name VARCHAR(30) NOT NULL
);

CREATE TABLE IF NOT EXISTS movements(
id SERIAL PRIMARY KEY,
start_location INT REFERENCES locations(id) NOT NULL,
final_location INT REFERENCES locations(id) NOT NULL CHECK (final_location != start_location),
human INT REFERENCES humans(id) NOT NULL,
movement_type INT REFERENCES movement_types(id) NOT NULL
);

CREATE TABLE IF NOT EXISTS disappearences(
object INT REFERENCES objects(id) PRIMARY KEY,
time TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS placements(
id SERIAL PRIMARY KEY,
main_object INT REFERENCES objects(id) NOT NULL,
placed_object INT REFERENCES objects(id) NOT NULL,
placement_type placement_types NOT NULL,
location INT REFERENCES locations(id) NOT NULL
);

CREATE TABLE ownerships(
human INT REFERENCES humans(id) NOT NULL,   
object INT REFERENCES objects(id),
PRIMARY KEY (human, object)
);

CREATE TABLE IF NOT EXISTS liftings(
human INT REFERENCES humans(id),   
object INT REFERENCES objects(id) NOT NULL,
accuracy accuracy_types NOT NULL,
PRIMARY KEY(human, object)
);

CREATE TABLE IF NOT EXISTS stops(
human INT REFERENCES humans(id) NOT NULL,   
object INT REFERENCES objects(id),
time TIMESTAMP NOT NULL DEFAULT now(),
PRIMARY KEY (human, object)
);
