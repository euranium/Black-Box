/*
Htmlify.js
Handles creating and gathering html content from dashboard
Called from dashboard.js
*/

//Driver function that gets called from dashboard.js-------------------*/
//Returns an object with file and chart data---------------------------*/
/*
{
  files:[]
  charts: []
  images: []
}
*/
function htmlify(data, prog){


  if(prog == "modEvo"){
    return modEvoHtml(data, prog);
  }

}


function modEvoHtml(data, prog, $base){
  var rt = {
    files: [],
    graphs: [],
    image: {},
  }


  //build chart object and push to array
  //right now chartify returns an object
  //need to make it return an array to be compatible with future programs
  rt.graphs = chartify(data);

  var bytes = [];

  for (var i = 0; i < data.Results[2].Data.length; ++i) {
    bytes.push(data.Results[2].Data.charCodeAt(i));
}

  data.Results[2].Data = base64ArrayBuffer(bytes);

  rt.image = data.Results[2];


  //loop over every file returned to build files array
  //index is used to apply unique id's to elements in the directive
  // var items = data.Results;
  // for (var i = 0; i < items.length; i++) {
  //   if(items[i].Name != "contourPlot.png"){
  //     var temp = {}//"<div class='list-group'>";
  //     temp.index = i;
  //     temp.name = items[i].Name;
  //     temp.data = splitByLine(items[i].Data);
  //     rt.files.push(temp);
  //   }
  //   else{
  //     alert(i);
  //   }
  // }
  return rt;
}

//Gets all values from input fields with 'sending' class---------------*/
//Filters out empty options, and returns the array---------------------*/
function getInput(){
  var allInputs = $(".sending");
  var args = [];

  for (var i = 0; i < allInputs.length; i++) {
    var string = allInputs[i].id;
    string = string + "=";
    string = string + allInputs[i].value;
    args.push(string);
  }
  args.push("distributionSelection=default")

  args = args.filter(Boolean);
  return args;
}

//Utility function-----------------------------------------------------*/
function splitByLine(data) {
  return data.split("\n").filter(Boolean);
}
