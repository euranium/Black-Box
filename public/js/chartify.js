/*
Chartify.js
Takes in a file in data format and returns a 'chart object'
Called from htmlify.js
*/

function buildSingleGraph(file){

  result = {};
  result.name = file.Name.split(".")[0];
  result.labels = []; //lables are the axis
  result.series = []
  result.info = [];
  result.opt = {
    bezierCurve: false,
    showXLabels: 25,
    responsive: true
  };

  result.series.push(file.Name);

  //loop over first file to get lables and first info set
  var lines = splitByLine(file.Data);
  var s1 = []
  for (var i = 1; i < lines.length; i++) {
    var parts = lines[i].split(" ");
    result.labels.push(parts[0]);
    s1.push(parts[1]);
  }

  result.info.push(s1);

  return result;

}
