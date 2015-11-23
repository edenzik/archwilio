var express = require('express');
var models  = require('../models');
var router = express.Router();
var sequelize = models.sequelize

router.get('/', function(req, res) {
  models.course.findAll(
    { limit: 1000,
      attributes: [
        'instance_id', 'code', 'name', 'term', 'description', 
        sequelize.fn('count', sequelize.col('id'))], 
      where: {
        code: {
          $like: 'COSI%'
        },
        term: {
          $gte: '1152'
        }
      },
      group: ['code', 'name', 'term', 'description', 'instance_id'],
      order: 'name ASC'
    }).then(function(courses) {

    res.setHeader('Content-Type', 'application/json');
    res.send(JSON.stringify(courses));
  });
});

router.post('/train', function(req, res) {
  var instance_id = req.body.instance_id,
      rating = 0.8;

  models.course.findOne({ limit: 1,
      attributes: ['instance_id', 'code', 'name', 'term', 'description'],
      where: {instance_id: instance_id}
    }).then(function(course) {
      console.log(instance_id);
      console.log(rating);
      
      console.log(course.dataValues.description);
      console.log(course.dataValues.code);
      console.log(course.dataValues.name);
    });


});

module.exports = router;
