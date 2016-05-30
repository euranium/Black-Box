app.factory('Auth', ['$http', function($http){
  var a = {};

  a.isLoggedIn = function(){
    return $http.get('/api/loggedIn');
  }

  return a;
}]);
