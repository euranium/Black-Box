/*
Dashboard.js
Runs the "front end" of the dashboard
This file registers the 'app' variable that runs the angular app
Controllers and Factories are seperated into different files
*/

var app = angular.module('dashboard', ['ngSanitize', 'ui.router', 'chart.js']);

/*
Configure states for the app
Each state has a template html file
Each state, except default, has a controller
*/
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

      $stateProvider
        .state('error', {
          url: '/error',
          templateUrl: '/html/error.html',
        });

        $stateProvider
          .state('running', {
            url: '/running/{id}',
            controller: 'Running',
            templateUrl: '/html/running.html'
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
            $event.currentTarget.href = makeTextFile(scope.obj.data);
          }
        }
    };
});
