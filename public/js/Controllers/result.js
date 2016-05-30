/*
Controller for the result state
Deals with parsing the data recieved from the server
calls to parsify to keep the code here concise
*/
app.controller('ResultCtrl', [
'$scope',
'$stateParams',
'$http',
'$state',
'Result',
function($scope, $stateParams, $http, $state, Result){

  $scope.obj = {}
  $scope.message = "";

  Result.getResult($stateParams.id)
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
