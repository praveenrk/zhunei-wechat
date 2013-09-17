-- phpMyAdmin SQL Dump
-- version 2.7.0-pl1
-- http://www.phpmyadmin.net
-- 
-- 主机: localhost
-- 生成日期: 2013 年 09 月 17 日 05:20
-- 服务器版本: 5.1.63
-- PHP 版本: 5.2.17
-- 
-- 数据库: `a0917090057`
-- 

-- --------------------------------------------------------

-- 
-- 表的结构 `lodo`
-- 

CREATE TABLE `lodo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `lodo` text,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=107 DEFAULT CHARSET=latin1 AUTO_INCREMENT=107 ;

-- --------------------------------------------------------

-- 
-- 表的结构 `pray`
-- 

CREATE TABLE `pray` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` text,
  `createtime` datetime DEFAULT NULL,
  `text` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=20 DEFAULT CHARSET=latin1 AUTO_INCREMENT=20 ;

-- --------------------------------------------------------

-- 
-- 表的结构 `stuff`
-- 

CREATE TABLE `stuff` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `time` date NOT NULL,
  `mass` text,
  `med` text,
  `comp` text,
  `let` text,
  `lod` text,
  `thought` text,
  `ordo` text,
  `ves` text,
  `saint` text,
  `valid` int(11) DEFAULT NULL,
  `lastupdate` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `stuff_time_index` (`time`),
  UNIQUE KEY `stuff_id_index` (`id`),
  KEY `index_stuff_time` (`time`)
) ENGINE=MyISAM AUTO_INCREMENT=134 DEFAULT CHARSET=latin1 AUTO_INCREMENT=134 ;

-- --------------------------------------------------------

-- 
-- 表的结构 `users`
-- 

CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  `username` varchar(32) DEFAULT NULL,
  `password` varchar(32) DEFAULT NULL,
  `isadmin` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

-- --------------------------------------------------------

-- 
-- 表的结构 `vatican_topic`
-- 

CREATE TABLE `vatican_topic` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `local` varchar(1024) NOT NULL,
  `name` varchar(1024) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `local` (`local`(767),`name`(767)),
  KEY `local_2` (`local`(767)),
  KEY `name` (`name`(767))
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

-- 
-- 表的结构 `vaticanacn`
-- 

CREATE TABLE `vaticanacn` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(10086) DEFAULT NULL,
  `src` varchar(10086) NOT NULL,
  `local` varchar(512) NOT NULL,
  `time` date NOT NULL,
  `cate` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `src` (`src`(767),`local`,`time`,`cate`),
  KEY `src_2` (`src`(767)),
  KEY `local` (`local`),
  KEY `cate` (`cate`),
  KEY `time` (`time`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1 AUTO_INCREMENT=11 ;

-- --------------------------------------------------------

-- 
-- 表的结构 `wechat`
-- 

CREATE TABLE `wechat` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `get` text,
  `post` text,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;
