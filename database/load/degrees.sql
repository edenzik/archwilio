CREATE TABLE IF NOT EXISTS degrees(
    comp_term varchar,
    acad_plan varchar,
    description varchar,
    plan_type varchar,
    survey_id varchar
);

COPY degrees(
    comp_term,
    acad_plan,
    description,
    plan_type,
    survey_id
) 
FROM :SOURCE 
CSV HEADER DELIMITER ',';
