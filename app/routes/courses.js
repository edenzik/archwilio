var express = require('express');
var models  = require('../models');
var router = express.Router();

/* GET courses listing. */
router.get('/', function(req, res) {
  models.course.findAll({limit: 10}).then(function(courses) {
    res.setHeader('Content-Type', 'application/json');
    res.send(JSON.stringify(courses));
  });

  // res.setHeader('Content-Type', 'application/json');
  // res.send(JSON.stringify({}));
});

module.exports = router;
