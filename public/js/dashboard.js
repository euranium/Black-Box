/*
Dashboard.js
Runs the "front end" of the dashboard
Dynamically populates the dashboard page
*/

var app = angular.module('dashboard', ['ngSanitize', 'chart.js']);

//Wrapper directive to wrap graph and file results
/*obj is of form
  {
    files: [{}],
    graphs: [{}]
  }
*/
app.directive('result', function() {
    return {
        restrict: 'E',
        scope: {
          obj: '='
        },
        templateUrl: '/html/result.html'
    };
});

app.directive('file', function() {
    return {
        restrict: 'E',
        scope: {
          obj: '=',
          index: '='
        },
        templateUrl: '/html/file.html'
    };
});

app.controller('MainCtrl', [
  '$scope',
  '$http',
  '$compile',
  '$sce',
  function($scope, $http, $compile, $sce) {

    ////////////////////////////////////////////////////////////////////////////
    //Initialization
    ////////////////////////////////////////////////////////////////////////////
    $scope.software = [];
    $scope.results = [];
    $scope.result = {};

    //hard coded for now, parse from url in future
    var prog = "modEvo"

    //Call backend to get list of all software----------------*/
    $http.get('/api/listsoftware').success(function(data) {
      $scope.software = data;
    });

    //Call backend to get list of all results----------------*/
    $http.get('/api/results').success(function(data) {
      angular.copy(data, $scope.results);
    });

    ////////////////////////////////////////////////////////////////////////////
    //Angular Functions
    ////////////////////////////////////////////////////////////////////////////

    //Charting utility function--------------------------------*/
    $scope.onClick = function(points, evt) {
      console.log(points, evt);
    };

    //Loads the submission template for the given software-----*/
    //compiles the returned data to the dom--------------------*/
    $scope.loadSoftware = function(name) {
      $http.get('/api/template/query?name=' + name).success(function(data) {
        $('#dash').html($compile(data)($scope)); //$sce.trustAsHtml(data);
      });
    };

    //Sends the form data to the server to compute a result--*/
    //Gets called when the submit button is clicked----------*/
    //Builds argument list from inputs with 'sending' class--*/
    $scope.send = function(name) {
      console.log("sending for prog: " + name);

      args = getInput();

      obj = {
        Name: name,
        Input: args,
      };

      $http({
          method: 'POST',
          url: 'api/submit',
          data: JSON.stringify(obj),
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
          }
        })
        .success(function(data) {
          console.log("Data sent");
        });
    }

    //Gets called when a result tab is clicked---------------*/
    //Calls the back end api, which returns the files--------*/
    //Parse the files, and compile them to the dom-----------*/
    //Name is the string seen on the dashboard menu with date*/
    $scope.loadResult = function(name) {
      $http.get('/api/results/query?name=' + name).success(function(data) {
        $scope.result = htmlify(data, "modEvo");
        $('#dash').html($compile("<result obj='result'></result>")($scope));
      });
    };



  }
]);
