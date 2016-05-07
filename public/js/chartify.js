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
  result = {};
  result.labels = []; //lables are the axis
  result.series = []
  result.info = [];

  //Add file names to series
  result.series.push(data.Results[0].Name);
  result.series.push(data.Results[1].Name);

  //loop over first file to get lables and first info set
  var lines = splitByLine(data.Results[0].Data);
  var s1 = []
  for (var i = 1; i < lines.length; i++) {
    var parts = lines[i].split(" ");
    result.labels.push(parts[0]);
    s1.push(parts[1]);
  }

  //loop over second file to get second info set
  var lines = splitByLine(data.Results[1].Data);
  var s2 = []
  for (var i = 1; i < lines.length; i++) {
    var parts = lines[i].split(" ");
    s2.push(parts[1]);
  }

  result.info.push(s1);
  result.info.push(s2);

  result.html = "<canvas id='line' class='chart chart-line' chart-data='stuff' chart-labels='labels' chart-legend='true' chart-series='series' chart-click='onClick' chart-options='opt'></canvas> \n\n";
  return result;
}
