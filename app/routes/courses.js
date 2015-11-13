var express = require('express');
var models  = require('../models');
var router = express.Router();

/* GET courses listing. */
router.get('/', function(req, res) {
  models.Course.findAll({}).then(function(courses) {
    res.setHeader('Content-Type', 'application/json');
      res.send(JSON.stringify(courses));
    });
});

module.exports = router;
