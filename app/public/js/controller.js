var ClassPathExploration = angular.module('ClassPathExploration', []);

ClassPathExploration.controller('Course', function ($scope, $http) {
    /*$scope.courses = [
    {'name': 'Distributed system'},
    {'name': 'Mobile application'},
    {'name': 'Software entrpreneurship'}*/

    // change the address below 
    $http.get('/courses').success(function(data) {
    $scope.courses = data;
  });
});