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

  $scope.labels = ["January", "February", "March", "April", "May", "June", "July"];
  $scope.series = ['Series A', 'Series B'];
  $scope.stuff = [[65, 59, 80, 81, 56, 55, 40],[28, 48, 40, 19, 86, 27, 90]];

  $scope.onClick = function (points, evt) {
    console.log(points, evt);
  };

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
      obj.Name = name;
      obj.Input = args;

      $http({
          method  : 'POST',
          url     : 'api/submit',
          data    : JSON.stringify(obj),
          headers : {'Content-Type': 'application/x-www-form-urlencoded'}
         })
          .success(function(data) {
            console.log("it worked");
          });$scope.labels = ["January", "February", "March", "April", "May", "June", "July"];
  $scope.series = ['Series A', 'Series B'];
  $scope.data = [
    [65, 59, 80, 81, 56, 55, 40],
    [28, 48, 40, 19, 86, 27, 90]
  ];
  $scope.onClick = function (points, evt) {
    console.log(points, evt);
  };
    }

    $scope.loadResult = function(name){
      var result;
      $http.get('/api/results/query?name=' + name).success(function(data){
          var result = "<h2>" + data.Name + "</h2>\n";
          result = result + chartify(data);
          var items = data.Results;
          for(var i = 0; i < items.length; i++){
            var temp = "<div class=\"list-group\">";
            temp = temp + "<div class=\"list-group-item disabled\" style=\"cursor:default;\"> <h4>" + items[i].Name + "</h4> </div>"

            var lines = splitByLine(items[i].Data);

            for(var j = 0; j < lines.length; j++){
              console.log(lines[j]);
              temp = temp + "<div class=\"list-group-item\">" + lines[j] + "</div>"
            }



            temp = temp + "</div>"
            result = result + temp;
          }
            $('#dash').html($compile(result)($scope));//$('#dash').html(result);
      });
    };



}]);

function splitByLine(data){
  return data.split("\n").filter(Boolean);
}

function chartify(data){
  return "<canvas id=\"line\" class=\"chart chart-line\" chart-data=\"stuff\" chart-labels=\"labels\" chart-legend=\"true\" chart-series=\"series\"chart-click=\"onClick\" ></canvas>";
}
