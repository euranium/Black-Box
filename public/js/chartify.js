/*
Chartify.js
Takes in a file in data format and returns a 'chart object'
Called from htmlify.js
*/

//Driver function that gets called from htmlify.js---------------------*/
//Returns the correct chart object based on prog name------------------*/
function chartify(data, prog) {

  //currently only one program, extend if statement in future
  if (prog == "modEvo") {
    return modEvoChart(data);
  }

}


//Creates a chart object for modEvo------------------------------------*/
function modEvoChart(data){
  console.log(data);
  result = {};
  result.name = data.Name;
  result.labels = []; //lables are the axis
  result.series = []
  result.info = [];
  result.opt = {
    bezierCurve: false,
    showXLabels: 25,
    responsive: true
  };


  //Add file names to series
  result.series.push(data.Results[3].Name);
  result.series.push(data.Results[4].Name);

  //loop over first file to get lables and first info set
  var lines = splitByLine(data.Results[3].Data);
  var s1 = []
  for (var i = 1; i < lines.length; i++) {
    var parts = lines[i].split(" ");
    result.labels.push(parts[0]);
    s1.push(parts[1]);
  }

  //loop over second file to get second info set
  var lines = splitByLine(data.Results[4].Data);
  var s2 = []
  for (var i = 1; i < lines.length; i++) {
    var parts = lines[i].split(" ");
    s2.push(parts[1]);
  }

  result.info.push(s1);
  result.info.push(s2);

  return result;
}
