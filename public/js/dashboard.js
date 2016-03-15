var app = angular.module('dashboard', []);

app.controller('MainCtrl', [
'$scope',
'$http',
function($scope, $http){
  $scope.test = 'Hello world!';
  $scope.software = [];
  $scope.content = "<h1>Dashboard</h1><p>This area will be populated with data from the menu item selected on the left</p>";

    $http.get('/api/listsoftware').success(function(data){
      angular.copy(data, $scope.software);
    });

    $scope.loadSoftware = function(name){

      $http.get('/api/template/query?name=' + name).success(function(data){
        angular.copy(data, $scope.content);
      });

      console.log($scope.content);

    };



}]);
