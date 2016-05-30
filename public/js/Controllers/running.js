app.controller('Running', [
'$scope',
'$stateParams',
'$http',
'$state',
function($scope, $stateParams, $http, $state){

  $http.get('/api/results/query?name=' + $stateParams.id)
    .success(function(data) {
      if(data.hasOwnProperty('Error')){
        if(data.Error == "Program Not Found"){
            $state.go("error");
        }
      }
      else{
        $state.go("result", {"id": $stateParams.id})
      }
    });


}]);
