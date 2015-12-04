var express = require('express');
var app = express();
var bodyParser = require('body-parser');
var q = require('q');
var child_process = require('child_process');

app.use(express.static('public'));

app.use(bodyParser.json());

// respond with "hello world" when a GET request is made to the homepage
app.post('/explore', function(req, res) {
    var body = JSON.stringify(req.body);
    console.log('body in handler = ');
    console.log(body);
    getResponse(body).then(console.log);
});

function getResponse(body) {
    if(body.length == 0) {
        console.log('SLEEP');
        body = "[[]]";
    }
    var deferred = q.defer();
    console.log('body in get response = ' + body);
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
