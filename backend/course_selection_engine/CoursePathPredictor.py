import json
from collections import defaultdict
from itertools import *
import psycopg2
import psycopg2.extras
import os
import sys
import ast


conn = psycopg2.connect("dbname=%s user=%s host=%s password=%s" % (os.environ.get('PGDATABASE'), os.environ.get('PGUSER'), os.environ.get('PGHOST'), os.environ.get('PGPASSWORD')))

query_courses = """
    SELECT course.term,course.instance_id, course.code,course.name 
    FROM course, term 
    WHERE course.code LIKE 'COSI%' AND 
    course.instance_id NOT LIKE '%-IND' AND 
    course.code NOT LIKE '%COSI 2___' AND
    course.term = term.id AND 
    to_date(term.start, 'YYYY MM') >= to_date('2012 08', 'YYYY MM')
"""

cur = conn.cursor(cursor_factory=psycopg2.extras.RealDictCursor)
cur.execute(query_courses)
rows = cur.fetchall()
cur.close()

course_to_name = defaultdict(str)
course_to_name.default_factory = lambda: "UNKNOWN COURSE"
for row in rows:
    course_to_name[row['instance_id']] = row['name']

course_to_name['-1'] = "Enrollment"
    
courses = []
for row in rows:
    courses += [row['instance_id']]

courses = list(set(courses))

# Fetches prereqs

query_prereqs = """
SELECT course_id, prereq_id 
FROM prereq, term 
WHERE prereq.course_term = term.id AND 
to_date(term.start, 'YYYY MM') >= to_date('2012 08', 'YYYY MM')
"""

cur = conn.cursor()
cur.execute(query_prereqs)
rows = cur.fetchall()
cur.close()

DUMMY_COURSE = '-1'
    
course_to_prereqs = defaultdict(list)   #a map from a course to list of prereq
course_to_prereqs.default_factory = lambda: [DUMMY_COURSE]
for row in rows:
    course_to_prereqs[row[0]].append(row[1])

prereq_to_course = defaultdict(list)   #a map from a course to list of prereq
for row in rows:
    prereq_to_course[row[1]].append(row[0])
prereq_to_course['-1'] = courses



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


class Node(dict):
    def __init__(self,course,idx,label,color="gray"):
        self['color'] = color
        self['course'] = course
        self['idx'] = idx
        self['id'] = "{0}|{1}".format(idx,course)
        self['label'] = label
        self['value'] = 10
    def __dot__(self):
        return "\"{0}\" [label=\"{1}\", style=filled, color={2}];".format(self['id'],self['label'],self['color'])
    def __hash__(self):
        return hash(self['id'])
    def __eq__(self,other):
        return self.__hash__() == other.__hash__()
        
class Edge(dict):
    def __init__(self, from_node, to_node):
        self['from'] = from_node
        self['to'] = to_node
    def __dot__(self):
        return "\"{0}\" -> \"{1}\";".format(self['from']['id'],self['to']['id'])
    def __hash__(self):
        return hash(self['from']['id'] + "|" + self['to']['id'])
    def __eq__(self,other):
        return self.__hash__() == other.__hash__()

class CoursePathPredictor:
    def __init__(self,source):
        self.nodes = set()
        self.edges = set()
        self.predict(source)

    def predict(self,selected_path):
        suggested_path = selected_path + [[]]
        for idx in range(len(suggested_path)):
            prior_courses = list(chain.from_iterable(selected_path[0:idx]))
            later_courses = list(chain.from_iterable(selected_path[idx+1:]))
            potential_courses = list(suggested_path[idx])
            for prereq in prior_courses:
                for course in prereq_to_course[prereq]:
                    if not meets_prereq(course, prior_courses): continue
                    if already_taken(course, prior_courses): continue
                    if will_take(course, later_courses): continue
                    potential_courses += [course]
            potential_courses = list(set(potential_courses))
            for prereq in suggested_path[idx-1]:
                for course in potential_courses:
                    from_node = Node(prereq,idx-1,course_to_name[prereq],"red")
                    to_node = Node(course,idx,course_to_name[course])
                    edge = Edge(from_node,to_node)
                    self.nodes.add(from_node)
                    self.nodes.add(to_node)
                    self.edges.add(edge)

    def __dot__(self):
        return "digraph {{ rankdir=\"LR\";\n {0} {1} }}".format("\n".join(map(Node.__dot__, self.nodes)),"\n".join(map(Edge.__dot__,self.edges)))

    def __visjs__(self):
        return json.dumps({"nodes":self.nodes,"edges":self.edges})

output_format = sys.argv.pop()
if output_format == 'visjs':
    print CoursePathPredictor(ast.literal_eval(sys.stdin.read())).__visjs__()
    sys.exit(0)
if output_format == 'dot':
    print CoursePathPredictor(ast.literal_eval(sys.stdin.read())).__dot__()
    sys.exit(0)

print "HELP: \t python CoursePathPredictor.py [visjs|dot]"
sys.exit(1)





