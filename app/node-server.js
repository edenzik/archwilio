var express = require('express');
var app = express();
var bodyParser = require('body-parser');
var q = require('q');
var child_process = require('child_process');

app.use(express.static('public'));

app.use(bodyParser.json());

// respond with "hello world" when a GET request is made to the homepage
app.post('/explore', function(req, res) {
    var body = req.body;
    if (!body.indexOf(['-1']) >= 0) {
        body.unshift(['-1'])
    }
    console.log(body);
    // Replace double quotes with single quotes
    body = JSON.stringify(body).replace(/"/g, "'");
    getResponse(body).then(function(data) {
        res.end(data);
    });
});

function getResponse(body) {
    var deferred = q.defer();
    var cmd = 'echo "' + body + '" | python ../backend/course_selection_engine/CoursePathPredictor.py visjs';
    console.log('command = ' + cmd);
    child_process.exec('echo "' + body + '" | python ../backend/course_selection_engine/CoursePathPredictor.py visjs',
    function (error, stdout, stderr) {
        deferred.resolve(stdout);
        console.log('stdout: ' + stdout);
        console.log('stderr: ' + stderr);
        if (error !== null) {
            console.log('exec error: ' + error);
        }
    });
    return deferred.promise;
}

app.listen(8000);
