var app = angular.module('dashboard', ['ngSanitize', 'chart.js']);

app.controller('MainCtrl', [
'$scope',
'$http',
'$compile',
'$sce',
function($scope, $http, $compile, $sce){
  $scope.software = [];
  $scope.results = []
  $scope.radio = "";


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
          });
    }

    $scope.loadResult = function(name){
      var result;
      $http.get('/api/results/query?name=' + name).success(function(data){

          //Set title bar
          var result = "<h2>" + data.Name + "</h2>\n";

          //build chart object
          chartInfo = chartify("modevo", data);

          //append chart html to result string
          result = result + chartInfo.html;

          var items = data.Results;
          for(var i = 0; i < items.length; i++){
            var temp = "<div class='list-group'>";
            temp = temp + "<div class='list-group-item disabled' style='cursor:default;' data-toggle='collapse' data-target='#" + i + "'> <h4>" + items[i].Name + "</h4> </div>"

            var lines = splitByLine(items[i].Data);

            temp = temp + "<div class='collapse' id='" + i + "'>";
            for(var j = 0; j < lines.length; j++){
              console.log(lines[j]);
              temp = temp + "<div class='list-group-item'>" + lines[j] + "</div>"
            }




            temp = temp + "</div></div>"
            result = result + temp;
          }

          $scope.labels = chartInfo.labels;
          $scope.series = chartInfo.series;
          $scope.stuff = chartInfo.info;
          $scope.opt = {
            bezierCurve: false
          }

          $('#dash').html($compile(result)($scope));
      });
    };



}]);

function splitByLine(data){
  return data.split("\n").filter(Boolean);
}

function chartify(prog, data){
  result = {};
  result.labels = [];
  result.series = []
  result.info = [];


  if(prog == "modevo"){

    //Add file names to series
    result.series.push(data.Results[0].Name);
    result.series.push(data.Results[1].Name);

    //loop over first file to get lables and first info set
    var lines = splitByLine(data.Results[0].Data);
    var s1 = []
    for(var i = 1; i < lines.length; i++){
      var parts = lines[i].split(" ");
      result.labels.push(parts[0]);
      s1.push(parts[1]);
    }

    //loop over second file to get second info set
    var lines = splitByLine(data.Results[1].Data);
    var s2 = []
    for(var i = 1; i < lines.length; i++){
      var parts = lines[i].split(" ");
      s2.push(parts[1]);
    }

    result.info.push(s1);
    result.info.push(s2);

  }

  result.html =  "<canvas id='line' class='chart chart-line' chart-data='stuff' chart-labels='labels' chart-legend='true' chart-series='series' chart-click='onClick' chart-options='opt'></canvas> \n\n";
  return result;
}
