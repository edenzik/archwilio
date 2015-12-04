var express = require('express');
var app = express();
var bodyParser = require('body-parser');

app.use(express.static('public'));

app.use(bodyParser.json());

// respond with "hello world" when a GET request is made to the homepage
app.post('/explore', function(req, res) {
  console.log(req.body);
});

app.listen(8000);
