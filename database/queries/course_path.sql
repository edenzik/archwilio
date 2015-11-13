-- Finds all course paths of students having taken a CS class
select MIN(A.description), MIN(B.description), count(*)::int from enrollment AS A, enrollment AS B WHERE A.survey_id = B.survey_id AND A.course_id != B.course_id AND B.term > A.term AND (B.term::int - A.term::int) = (SELECT min(id)::int - A.term::int FROM term WHERE id::int > A.term::int) AND A.subject = 'COSI' AND B.subject = 'COSI' GROUP BY A.catalog, B.catalog ORDER BY count(*) DESC;

