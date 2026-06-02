CREATE OR REPLACE FUNCTION update_table_meta()
    RETURNS TRIGGER AS $$
BEGIN

    INSERT INTO table_meta (table_name, last_modified)
    VALUES (TG_TABLE_NAME, LOCALTIMESTAMP) 
    ON CONFLICT (table_name)
        DO UPDATE SET last_modified = LOCALTIMESTAMP;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE TRIGGER results_modified_trigger
    AFTER INSERT OR UPDATE OR DELETE ON results
    FOR EACH STATEMENT
EXECUTE FUNCTION update_table_meta();