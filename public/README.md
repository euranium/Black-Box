# FrontEnd

This file details how the front end of the ModEvo is laid out and opperates. In general, the front end works by first parsing the url to decidr which state to enter. Then, each state will load it's corresponding template file or in the case of the models state, dynamicly add html to the page directly.

---
## Public/bower_components

This folder contains the charting libraries used by the dashboard to visualize data, as well as all the dependencies for the plugin. This folder was gnerated with Bower.

---

## Public/js

This folder conatins the various javascript fiels that run the bront end of the dashbaord page. It is split into controllers, factories, and various other files.

### Public/js/Dashboard.js

Angular works by declaring a global app variable, and assigning that variable various components, such as controllers and states. In this file, we declare the our app as a globabl varialbe called app for an application named 'dashbaord'.
```javascript
var app = angular.module('dashboard', ['ngSanitize', 'ui.router', 'chart.js']);
```
When declaring the app, we specificy in the array arguement that the app uses external resources, in this case ngSanatize, which is used to sanatize html being rendered to the page, ui.router, which is what we used to create states, and chart.js which is used to create the various charts in the result state.

In this file, we also declare the file directive, which tells angular how it should process the file tag when it apepars in html.

### Public/js/Parser.js

This file is used by the result state to build teh data object that the result.html template loops over to populate the page. parse() takes data, which is the data returned by ther server, and folder, which is a string representing the folder containing the result data. This function also calls chartify, which is used to create chart objects for the result template.

This file also contains the getInput(), ehich is used to build the arguement that is passed to the back end on submission of a job.

### Public/js/Chartify.js

This file contains functions that take in a file from the back end and returns a graph object representing that file.

### Public/js/Download.js

This file contains functions that are used to convert string data into a downloadable txt file. It gets called in the file directive.

## Public/js/Controlelrs

This directory contains various controller files that define how a state should operate. These controllers are defined in dashboard.js, but are implemented here.

## Public/js/Factories

This directory contains various factory files. In Angular js, factories are used to separate responsibility to different parts of the code. In this case, auth.js models.js, and results.js all contain functions that deal with communicating with the back end.

---

## Public/css

This folder contains various css files used to style the site and dashboard pages. Bootstrap.min.css is used globally throughout the site and is overwritten by style.css and dashboard.css.

Dashboard.css is used specifically for the dashboard page, while style.css are used for the other various pages such as people.html and publications.html.

---

## Public/html

This directory contains the various dashboard templates defined in pulbic/js/dashboard.js. These files are injected into the dashboard in the div labeled ui-view.

```html
<div id="dash" ui-view>

</div>
```

### Public/html/Default.html
This tempalte is used for the default starting state of the app.

### Public/html/Error.html
This template is used when a front end controller receives an object from the back end with an error field, and such is displayed when there is an error.

### Public/html/File.html
This is a template file for the file directive and defines how the file tag is displayed. This directive gets called from Result.html

### Public/html/Header.html
This template defines the html for the header in non dashboard pages,such as people.html

### Public/html/Result.html
This file defines the view of the result state. It creates a grid of objects, including graphs files and images. It is dependent on a data object "obj" which is used to store all the graph image, and file objects in arrays. The result template works by simply looping over these arrays to populate the page.

### Public/html/Running.html
This template is used in the running state. It has a loading bar and tells the user that their submission is still running on the server.

