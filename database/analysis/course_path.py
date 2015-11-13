
#!/usr/bin/python
import psycopg2
import os
import re

conn = psycopg2.connect("dbname=%s user=%s host=%s password=%s" % (os.environ.get('PGDATABASE'), os.environ.get('PGUSER'), os.environ.get('PGHOST'), os.environ.get('PGPASSWORD')))

cur = conn.cursor()
cur.execute(open("../queries/course_path.sql").read())
rows = cur.fetchall()

count = 0
print 'digraph {rankdir="LR"'
for row in rows:
#	print row
	count+= 1
	if count > 200:
		break
	print '"%s" -> "%s" [label="%s", weight="%s"]' % (row[0],row[1],row[2],row[2])

print "}"
cur.close()
conn.close()


