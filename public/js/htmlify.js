/*
Htmlify.js
Handles creating and gathering html content from dashboard
Called from dashboard.js
*/

//Driver function that gets called from dashboard.js-------------------*/
//Returns an object with html and chart data---------------------------*/
function htmlify(data, prog){

  if(prog == "modEvo"){
    return modEvoHtml(data, prog);
  }

}

//Needs to return an object with html string and chart data
/*
{
  html: string
  charts: []
}
*/
function modEvoHtml(data, prog){
  var rt = {
    html: "",
    charts: [],
  }

  //build chart object and push to array
  rt.charts.push(chartify(data, prog));

  //Set title bar
  var result = "<h2>" + data.Name + "</h2>\n";


  //append chart html to result string
  result = result + rt.charts[0].html;

  var items = data.Results;
  for (var i = 0; i < items.length; i++) {
    var temp = "<div class='list-group'>";
    temp = temp + "<div class='list-group-item disabled' style='cursor:default;' data-toggle='collapse' data-target='#" + i + "'> <h4>" + items[i].Name + "</h4> </div>"

    var lines = splitByLine(items[i].Data);

    temp = temp + "<div class='collapse' id='" + i + "'>";
    for (var j = 0; j < lines.length; j++) {
      temp = temp + "<div class='list-group-item'>" + lines[j] + "</div>"
    }

    temp = temp + "</div></div>"
    result = result + temp;
  }

  rt.html = result;
  return rt;
}

//Gets all values from input fields with 'sending' class---------------*/
//Filters out empty options, and returns the array---------------------*/
function getInput(){
  var allInputs = $(".sending");
  var args = [];

  for (var i = 0; i < allInputs.length; i++) {
    console.log(allInputs[i].value);
    args.push(allInputs[i].value);
  }

  args = args.filter(Boolean);
  return args;
}

//Utility function-----------------------------------------------------*/
function splitByLine(data) {
  return data.split("\n").filter(Boolean);
}
