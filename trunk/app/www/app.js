var app = new $.mvc.app();

app.loadModels(["stuff"]);
app.loadControllers(["stuff"]);
//Routing by hash change
//app.listenHashChange();

//We wait until app.ready is available to fetch the data, then we wire up the existing data in the templates
app.ready(function(){
    $.mvc.route("/stuff");//Load the default todo route
});


function CathAssistDB(){
	this.db = openDatabase("CathAssist", "0.9", "database for cathassist.org", 200000);
	this.server = "http://cathassist.org/";
	
	if(!this.db)
		return null;
	this.db.transaction(function(tx){
		tx.executeSql('CREATE TABLE IF NOT EXISTS stuff (date unique,mass,med,comp,let,lod,thought,ordo,ves,saint)');
	});
}
CathAssistDB.prototype = {
    getStuff:function(date,type,callback){
		if(!this.db)
			return null;
		this.db.transaction(function(tx){
			tx.executeSql('select mass,med,comp,let,lod,thought,ordo,ves,saint from stuff where date=?', [date], function(tx,r){
				if(r.rows.length>0)
				{
					if(callback)
					{
						if(type=="" || (!type))
							callback(r.rows.item(0));
						else
							callback(r.rows.item(0)[type]);
					}
				}
				else
				{
					$.getJSON(localDB.server+"getstuff/getstuff.php?date="+date+"&type=jsonp&callback=?",
						function(obj){
							localDB.setStuff(date,obj);
							if(callback)
							{
								if(type=="" || (!type))
									callback(obj);
								else
									callback(obj[type]);
							}
						}
					);
				}
			});
		});
    },
	setStuff:function(date,data){
		if(!this.db)
			return null;
		this.db.transaction(function(tx){
			tx.executeSql('delete from stuff where date=?', [date]);
			tx.executeSql('insert into stuff(date,mass,med,comp,let,lod,thought,ordo,ves,saint) values(?,?,?,?,?,?,?,?,?,?)',[date,data['mass'],data['med'],data['comp'],data['let'],data['lod'],data['thought'],data['ordo'],data['ves'],data['saint']]);
		});
	}
}

var localDB = new CathAssistDB();