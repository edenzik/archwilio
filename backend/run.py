from flask import Flask, request, send_from_directory, jsonify
import time
import json
import os
import psycopg2
import psycopg2.extras

app = Flask(__name__)
conn = psycopg2.connect("dbname=%s user=%s host=%s password=%s" % (os.environ.get('PGDATABASE'), os.environ.get('PGUSER'), os.environ.get('PGHOST'), os.environ.get('PGPASSWORD')))

@app.route("/<path>")
def files(path):
    print path
    return send_from_directory('static', path)

@app.route("/css/<path>")
def css(path):
    return send_from_directory('static/css', path)

@app.route("/img/<path>")
def img(path):
    return send_from_directory('static/img', path)

@app.route("/js/<path>")
def js(path):
    return send_from_directory('static/js', path)

@app.route("/predict")
def predict(comment_text="this is a sample comment"):
    comment = RedditComment(comment_text)
    return comment.__str__()

#Returns all Computer Science courses as a JSON
@app.route("/course")
def api():
    query = "SELECT term,instance_id,code,name from course WHERE code LIKE 'COSI%'"
    cur = conn.cursor(cursor_factory=psycopg2.extras.RealDictCursor)
    cur.execute(query)
    rows = cur.fetchall()
    return jsonify(courses = rows)

if __name__ == "__main__":
    app.run(debug=True, host="0.0.0.0", port=8000)
