/*
Parse.js
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
function parse(data, folder){

  var rt = {
    files: [],
    graphs: [],
    images: []
  }

  //loop over every file returned to build files array
  //index is used to apply unique id's to elements in the directive
  var index = 0;
  var items = data.Results;
  for (var i = 0; i < items.length; i++) {
    //items[i].Name = items[i].Name.replace("TraitOne", "Melanin");
    //items[i].Name = items[i].Name.replace("TraitTwo", "DVM");
    if(fileExtension(items[i].Name) == "txt"){
      var temp = {}
      temp.index = index;
      temp.name = items[i].Name;
      temp.data = splitByLine(items[i].Data);
      rt.files.push(temp);
      index = index + 1;

      if(data.Name == "ModEvo_Model-1"){
        if(items[i].Name.split("_")[0] != "speciesInputs"){
          rt.graphs.push(buildSingleGraph(items[i]))
        }
      }
      else if(data.Name == "ModEvo_Model-2"){
        if(items[i].Name.split(".")[0] == "meanFitnessValues_ModelTwo" || items[i].Name.split(".")[0] == "meanTraitOneValues_ModelTwo" | items[i].Name.split(".")[0] == "meanTraitTwoValues_ModelTwo"){
          rt.graphs.push(buildSingleGraph(items[i]))
        }
      }
    }

  }

  var image = {
    src: "img/gnu/" + folder + "/contourPlot.png",
    name: "Fitness Contour"
  }

  rt.images.push(image);


  return rt;

}

//Returns the file extension of a file with one "."--------------------*/
//Otherwise returns the empty string-----------------------------------*/
function fileExtension(data){
  try {
   return data.split(".")[1];
  }
  catch (e) {
     return "";
  }
}

//Gets all values from input fields with 'sending' class---------------*/
//Filters out empty options, and returns the array---------------------*/
//Gets called from Modesl Controller ----------------------------------*/
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
