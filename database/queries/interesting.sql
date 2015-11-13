-- I forgot what this does
select description, count(*) from enrollment group by description ORDER BY count(*) DESC;

-- Finds all of the course paths involving a computer science class
select A.description, B.description, count(*) from enrollment AS A, enrollment AS B WHERE A.survey_id = B.survey_id AND A.course_id != B.course_id AND B.term > A.term AND (B.term::int - A.term::int) = (SELECT min(id)::int - A.term::int FROM term WHERE id::int > A.term::int) AND A.subject = 'COSI' GROUP BY A.description, B.description


