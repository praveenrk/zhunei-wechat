var app = new $.mvc.app();

app.loadModels(["stuff"]);
app.loadControllers(["stuff"]);
//Routing by hash change
//app.listenHashChange();

//We wait until app.ready is available to fetch the data, then we wire up the existing data in the templates
app.ready(function(){
    $.mvc.route("/stuff");//Load the default todo route
});
