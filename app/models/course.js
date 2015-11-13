"use strict";

module.exports = function(sequelize, DataTypes) {
  // var User = sequelize.define("User", {
  //   username: DataTypes.STRING
  // }, {
  //   classMethods: {
  //     associate: function(models) {
  //       User.hasMany(models.Task)
  //     }
  //   }
  // });

  var Course = sequelize.define('course', {
      id: DataTypes.STRING,
      term: DataTypes.STRING,
      instance_id: DataTypes.STRING,
      code: DataTypes.STRING,
      name: DataTypes.STRING,
      description: DataTypes.STRING,
      credits: DataTypes.STRING,
    },
    { tableName: 'course'});

  return Course;
};