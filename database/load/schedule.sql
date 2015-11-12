CREATE TABLE dump(data json);

COPY dump(data) 
FROM :SOURCE
CSV QUOTE e'\x01' DELIMITER e'\x02';


CREATE TABLE section AS
(
    SELECT 
    data->>'status' AS status,
    data->>'course' AS course,
    data->>'section' AS section,
    data->>'details' AS details,
    data->>'waiting' AS waiting,
    data->>'limit' AS limit,
    data->>'enrolled' AS enrolled
    FROM dump 
    WHERE data->>'type' = 'section'
);

CREATE TABLE section_status_text AS
(
    SELECT 
    data->>'status' AS status,
    data->>'status_text' AS status_text
    FROM dump 
    WHERE data->>'type'='section' 
    GROUP BY data->>'status', data->>'status_text'
);

CREATE TABLE section_times AS
(
    SELECT
    section AS section,
    data->>'start' AS start_time,
    data->>'end' AS end_time,
    data->>'room' AS room,
    data->>'building' AS building,
    data->>'days' AS days
    FROM (
       SELECT 
       data->>'section' AS section,
       json_array_elements_text(data->'times')::json AS data
       FROM dump
       WHERE data->>'type' = 'section' 
    ) AS temp
);

CREATE TABLE section_instructor AS
(
    SELECT
    data->>'section' AS section, 
    json_array_elements_text(data->'instructors') AS instructor_id
    FROM dump 
    WHERE data->>'type'='section'
);

CREATE TABLE requirement AS
(
    SELECT
    data->>'id' AS id, 
    data->>'short' AS short_description, 
    data->>'long' AS log_description 
    FROM dump 
    WHERE data->>'type'='requirement'
);

CREATE TABLE term AS
(
    SELECT
    data->>'id' AS id, 
    data->>'name' AS name, 
    data->>'start' AS start, 
    data->>'end' AS end 
    FROM dump 
    WHERE data->>'type'='term'
);


CREATE TABLE instructor AS
(
    SELECT
    data->>'id' AS id, 
    data->>'first' AS first_name, 
    data->>'middle' AS middle_name, 
    data->>'last' AS last_name, 
    data->>'email' AS email 
    FROM dump 
    WHERE data->>'type'='instructor'
);


CREATE TABLE subject AS
(
    SELECT 
    data->>'id' AS id,
    data->>'term' AS term,
    data->>'name' AS name,
    data->>'abbreviation' AS short_name
    FROM dump 
    WHERE data->>'type' = 'subject'
);

CREATE TABLE course AS
(
    SELECT
    data->>'id' AS id,
    data->>'term' AS term,
    data->>'continuity_id' AS instance_id,
    data->>'code' AS code,
    data->>'name' AS name,
    data->>'description' AS description,
    data->>'credits' AS credits
    FROM dump 
    WHERE data->>'type'='course'
);

CREATE TABLE course_requirement AS
(
    SELECT
    data->>'id' AS id, 
    json_array_elements_text(data->'requirements') AS requirement
    FROM dump 
    WHERE data->>'type'='course'
);
