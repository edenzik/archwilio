var ClassPathExploration = angular.module('ClassPathExploration', ['ui.bootstrap']);

/*ClassPathExploration.controller('Course', function ($scope, $http) {
    /*$scope.courses = [
    {'name': 'Distributed system'},
    {'name': 'Mobile application'},
    {'name': 'Software entrpreneurship'}*/


ClassPathExploration.controller('CourseCtrl', function ($scope, $http, $modal, $log) {
    //$scope.animationsEnabled = true;
    $http.get('http://localhost:3000/courses').success(function(data) {
    //$http.get('http://localhost/data_format.json').success(function(data) {
    $scope.courses = data;
    });
    $scope.open = function (_course,size) {
      var modalInstance = $modal.open({
        //animation: $scope.animationsEnabled,
        templateUrl: 'myModalContent.html',
        controller: 'ModalInstanceCtrl',
        size: size,
        resolve: {
                  course: function()
                  {
                      return _course;
                  }
              }
      	});
  	};
});

ClassPathExploration.controller('ModalInstanceCtrl', function ($scope, $uibModalInstance, $http, course) {
  $scope.course = course;

  $scope.train = function(rating) {
    data = {
      instance_id: $scope.course.instance_id,
      rating: $scope.rating
    };

    $http.post('http://localhost:3000/courses/train', data).success(function() {});
  }
});
