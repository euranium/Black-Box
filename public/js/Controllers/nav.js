/*
The controller for the nav background
Uses the Auth factory to see if the user is logged in
*/
app.controller('Nav', [
'$scope',
'$stateParams',
'$http',
'$state',
'Auth',
function($scope, $stateParams, $http, $state, Auth){

  $scope.user = {};

  Auth.isLoggedIn().success(function(data){
    $scope.user = data;
  });

}]);
