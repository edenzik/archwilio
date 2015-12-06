CREATE TEMP TABLE tmp_x (instance_id varchar, code varchar, name varchar, description varchar, score varchar, keywords varchar);
COPY tmp_x FROM :SOURCE (FORMAT csv);

UPDATE course
SET    score = tmp_x.score, keywords = tmp_x.keywords
FROM   tmp_x
WHERE  course.code = tmp_x.code;

DROP TABLE tmp_x;

-- CSV HEADER DELIMITER ',';
