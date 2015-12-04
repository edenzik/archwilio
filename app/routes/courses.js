var express = require('express');
var models  = require('../models');
var router = express.Router();
var sequelize = models.sequelize;
var http = require('http');
var request = require('request');

router.get('/', function(req, res) {
  models.course.findAll(
    { attributes: [
        'code', 'name', 'description', 'score', 
        [sequelize.fn('count', sequelize.col('instance_id')), 'count']], 
      where: {
        code: {
          $like: 'COSI%'
        },
        term: {
          $gte: '1152'
        }
      },
      group: ['code', 'name', 'description', 'score'],
      order: 'name ASC'
    }).then(function(courses) {

    console.log(courses[0]);
    res.setHeader('Content-Type', 'application/json');
    res.send(JSON.stringify(courses));
  });
});

router.post('/train', function(req, res) {
  var instance_id = '013170', //req.body.instance_id,
      rating = 0.8;

  models.course.findOne({ limit: 1,
      attributes: ['instance_id', 'code', 'name', 'term', 'description'],
      where: {instance_id: instance_id}
    }).then(function(course) {

      console.log('posting');
      request.post({
        url:'http://localhost:8000/estimators/train',
        form: {
          instance_id:instance_id,
          rating: rating,
          name: course.dataValues.name,
          description: course.dataValues.description,
          code: course.dataValues.code}},
        function optionalCallback(err, httpResponse, body) {
          if (err) {
            return console.error('upload failed:', err);
          }
          console.log('Upload successful!  Server responded with:', body);
        });
    });
});

router.get('/rebuild_model', function(req, res) {
  var instance_id = '013170', //req.body.instance_id,
      rating = 0.8;

  models.course.findAll(
    { attributes: [
        'code', 'name', 'description', 'score', 
        [sequelize.fn('count', sequelize.col('instance_id')), 'count']], 
      where: {
        code: {
          $like: 'COSI%'
        },
        term: {
          $gte: '1152'
        }
      },
      group: ['code', 'name', 'description', 'score'],
      order: 'name ASC'
    }).then(function(courses) {

      console.log('posting');
      console.log(courses[0]['dataValues']);
      data = _.map(courses, function(c) { return courses[0]['dataValues'] });
      console.log(data);

      request.post({
        url:'http://localhost:8000/estimators/rebuild_model',
        form: {courses: courses}},
        function optionalCallback(err, httpResponse, body) {
          if (err) {
            return console.error('upload failed:', err);
          }
          console.log('Upload successful!  Server responded with:', body);
        });
    });
});

module.exports = router;
