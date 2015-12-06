import psycopg2
import os
import re

conn = psycopg2.connect("dbname=%s user=%s host=%s password=%s" % (os.environ.get('PGDATABASE'), os.environ.get('PGUSER'), os.environ.get('PGHOST'), os.environ.get('PGPASSWORD')))

# Open a cursor to perform database operations
cur = conn.cursor()

cur.execute("DROP TABLE IF EXISTS prereq;")
cur.execute("DROP TABLE IF EXISTS coreq;")

cur.execute("CREATE TEMPORARY TABLE temp_prereq (id text, term text, prereq_name text);")
cur.execute("CREATE TEMPORARY TABLE temp_coreq (id text, term text, coreq_name text);")

# Execute a command: this creates a new table
cur.execute("SELECT id, description, term FROM course")

# Pass data to fill a query placeholders and let Psycopg perform
# the correct conversion (no more SQL injections!)

rows = cur.fetchall()

re.match

for row in rows:
	prereq_match = re.search('(Prerequisites?:[^\.]+)', row[1])
	if prereq_match:
		prereq_string = prereq_match.group(1)
		classes = re.finditer('([A-Z]+ \d+[a-z])',prereq_string)
		if classes:
			for prereq in classes:
				cur.execute("INSERT INTO temp_prereq(id,term,prereq_name) VALUES ('%s','%s','%s');" % (row[0].split("-")[1], row[2], prereq.group(1)))
	coreq_match = re.search('(Corequisites:[^\.]+)', row[1])
	if coreq_match:
		#print prereq_match.group(1)
		coreq_string = coreq_match.group(1)
		classes = re.finditer('([A-Z]+ \d+[a-z])',coreq_string)
		if classes:
			for coreq in classes:
				cur.execute("INSERT INTO temp_coreq(id,term,coreq_name) VALUES ('%s','%s','%s');" % (row[0].split("-")[1], row[2], coreq.group(1)))
	#print row
# Query the database and obtain data as Python objects

# Make the changes to the database persistent

cur.execute("CREATE TABLE prereq AS (SELECT temp_prereq.id AS course_id, temp_prereq.term AS course_term, to_course.instance_id AS prereq_id FROM temp_prereq, course AS to_course WHERE upper(temp_prereq.prereq_name) = to_course.code GROUP BY temp_prereq.id,temp_prereq.term,to_course.instance_id);")

cur.execute("CREATE TABLE coreq AS (SELECT temp_coreq.id AS course_id, temp_coreq.term AS course_term, to_course.instance_id AS coreq_id FROM temp_coreq, course AS to_course  WHERE upper(temp_coreq.coreq_name) = to_course.code GROUP BY temp_coreq.id,temp_coreq.term,to_course.instance_id);")

conn.commit()

print "LOADED all pre/co requisites into the database"

# Close communication with the database
cur.close()
conn.close()


