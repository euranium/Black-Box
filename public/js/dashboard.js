var app = angular.module('dashboard', ['ngSanitize']);

app.controller('MainCtrl', [
'$scope',
'$http',
'$compile',
'$sce',
function($scope, $http, $compile, $sce){
  $scope.test = 'Hello world!';
  $scope.software = [];

    $http.get('/api/listsoftware').success(function(data){
      angular.copy(data, $scope.software);
    });

    $scope.loadSoftware = function(name){

      $http.get('/api/template/query?name=' + name).success(function(data){
        console.log(data);
        $('#dash').html($compile(data)($scope))//$sce.trustAsHtml(data);
      });

    $scope.send= function(){
      console.log("Hello");
    }

      //console.log($scope.content);

    };



}]);
