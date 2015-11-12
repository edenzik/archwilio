select description, count(*) from enrollment group by description ORDER BY count(*) DESC;
