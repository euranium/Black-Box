/*
Controller for the running state
This state appears when the user tries to view a result that is still processing
On load attempts to query the database for the result object
If the object exists, go to the result state
else, do nothing
*/
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
