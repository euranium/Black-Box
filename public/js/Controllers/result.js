app.controller('ResultCtrl', [
'$scope',
'$stateParams',
'$http',
'$state',
function($scope, $stateParams, $http, $state){

  $scope.obj = {}
  $scope.message = "";
  $http.get('/api/results/query?name=' + $stateParams.id)
    .success(function(data) {
      if(data.hasOwnProperty('Error')){
        if(data.Error == "Program Not Complete"){
          $state.go("running", {"id": $stateParams.id});
        }
        else{
          $state.go("error");
        }
      }
      else{
        console.log(data);
        $scope.obj = parse(data, $stateParams.id);
      }
    });


}]);
