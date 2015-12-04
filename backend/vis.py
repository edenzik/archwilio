import json
from collections import defaultdict
from itertools import *
import psycopg2
import psycopg2.extras
import os

start_term = 1133

end_term = start_term + 7 #7 semesters after the first semester

conn = psycopg2.connect("dbname=%s user=%s host=%s password=%s" % (os.environ.get('PGDATABASE'), os.environ.get('PGUSER'), os.environ.get('PGHOST'), os.environ.get('PGPASSWORD')))

query_prereqs = "SELECT prereq.id AS course_id, course.instance_id AS prereq_id FROM prereq, course WHERE upper(prereq.prereq_name) = course.code AND course.code LIKE 'COSI%' GROUP BY prereq.id,course.instance_id;"

cur = conn.cursor()
cur.execute(query_prereqs)
rows = cur.fetchall()
cur.close()

prereqs = defaultdict(list)
for row in rows:
    prereqs[row[0]].append(row[1])

query_courses = "SELECT term,instance_id,name from course WHERE code LIKE 'COSI%' AND instance_id NOT LIKE '%-IND' AND term::int>=" + str(start_term)
cur = conn.cursor(cursor_factory=psycopg2.extras.RealDictCursor)
cur.execute(query_courses)
rows = cur.fetchall()


cur.close()
course_to_name = {}
for row in rows:
    course_to_name[row['instance_id']] = row['name']
    
courses = []
for row in rows:
    courses += [row['instance_id']]


course_path = [['001651']]


def meets_prereq(course, prev_courses):
    print prev_courses
    print set(prev_courses).issuperset(prereqs[course])
    return set(prev_courses).issuperset(prereqs[course])

def not_already_taken(course, prev_courses):
    return course not in set(prev_courses)

def not_currently_taking(course, current_courses):
    return course not in set(current_courses)

def not_taking_in_future(course, future_courses):
    return course not in set(future_courses)

semester_path = [[]] * (len(course_path) + 1)

for idx in range(len(semester_path)):
    courses_taken_before_this_point = list(chain.from_iterable(course_path[0:idx]))
    print courses_taken_before_this_point
    courses_taken_after_this_point = list(chain.from_iterable(course_path[idx:]))
    current_semester = semester_path[idx]
    semester_path[idx] = course_path[idx] if len(course_path) > idx else []
    for course in courses:
        if meets_prereq(course,courses_taken_before_this_point) and not_already_taken(course,courses_taken_before_this_point) and not_currently_taking(course,semester_path[idx]) and not_taking_in_future(course,courses_taken_after_this_point):
            semester_path[idx] = semester_path[idx] + [course]

print semester_path
edges = defaultdict(list)

class Node():
    def __init__(self,label):
        self.label = label

    def to_json(self):
        return json.dumps({"id":id(self),"label":self.label})

class Edge():
    def __init__(self, from_node, to_node):
        self.from_node = from_node
        self.to_node = to_node

    def to_json(self):
        return json.dumps({"from":id(self.from_node),"to":id(self.to_node)})


b = Edge("a","b")
Node("hey")
Node("hey")
Node("hey")

