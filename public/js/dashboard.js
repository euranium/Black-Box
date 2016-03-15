var app = angular.module('dashboard', []);

app.controller('MainCtrl', [
'$scope',
'$http',
function($scope, $http){
  $scope.test = 'Hello world!';
  $scope.software = []

    $http.get('/api/listsoftware').success(function(data){
      angular.copy(data, $scope.software);
    });




}]);
