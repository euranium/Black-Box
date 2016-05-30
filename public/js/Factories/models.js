app.factory('Model', ['$http', function($http){
  var m = {};

  m.getModel = function(id){
    return $http.get('/api/template/query?name=' + id);
  }

  return m;
}]);
