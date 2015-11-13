#!/usr/bin/python
import psycopg2
import os
import re

conn = psycopg2.connect("dbname=%s user=%s host=%s password=%s" % (os.environ.get('PGDATABASE'), os.environ.get('PGUSER'), os.environ.get('PGHOST'), os.environ.get('PGPASSWORD')))

# Open a cursor to perform database operations
cur = conn.cursor()

cur.execute("DROP TABLE IF EXISTS prereq;")
cur.execute("DROP TABLE IF EXISTS coreq;")

cur.execute("CREATE TABLE prereq (id text, prereq_name text);")
cur.execute("CREATE TABLE coreq (id text, coreq_name text);")

# Execute a command: this creates a new table
cur.execute("SELECT id, description FROM course")

# Pass data to fill a query placeholders and let Psycopg perform
# the correct conversion (no more SQL injections!)

rows = cur.fetchall()

re.match

for row in rows:
	prereq_match = re.search('(Prerequisites:[^\.]+)', row[1])
	if prereq_match:
		#print prereq_match.group(1)
		prereq_string = prereq_match.group(1)
		classes = re.finditer('([A-Z]+ \d+[a-z])',prereq_string)
		if classes:
			for prereq in classes:
				cur.execute("INSERT INTO prereq(id,prereq_name) VALUES ('%s','%s');" % (row[0], prereq.group(1)))
	coreq_match = re.search('(Corequisites:[^\.]+)', row[1])
	if coreq_match:
		#print prereq_match.group(1)
		coreq_string = coreq_match.group(1)
		classes = re.finditer('([A-Z]+ \d+[a-z])',coreq_string)
		if classes:
			for coreq in classes:
				cur.execute("INSERT INTO coreq(id,coreq_name) VALUES ('%s','%s');" % (row[0], coreq.group(1)))
	#print row
# Query the database and obtain data as Python objects

# Make the changes to the database persistent
conn.commit()

print "LOADED all pre/co requisites into the database"

# Close communication with the database
cur.close()
conn.close()


