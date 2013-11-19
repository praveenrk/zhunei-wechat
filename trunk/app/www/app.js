var app = new $.mvc.app();

app.loadModels(["article"]);
app.loadControllers(["article"]);
//Routing by hash change
//app.listenHashChange();

//We wait until app.ready is available to fetch the data, then we wire up the existing data in the templates
app.ready(function(){
    $.mvc.route("/article");//Load the default todo route
});