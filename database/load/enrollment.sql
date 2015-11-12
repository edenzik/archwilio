CREATE TABLE IF NOT EXISTS enrollment(
    term varchar,
    class_nbr varchar,
    section varchar,
    subject varchar,
    catalog varchar,
    description varchar,
    component varchar,
    course_id varchar,
    survey_id varchar,
    year integer
);

ALTER TABLE enrollment ALTER COLUMN year SET DEFAULT :YEAR;

COPY enrollment(
    term, 
    class_nbr, 
    section,
    subject,
    catalog,
    description,
    component,
    course_id,
    survey_id
) 
FROM :SOURCE 
CSV HEADER DELIMITER ',';
