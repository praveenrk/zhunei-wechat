package com.yyxu.download.services;

interface IDownloadService {
	
	void startManage();
	
	void addTask(String url, String path);
	
	void pauseTask(String url);
	
	void deleteTask(String url);
	
	void continueTask(String url);
}
