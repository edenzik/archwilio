var express = require('express');
var models  = require('../models');
var router = express.Router();

/* GET users listing. */
router.get('/', function(req, res) {
  models.Course.findAll({}).then(function(courses) {
    res.render('index', {
      title: 'Course',
      course: '1'
    });
  });
});

module.exports = router;
