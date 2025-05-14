CREATE TYPE Furnish as ENUM ('DESIGNER' , 'LITTLE', 'BAD', 'NONE');
CREATE TYPE Transport as ENUM ('FEW', 'NONE', 'LITTLE', 'NORMAL', 'ENOUGH');

CREATE TABLE houses(
id serial PRIMARY KEY,
name varchar(500),
year integer CHECK (year > 0),
num_of_flats bigint CHECK (num_of_flats > 0)
);

CREATE TABLE flats(
id serial PRIMARY KEY,
name varchar(500) NOT NULL,
x float4 NOT NULL CHECK (x > -621),
y double precision,
date DATE,
area float4 CHECK (area > 0),
numb_of_rooms integer CHECK (numb_of_rooms > 0),
height bigint,
furnish Furnish,
transport Transport NOT NULL,
house integer REFERENCES houses(id) ON DELETE SET NULL
);

CREATE FUNCTION insertHouse(varchar(500), integer, bigint) RETURNS INTEGER AS $$
    DECLARE
        house_id integer;
    BEGIN
        SELECT id INTO house_id FROM houses WHERE name=$1 AND year=$2 AND num_of_flats=$3
        LIMIT 1;

        IF (house_id IS NULL) THEN
            INSERT INTO houses (name, year, num_of_flats)
             VALUES ($1, $2, $3)
              RETURNING id INTO house_id;
        END IF;
        RETURN house_id;
    END;
$$ LANGUAGE plpgsql;