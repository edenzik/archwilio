CREATE TEMP TABLE tmp_x (instance_id varchar, code varchar, name varchar, description varchar, score varchar);
COPY tmp_x FROM :SOURCE (FORMAT csv);

UPDATE course
SET    score = tmp_x.score
FROM   tmp_x
WHERE  course.instance_id = tmp_x.instance_id;

DROP TABLE tmp_x;

-- CSV HEADER DELIMITER ',';
