var ClassPathExploration = angular.module('ClassPathExploration', ['ui.bootstrap']);

/*ClassPathExploration.controller('Course', function ($scope, $http) {
    /*$scope.courses = [
    {'name': 'Distributed system'},
    {'name': 'Mobile application'},
    {'name': 'Software entrpreneurship'}*/

   /* // change the address below 
    $http.get('http://localhost/courses.json').success(function(data) {
    $scope.courses = data;
  });
});*/

ClassPathExploration.controller('CourseCtrl', function ($scope, $http, $modal, $log) {
    //$scope.animationsEnabled = true;
    $http.get('http://localhost/courses.json').success(function(data) {
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

ClassPathExploration.controller('ModalInstanceCtrl', function ($scope, $uibModalInstance, course) {
	$scope.course = course;
});