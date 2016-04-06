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

    console.log($scope.software);

    $http.get('/api/results').success(function(data){
      angular.copy(data, $scope.results);
    });

    $scope.loadSoftware = function(name){
      $http.get('/api/template/query?name=' + name).success(function(data){
        console.log(data);
        $('#dash').html($compile(data)($scope));//$sce.trustAsHtml(data);
      });


    };

    $scope.send = function(name){
      console.log(name);
      var allInputs = $( ":input" );
      var args = [];

      for(var i = 0; i < allInputs.length; i++){
        console.log(allInputs[i].value);
        args.push(allInputs[i].value);
      }

      $http.post('api/submit/query?name=' + name + "&type=java&sorted=true").success(function(data){
          console.log("it worked");
      });

      //action="api/submit/query?name=ModEvo&type=java&sorted=true" method="post"

    }

    $scope.loadResult = function(name){

      // $http.get('/api/template/query?name=' + name).success(function(data){
      //   console.log(data);
      //   $('#dash').html(name);//$sce.trustAsHtml(data);
      // });

      $http.get('/api/results/query?name=' + name).success(function(data){
          $('#dash').html(data);//$sce.trustAsHtml(data);
      });



    };



}]);
