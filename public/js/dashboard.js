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
      console.log("sending for prog: " + name);
      var allInputs = $( ":input" );
      var args = [];

      for(var i = 0; i < allInputs.length; i++){
        console.log(allInputs[i].value);
        args.push(allInputs[i].value);
      }

      args = args.filter(Boolean);

      obj = {};
      obj.name = name;
      obj.input = args;

      console.log(JSON.stringify(obj));

      $http({
          method  : 'POST',
          url     : 'api/submit',
          data    : JSON.stringify(obj), 
          headers : {'Content-Type': 'application/x-www-form-urlencoded'}
         })
          .success(function(data) {
            console.log("it worked");
          });


      // $http.post('api/submit' + JSON.stringify(obj)).success(function(data){
      //     console.log("it worked");
      // });

    }

    $scope.loadResult = function(name){
      $http.get('/api/results/query?name=' + name).success(function(data){
          $('#dash').html(data);
      });
    };



}]);
