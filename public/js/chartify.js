/*
Chartify.js
Takes in a file in data format and returns a 'chart object'
Called from htmlify.js
*/

//Driver function that gets called from htmlify.js---------------------*/
//Returns the correct chart object based on prog name------------------*/
function chartify(data) {

  //currently only one program, extend if statement in future
  if (data.Name == "ModEvo_Model-1") {
    return modEvoChart1(data);
  }

}


//Creates a chart object for modEvo------------------------------------*/
function modEvoChart1(data){

  var graphs = [];

  result1 = {};
  result1.name = "Mean Trait Values";
  result1.labels = []; //lables are the axis
  result1.series = []
  result1.info = [];
  result1.opt = {
    bezierCurve: false,
    showXLabels: 25,
    responsive: true
  };

  console.log(data.Results[4].Data);

  data.Results[4].Data = bin2String(data.Results[4].Data);
  data.Results[5].Data = bin2String(data.Results[5].Data);




  //Add file names to series
  result1.series.push(data.Results[4].Name);
  result1.series.push(data.Results[5].Name);

  //loop over first file to get lables and first info set
  var lines = splitByLine(data.Results[4].Data);
  var s1 = []
  for (var i = 1; i < lines.length; i++) {
    var parts = lines[i].split(" ");
    result1.labels.push(parts[0]);
    s1.push(parts[1]);
  }

  //loop over second file to get second info set
  var lines = splitByLine(data.Results[5].Data);
  var s2 = []
  for (var i = 1; i < lines.length; i++) {
    var parts = lines[i].split(" ");
    s2.push(parts[1]);
  }

  result1.info.push(s1);
  result1.info.push(s2);

  graphs.push(result1);

  result2 = {};
  result2.name = "Mean Fitness Values";
  result2.labels = []; //lables are the axis
  result2.series = []
  result2.info = [];
  result2.opt = {
    bezierCurve: false,
    showXLabels: 25,
    responsive: true
  };

  //Add file names to series
  result2.series.push(data.Results[3].Name);

  //loop over first file to get lables and first info set
  var lines = splitByLine(data.Results[3].Data);
  var s3 = []
  for (var i = 1; i < lines.length; i++) {
    var parts = lines[i].split(" ");
    result2.labels.push(parts[0]);
    s3.push(parts[1]);
  }

  result2.info.push(s3);
  graphs.push(result2);


  return graphs;
}
