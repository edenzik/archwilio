var express = require('express');
var app = express();
var bodyParser = require('body-parser');
var q = require('q');
var child_process = require('child_process');

app.use(express.static('public'));

app.use(bodyParser.json());

// respond with "hello world" when a GET request is made to the homepage
app.post('/explore', function(req, res) {
    console.log('boo');
    // Replace double quotes with single quotes
    var body = JSON.stringify(req.body).replace(/"/g, "'");
    console.log('body in handler = ');
    console.log(body);
    getResponse(body).then(function(data) {
        console.log('here');
        res.end(data);
    });
});

function getResponse(body) {
    if(body.length == 0) {
        body = "[[]]";
    }
    var deferred = q.defer();
    var cmd = 'echo "' + body + '" | python ../backend/course_selection_engine/CoursePathPredictor.py';
    console.log('command = ' + cmd);
    child_process.exec('echo "' + body + '" | python ../backend/course_selection_engine/CoursePathPredictor.py',
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
