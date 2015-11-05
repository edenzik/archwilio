var models  = require('../models');
var express = require('express');
var router = express.Router();

/* GET home page. */
router.get('/', function(req, res, next) {
  
  res.render('index', { title: 'Express' });

}).get('/courses', function(req, res) {
  
  models.Course.findAll({
    include: [ models.Course ]
  }).then(function(users) {
    res.render('index', {
      title: 'Course',
      course: courses
    });
  });
});

module.exports = router;
