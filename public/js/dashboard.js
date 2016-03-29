var app = angular.module('dashboard', ['ngSanitize']);

app.controller('MainCtrl', [
'$scope',
'$http',
'$compile',
'$sce',
function($scope, $http, $compile, $sce){
  $scope.software = [];
  $scope.results = []
  $scope.radio = "";

    $http.get('/api/listsoftware').success(function(data){
      angular.copy(data, $scope.software);
    });

    $scope.loadSoftware = function(name){
      $http.get('/api/template/query?name=' + name).success(function(data){
        console.log(data);
        $('#dash').html($compile(data)($scope));//$sce.trustAsHtml(data);
      });


    };

    $scope.send= function(){
      $scope.results.push($scope.radio);
    }

    $scope.loadResult = function(name){

      $http.get('/api/template/query?name=' + name).success(function(data){
        console.log(data);
        $('#dash').html(name);//$sce.trustAsHtml(data);
      });


    };



}]);
