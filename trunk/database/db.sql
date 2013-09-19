create table lodo(
id int NOT NULL AUTO_INCREMENT,
PRIMARY KEY(id),
lodo text
);

create table pray(
id int NOT NULL AUTO_INCREMENT,
PRIMARY KEY(id),
name text,
createtime datetime,
text text NOT NULL
);

create table stuff(
id int NOT NULL AUTO_INCREMENT,
PRIMARY KEY(id),
time date NOT NULL,
mass text,
med text,
comp text,
let text,
lod text,
thought text,
ordo text,
ves text,
saint text,
valid int,
lastupdate date
);

create table users(
id int NOT NULL AUTO_INCREMENT,
PRIMARY KEY(id),
name varchar(32) NOT NULL,
username varchar(32),
password varchar(32),
isadmin int
);

create table wechat(
id int NOT NULL AUTO_INCREMENT,
PRIMARY KEY(id),
get text,
post text
);

create table vaticanacn(
id int NOT NULL AUTO_INCREMENT,
PRIMARY KEY(id),
title text,
src varchar(512),
local varchar(512),
time date,
cate int,
picurl varchar(10086);
);