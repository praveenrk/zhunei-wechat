package org.cathassist.cxradio.data;

import java.io.File;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import org.cathassist.download.DownloadTask;
import org.cathassist.download.DownloadTaskListener;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class RadioDownloadManager
{
	public class RadioDownloadItem
	{
		public Channel.Item item;
		public DownloadTask task;
		
		public RadioDownloadItem(Channel.Item item,DownloadTask task)
		{
			this.item = item;
			this.task = task;
		}
	}
	
    private String destDir = "";
    private Channel channel = null;
    private Context context = null;
    private RadioDownloadEvents events = null;
    private Map<String,RadioDownloadItem> tasks = new HashMap<String,RadioDownloadItem>();
	
    public RadioDownloadManager(Channel channel,Context context,RadioDownloadEvents events)
    {
		String sd = Environment.getExternalStorageDirectory().getAbsolutePath();
		destDir = sd + "/CathAssist/cxradio/";

		java.io.File a=new java.io.File(destDir);
		if(!a.exists())
		{
			a.mkdirs();
		}
		
		this.channel = channel;
		this.context = context;
		this.events = events;
    }
    
    
    public void run()
    {
    	if(this.channel==null)
    	{
			events.onRadioDownloadFinished();
    		return;
    	}
    	
    	for(int i=0;i<channel.items.size();++i)
    	{
	    	try
			{
	    		Channel.Item item = channel.items.get(i);
				if(!tasks.containsKey(item.getSrc()))
				{
			        String fileName = "";
			        try
			        {
						java.security.MessageDigest md5 = java.security.MessageDigest.getInstance("MD5");
						md5.update(item.getSrc().getBytes());
						byte[] m = md5.digest();//加密
						StringBuffer sb = new StringBuffer();
						{
			                for(int j = 0; j < m.length; j++)
			                {
			                	sb.append(Integer.toHexString((0x000000ff & m[j]) | 0xffffff00).substring(6));
			                }
						}
			            fileName = sb.toString();
					}
			        catch (NoSuchAlgorithmException e)
			        {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					DownloadTask t = newDownloadTask(item.getSrc(),fileName);
					RadioDownloadItem downloadItem = new RadioDownloadItem(item,t);
					tasks.put(item.getSrc(), downloadItem);
			        File fDest = new File(destDir,fileName);
			        if(!fDest.exists())
			        {
						t.execute();
			        }
			        else
			        {
			        	item.setLoading(101);			//设置已下载完成
			        	removeDownloadItem(downloadItem);
			        }
				}
			}
			catch (MalformedURLException e)
			{
				e.printStackTrace();
			}
    	}
    }
	
	
    private void removeDownloadItem(RadioDownloadItem item)
    {
    	if(item!=null)
    	{
    		tasks.remove(item.item.getSrc());
    		if(tasks.size()<1)
    		{
    			events.onRadioDownloadFinished();
    		}
    	}
    }

    private DownloadTask newDownloadTask(String url,String fileName) throws MalformedURLException
    {
        DownloadTaskListener taskListener = new DownloadTaskListener()
        {
            @Override
            public void updateProcess(DownloadTask task)
            {
            	Log.d("DownloadManager", task.getUrl()+" Percent:"+task.getDownloadPercent());
            	RadioDownloadItem item = tasks.get(task.getUrl());
            	if(item!=null)
            	{
            		item.item.setLoading(task.getDownloadPercent());
            		events.onRadioDownloadItemChanged(item.item);
            	}
            }

            @Override
            public void preDownload(DownloadTask task)
            {
            	Log.d("DownloadManager", "preDownload...");
            }

            @Override
            public void finishDownload(DownloadTask task)
            {
            	Log.d("DownloadManager", "Finished...");
            	RadioDownloadItem item = tasks.get(task.getUrl());
            	if(item!=null)
            	{
            		Toast.makeText(context, "下载完成:"+item.item.getTitle(), Toast.LENGTH_SHORT).show();
            		item.item.setLoading(101);			//下载完成
            		removeDownloadItem(item);
            		events.onRadioDownloadItemChanged(item.item);
            	}
            }

            @Override
            public void errorDownload(DownloadTask task, Throwable error)
            {
                if (error != null)
                {
                	Log.d("DownloadManager", "Error in download...");
                	RadioDownloadItem item = tasks.get(task.getUrl());
                	if(item!=null)
                	{
                		Toast.makeText(context, "下载失败:"+item.item.getTitle(), Toast.LENGTH_SHORT).show();
                		item.item.setLoading(-1);			//下载失败
                		removeDownloadItem(item);
                		events.onRadioDownloadItemChanged(item.item);
                	}
                }
            }
        };
        
        return new DownloadTask(url, destDir, fileName, taskListener);
    }
}
