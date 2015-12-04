var express = require('express');
var app = express();
var bodyParser = require('body-parser');
var q = require('q');
var child_process = require('child_process');

app.use(express.static('public'));

app.use(bodyParser.json());

// respond with "hello world" when a GET request is made to the homepage
app.post('/explore', function(req, res) {
    console.log(req.body);
    getResponse(req.body).then(console.log);
});

function getResponse(body) {
    var deferred = q.defer();
    child_process.exec('python ../backend/course_selection_engine/CoursePathPredictor.py' + ' \"' + body + '\"',
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
