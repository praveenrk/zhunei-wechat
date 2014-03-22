package org.cathassist.cxradio.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class Channel
{
	public static Channel getChannelFormJson(JSONObject j)
	{
		SimpleDateFormat fmDate = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
		Channel c = new Channel();
		
		try {
			c.title = j.getString("title");
			c.date = fmDate.parse(j.getString("date"));
        	c.logo = j.getString("logo");
        	JSONArray items = j.getJSONArray("items");
        	for(int i = 0; i<items.length(); ++i)
        	{
        		Channel.Item item = new Channel.Item(
        				items.getJSONObject(i).getString("title"),
        				items.getJSONObject(i).getString("src"),"");
        		c.items.add(item);
        	}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return c;
	}
	
	public static class Item
	{
		private String title;
		private String src;
		private String singer;
		private long loading;	// <0未下载  0<=&&>=100正在下载 >100已完成下载
		
		public Item(String title,String src,String singer)
		{
			this.title = title;
			this.src = src;
			this.singer = singer;
			
			updateLoading();
		}
		
		public String getTitle()
		{
			return title;
		}
		
		public String getSrc()
		{
			return src;
		}
		
		public String getSinger()
		{
			return singer;
		}
		
		public float getLoading()
		{
			return loading;
		}
		
		public void setLoading(long f)
		{
			this.loading = f;
		}
		
		public void updateLoading()
		{
			String local = RadioDownloadManager.getTrackSrc(src);
			if(local != src)
			{
				this.loading = 101;
			}
			else
			{
				this.loading = -1;
			}
		}
	}
	public Channel()
	{
		items = new ArrayList<Item>();
	}
	
	public void updateLoadings()
	{
		for(int i=0;i<items.size();++i)
		{
			items.get(i).updateLoading();
		}
	}
	
	public String title;
	public Date date;
	public String logo;
	public List<Item> items;
}
