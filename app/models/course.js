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

  var Course = sequelize.define('Course', {
    name: DataTypes.STRING
  });

  return Course;
};