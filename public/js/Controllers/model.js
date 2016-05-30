/*
Controller for the model state, where a user is fills out the form
Deals with generating data for the example graphs
Deals with collecting and building arguement list from the form
Builds and sends the back end an 'execute' object
*/
app.controller('ModelCtrl', [
'$scope',
'$rootScope',
'$stateParams',
'$http',
'$compile',
'$state',
function($scope, $rootScope, $stateParams, $http, $compile, $state){

  $http.get('/api/template/query?name=' + $stateParams.id).success(function(data) {
    if(data.hasOwnProperty('Error')){
      $state.go("error");
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
         console.log("Data sent");
         $rootScope.$broadcast('lemonade', 1); //hahaha get it
         $scope.working = false;
       })
       .error(function(data){
         console.log("error!");
       });

   }

}]);

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
