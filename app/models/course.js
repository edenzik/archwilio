"use strict";

module.exports = function(sequelize, DataTypes) {
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