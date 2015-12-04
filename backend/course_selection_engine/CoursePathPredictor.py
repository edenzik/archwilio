import json
from collections import defaultdict
from itertools import *
import psycopg2
import psycopg2.extras
import os
import sys
import ast

def window(iterable, size):
    iters = tee(iterable, size)
    for i in xrange(1, size):
        for each in iters[i:]:
            next(each, None)
    return izip(*iters)

class Node(dict):
    def __init__(self,node_id,label):
        self['id'] = node_id
        self['label'] = label
        self['value'] = 10
        
class Edge(dict):
    def __init__(self, from_node, to_node):
        self['from'] = from_node['id']
        self['to'] = to_node['id']

conn = psycopg2.connect("dbname=%s user=%s host=%s password=%s" % (os.environ.get('PGDATABASE'), os.environ.get('PGUSER'), os.environ.get('PGHOST'), os.environ.get('PGPASSWORD')))

query_prereqs = "SELECT prereq.id AS course_id, course.instance_id AS prereq_id FROM prereq, course WHERE upper(prereq.prereq_name) = course.code AND course.code LIKE 'COSI%' GROUP BY prereq.id,course.instance_id;"

cur = conn.cursor()
cur.execute(query_prereqs)
rows = cur.fetchall()
cur.close()



    
course_to_prereqs = defaultdict(list)
for row in rows:
    course_to_prereqs[row[0]].append(row[1])



query_courses = "SELECT term,instance_id,name from course WHERE code LIKE 'COSI%' AND instance_id NOT LIKE '%-IND' GROUP BY term,instance_id,name"
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

courses = list(set(courses))


def meets_prereq(course, prev_courses):
    return set(prev_courses).issuperset(course_to_prereqs[course])

def already_taken(course, prev_courses):
    return course in set(prev_courses)

def will_take(course, later_courses):
    return course in set(later_courses)

def get_prereqs(course):
    return course_to_prereqs[course]

def find_node(nodes, value):
    for node in nodes:
        if node['label'] == value:
            return node


class CoursePathPredictor:
    def __init__(self,source):
        new_source = []
        for semester in source:
            new_sem = []
            for course in semester:
                new_sem += [str(source).split("|")[1]]
            new_source += new_sem
        self.predict(new_source)

    def predict(self,selected_path):
        suggested_path = [[]] * (len(selected_path) + 1)
        previous_semester = []
        for idx in range(len(suggested_path)):
                prior_courses = list(chain.from_iterable(selected_path[0:idx]))
                later_courses = list(chain.from_iterable(selected_path[idx+1:]))
                for course in courses:
                    if not meets_prereq(course, prior_courses): continue
                    if already_taken(course, prior_courses): continue
                    if will_take(course, later_courses): continue
                    suggested_path[idx] = suggested_path[idx] + [Node(str(idx) +"|" +  str(course),course_to_name[course])]
                previous_semester = suggested_path[idx]

        edges = []
        for from_nodes,to_nodes in list(window(suggested_path,2)):
            for from_node in from_nodes:
                for to_node in to_nodes:
                    edges += [Edge(from_node,to_node)]


        nodes = list(chain.from_iterable(suggested_path))
        self.edges = edges
        self.nodes = nodes

    def __str__(self):
        return json.dumps({"nodes":self.nodes,"edges":self.edges})

print CoursePathPredictor(ast.literal_eval(sys.stdin.read()))





