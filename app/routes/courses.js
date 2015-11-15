var express = require('express');
var models  = require('../models');
var router = express.Router();
var sequelize = models.sequelize

router.get('/', function(req, res) {
  models.course.findAll(
    { limit: 1000,
      attributes: [
        'code', 'name', 'term', 'description', 
        sequelize.fn('count', sequelize.col('id'))], 
      where: {
        code: {
          $like: 'COSI%'
        },
        term: {
          $gte: '1152'
        }
      },
      group: ['code', 'name', 'term', 'description'],
      order: 'name ASC'
    }).then(function(courses) {

    res.setHeader('Content-Type', 'application/json');
    res.send(JSON.stringify(courses));
  });
});

module.exports = router;
