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
