function CathAssistDB(){	try	{		this.db = openDatabase(this.dbName, this.dbVersion, "database for cathassist.org", 200000);	}	catch(e)	{		alert("目前『天主教小助手』无法支持你的系统！");	}	this.db.transaction(function(tx){		tx.executeSql('CREATE TABLE IF NOT EXISTS stuff (date unique,mass,med,comp,let,lod,thought,ordo,ves,saint)');		tx.executeSql('CREATE TABLE IF NOT EXISTS vaticanacn_cate (id unique,name)');		tx.executeSql('CREATE TABLE IF NOT EXISTS vaticanacn (id unique,title,pic,content,cate,time)');	});}CathAssistDB.prototype = {	db:null,	dbName:"CathAssist",	dbVersion:"0.9",	server:"http://cathassist.org/",	countPerPage:10,//每页个数	//Stuff操作    getStuff:function(date,type,callback){		if(!localDB.db)			return null;		localDB.db.transaction(function(tx){			tx.executeSql('select mass,med,comp,let,lod,thought,ordo,ves,saint from stuff where date=?', [date], function(tx,r){				if(r.rows.length>0)				{					if(callback)					{						if(type=="" || (!type))							callback(r.rows.item(0));						else							callback(r.rows.item(0)[type]);					}				}				else				{					$.getJSON(localDB.server+"getstuff/getstuff.php?date="+date+"&type=jsonp&callback=?",						function(obj){							localDB.setStuff(date,obj);							if(callback)							{								if(type=="" || (!type))									callback(obj);								else									callback(obj[type]);							}						}					);				}			});		});    },	setStuff:function(date,data){		if(!localDB.db)			return null;		localDB.db.transaction(function(tx){			tx.executeSql('delete from stuff where date=?', [date]);			tx.executeSql('insert into stuff(date,mass,med,comp,let,lod,thought,ordo,ves,saint) values(?,?,?,?,?,?,?,?,?,?)',[date,data['mass'],data['med'],data['comp'],data['let'],data['lod'],data['thought'],data['ordo'],data['ves'],data['saint']]);		});	},	//Article操作	getVaticanacnList:function(from,callback,refresh){		//http://cathassist.org/vaticanacn/getarticle.php?type=jsonp&mode=list&callback=10&from=479&count=200		if(!localDB.db)			return null;				if(refresh)		{			$.getJSON(localDB.server+"vaticanacn/getarticle.php?type=jsonp&mode=list&from="+from+"&&callback=?",				function(obj){					if(callback)					{						alert(obj[0].id);						callback(obj);					}				}			);		}		else		{			localDB.db.transaction(function(tx){				if(from<0)				{					from = 9999999;				}				tx.executeSql('select id,title,pic,content,cate,time from vaticanacn where id<? order by id desc limit ?', [from,localDB.countPerPage], function(tx,r){					if(r.rows.length>0)					{						if(callback)						{							callback(r.rows);						}					}					else					{						$.getJSON(localDB.server+"vaticanacn/getarticle.php?type=jsonp&mode=list&from="+from+"&&callback=?",							function(obj){								if(callback)								{									localDB.setVaticanacnList(obj);									callback(obj);								}							}						);					}				},function(tx,r){refresh = true;});			});		}	},	setVaticanacnList:function(obj)	{		if(!localDB.db)			return null;		localDB.db.transaction(function(tx){			for(var i in obj)			{				tx.executeSql('delete from vaticanacn where id=?', [obj[i].id]);				tx.executeSql('insert into vaticanacn(id,title,pic,cate,time) values(?,?,?,?,?)',[obj[i].id,obj[i].title,obj[i].pic,obj[i].cate,obj[i].time]);			}		});	}}var localDB = new CathAssistDB();