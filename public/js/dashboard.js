/*
Dashboard.js
Runs the "front end" of the dashboard
Dynamically populates the dashboard page
*/

var app = angular.module('dashboard', ['ngSanitize', 'ui.router', 'chart.js']);

app.config([
'$stateProvider',
'$urlRouterProvider',
function($stateProvider, $urlRouterProvider) {

  $stateProvider
    .state('default', {
      url: '/default',
      templateUrl: '/html/default.html'
    });

  $stateProvider
    .state('result', {
      url: '/result/{id}',
      templateUrl: '/html/result.html',
      controller: 'ResultCtrl'
    });

    $stateProvider
      .state('model', {
        url: '/model/{id}',
        controller: 'ModelCtrl'
      });

  $urlRouterProvider.otherwise('default');
}]);

app.directive('file', function() {
    return {
        restrict: 'E',
        replace: false,
        scope: {
          obj: '=',
          index: '='
        },
        templateUrl: '/html/file.html',
        link: function(scope, element, attrs) {
          scope.download = function($event){
            //console.log($event.currentTarget);
            $event.currentTarget.href = makeTextFile(scope.obj.data);
            //console.log(scope.obj.data);
          }
        }
    };
});

app.controller('ResultCtrl', [
'$scope',
'$stateParams',
'$http',
function($scope, $stateParams, $http){

  $scope.obj = {}
  $scope.message = "";
  $http.get('/api/results/query?name=' + $stateParams.id)
    .success(function(data) {
      if(data.hasOwnProperty('Error')){
        $scope.message = "Invalid url ¯\\\_(ツ)_/¯";
        $scope.error = 1;
      }
      else{
        console.log(data);
        $scope.obj = parse(data, $stateParams.id);
      }
    });


}]);

//Controller that opperates when the user selects a model from the side menu
app.controller('ModelCtrl', [
'$scope',
'$rootScope',
'$stateParams',
'$http',
'$compile',
function($scope, $rootScope, $stateParams, $http, $compile){

  $http.get('/api/template/query?name=' + $stateParams.id).success(function(data) {
    if(data.hasOwnProperty('Error')){
      var error = "<div class='alert alert-danger' role='alert'><span style='font-size:20px; font-weight: bold;'><i class='fa fa-exclamation-triangle' aria-hidden='true'></i>Invalid url ¯\\\_(ツ)_/¯</span></div>";
      $('#dash').html($compile(error)($scope));
      $scope.error = 1;
    }
    else{
      $('#dash').html($compile(data)($scope));
    }
  });

  $scope.working = false;

  //for modevo example graph in form
  $scope.types = [1, 2, 3];
  $scope.graphType = 1;
  $scope.max = 1;
  $scope.slope = 1;
  $scope.shift = 0;
  var g = buildData($scope.max, $scope.shift, $scope.slope);
  $scope.labels = g.labels;
  $scope.series = ['Function 1'];
  $scope.data = g.data;
  $scope.opt = {
    bezierCurve: false,
    showXLabels: 25,
    responsive: true
  };

   $scope.onClick = function (points, evt) {
     console.log(points, evt);
   };

   $scope.loadData = function(){
     if($scope.graphType == 1){
       $scope.max = 1;
       $scope.slope = 1;
       $scope.shift = 0;
       $scope.data = buildData($scope.max, $scope.shift, $scope.slope).data;
     }
     else if($scope.graphType == 2){
       $scope.max = 1;
       $scope.slope = 4;
       $scope.shift = 0;
       $scope.data = buildData($scope.max, $scope.shift, $scope.slope).data;
     }
     else{
       var a = typeof $scope.max;
       var b = typeof $scope.slope;
       var c = typeof $scope.shift;
       if(a == 'number' && b == 'number' && c == 'number'){
         $scope.data = buildData($scope.max, $scope.shift, $scope.slope).data;
       }
     }
   }

   //Sends the form data to the server to compute a result--*/
   //Gets called when the submit button is clicked----------*/
   //Builds argument list from inputs with 'sending' class--*/
   $scope.send = function(name) {
     if($scope.working){
       return;
     }
     $scope.working = true;
     console.log("sending for prog: " + name);

     args = getInput();
     console.log(args);

     obj = {
       Name: name,
       Commands: [{
         Program: "ModEvo",
         Input: args
       },
       {
         Program: "plot.py",
         input: []
      },
      {
        Program: "moveImage.sh",
        input: []
      }
     ]
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
         console.log(data.Name);
         console.log("Data sent");
         $rootScope.$broadcast('lemonade', 1);
         $scope.working = false;
       })
       .error(function(data){
         console.log("error!");
       });

   }



}]);

//Controller that opperates the side menu
app.controller('SideBarCtrl', [
  '$scope',
  '$rootScope',
  '$http',
  '$compile',
  '$sce',
  '$interval',
  '$location',
  function($scope, $rootScope, $http, $compile, $sce, $interval, $location) {


    ////////////////////////////////////////////////////////////////////////////
    //Initialization
    ////////////////////////////////////////////////////////////////////////////
    $scope.software = [];
    $scope.results = [];
    $scope.result = {};

    //$scope.example.data = buildData();
    $scope.prog = "modEvo"; //hard coded for now, parse from url in future

    //Call backend to get list of all software----------------*/
    $http.get('/api/listsoftware').success(function(data) {
      $scope.software = data;
    });

    //Call backend to get list of all results----------------*/
    $http.get('/api/results').success(function(data) {
      if(data != "null"){
        $scope.results = data;
        console.log(data);
      }
      else{
        $scope.results = [];
      }
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
    $scope.loadSoftware = function(name, event) {
      fixSelection($(event.target));
      $location.path("/model/" + name);

      // $http.get('/api/template/query?name=' + name).success(function(data) {
      //   $('#dash').html($compile(data)($scope)); //$sce.trustAsHtml(data);
      // });
    };

    //Gets called wheend api, which returns the files--------*/
    //Parse the files, and compile them to the dom-----------*/
    //Name is the string seen on the dashboard menu with date*/
    $scope.loadResult = function(name, event) {
      fixSelection($(event.target));
      $location.path("/result/" + name);
    };

    $rootScope.$on('lemonade', function(event, msg){
        $http.get('/api/results').success(function(data) {
          if(data != "null"){
            $scope.results = data;
          }
          else{
            $scope.results = [];
          }
        });
    });

  }
]);


//Non angular functions
function fixSelection(element){
  $(".selected").each(function(i, obj){
    if($(obj).hasClass("selected")){
      $(obj).removeClass("selected");
    }
  })

  element.addClass("selected");
}

//returns an object with data field and labels field
//used for the example charts in model submission forms.
function buildData(max, shift, slope){
  var final = {};
  var data = [];
  var outer = []
  var labels = []


  for(var i = -8.0; i <= 8.0; i+= 0.5){
    labels.push(String(i));
    var ans = max/(1+Math.exp((-1*(i-shift))*slope)); //1/(1+Math.exp(c*i));
    data.push(ans)
  }

  outer.push(data);
  final.data = outer;
  final.labels = labels;
  return final;
}

//Gets used for creating download links
makeTextFile = function (text) {
    textFile = null;
    var data = new Blob([text], {type: 'text/plain'});

    // If we are replacing a previously generated file we need to
    // manually revoke the object URL to avoid memory leaks.
    if (textFile !== null) {
      window.URL.revokeObjectURL(textFile);
    }

    textFile = window.URL.createObjectURL(data);

    return textFile;
  };
