/*
Controller for the sidebar
Populates the navbar by querying the back end
Responsible for changing states on menu item clicks
*/
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

    //Call backend to get list of all software----------------*/
    $http.get('/api/listsoftware').success(function(data) {
      $scope.software = data;
    });

    //Call backend to get list of all results----------------*/
    $http.get('/api/results').success(function(data) {
      if(data != "null"){
        $scope.results = data;
      }
      else{
        $scope.results = [];
      }
    });

    //Charting utility function--------------------------------*/
    $scope.onClick = function(points, evt) {
      console.log(points, evt);
    };

    //Loads the submission template for the given software-----*/
    //compiles the returned data to the dom--------------------*/
    $scope.loadSoftware = function(name, event) {
      fixSelection($(event.target));
      $location.path("/model/" + name);
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

function fixSelection(element){
  $(".selected").each(function(i, obj){
    if($(obj).hasClass("selected")){
      $(obj).removeClass("selected");
    }
  })

  element.addClass("selected");
}
