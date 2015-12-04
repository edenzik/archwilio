import sys
import os
import json
import psycopg2
import psycopg2.extras
from random import shuffle
from itertools import *
from collections import defaultdict

conn = psycopg2.connect("dbname=%s user=%s host=%s password=%s" % (os.environ.get('PGDATABASE'), os.environ.get('PGUSER'), os.environ.get('PGHOST'), os.environ.get('PGPASSWORD')))

start_term = 1133
end_term = start_term + 7 #7 semesters after the first semester

query_prereqs = "SELECT prereq.id AS course_id, course.instance_id AS prereq_id FROM prereq, course WHERE upper(prereq.prereq_name) = course.code AND course.code LIKE 'COSI%' GROUP BY prereq.id,course.instance_id;"

cur = conn.cursor()
cur.execute(query_prereqs)
rows = cur.fetchall()
cur.close()
course_to_prereqs = defaultdict(list)
for row in rows:
    course_to_prereqs[row[0]].append(row[1])


query_courses = "SELECT term,instance_id,name from course WHERE code LIKE 'COSI%' AND instance_id NOT LIKE '%-IND' AND term::int>=" + str(start_term)
cur = conn.cursor(cursor_factory=psycopg2.extras.RealDictCursor)
cur.execute(query_courses)
rows = cur.fetchall()

cur.close()

course_to_name = {}
for row in rows:
    course_to_name[row['instance_id']] = row['name']

course_to_term = defaultdict(list)
for row in rows:
    course_to_term[row['instance_id']].append(row['term'])

course_ids = map(lambda t: t['instance_id'], rows)

requirements = set(["001651","001736","012337","001722","001751"])

number_of_cs_courses_per_semester = 3
number_of_semesters = 8
total_cs_courses = number_of_cs_courses_per_semester * number_of_semesters
course_combos = combinations(course_ids, total_cs_courses)


contains_requirements = lambda courses: set(courses).issuperset(requirements)

add_terms = lambda courses: map(lambda course: (course, course_to_term[course]),courses)

course_terms_to_courses = lambda course_term_pair_list : map(lambda course_term_pair: course_term_pair[0], course_term_pair_list)

prereqs_of_courses = lambda courses: set(list(chain.from_iterable(map(lambda course: course_to_prereqs[course],courses))))

has_prereqs = lambda courses: float(len(prereqs_of_courses(courses).difference(courses)))/len(courses) <= .125


splitter = lambda A, n=number_of_cs_courses_per_semester: [A[i:i+n] for i in range(0, len(A), n)]

course_name_convert = lambda course: course_to_name[course]

make_dot_node = lambda semester: "\"" + " | ".join(map(course_name_convert,semester)) + "\""

def correct_permutation(courses):
    courses = list(courses)
    shuffle(courses)
    so_far = []
    for semester in splitter(courses):
        so_far += semester
        if has_prereqs(so_far):
            continue
        return False
    return True


step1 = ifilter(contains_requirements,course_combos)
step2 = ifilter(has_prereqs,step1)
step3 = permutations(next(step2))
step4 = ifilter(correct_permutation,step3)
#step5 = imap(add_terms,step2)

print "digraph {"
print "->".join(map(make_dot_node,splitter(next(step4))))
print "}"
sys.exit()

