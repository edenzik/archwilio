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
  $scope.train = function() {
    /*get element value in modal */
    var rating;
    if (document.getElementById('yes').checked) {
      var rating = document.getElementById('yes').value;
    }
    else if(document.getElementById('no').checked){
      var rating = document.getElementById('no').value;
    }
    
    var positive_reason_1 = document.getElementById('positive_reason_1').checked;
    var positive_reason_2 = document.getElementById('positive_reason_2').checked;
    var positive_reason_3 = document.getElementById('positive_reason_3').checked;
    var negative_reason_1 = document.getElementById('negative_reason_1').checked;
    var negative_reason_2 = document.getElementById('negative_reason_2').checked;
    var negative_reason_3 = document.getElementById('negative_reason_3').checked;
    /* */

    data = {
      instance_id: $scope.course.instance_id,
      rating: $scope.rating,
      positive_reason_1: $scope.positive_reason_1,
      positive_reason_2: $scope.positive_reason_2,
      positive_reason_3: $scope.positive_reason_3,
      negative_reason_1: $scope.negative_reason_1,
      negative_reason_2: $scope.negative_reason_2,
      negative_reason_3: $scope.negative_reason_3
    };

    $http.post('http://localhost:3000/courses/train', data).success(function() {});
  }
});
