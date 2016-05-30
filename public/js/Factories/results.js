app.factory('Result', ['$http', function($http){
  var r = {};

  r.getResult = function(id){
    return $http.get('/api/results/query?name=' + id);
  }

  return r;
}]);
