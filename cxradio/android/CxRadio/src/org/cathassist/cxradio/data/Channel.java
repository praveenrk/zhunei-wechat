package org.cathassist.cxradio.data;

import java.util.Date;
import java.util.*;



public class Channel {
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
			this.loading = -1;
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
	}
	public Channel()
	{
		items = new ArrayList<Item>();
	}
	
	public String title;
	public Date date;
	public String logo;
	public List<Item> items;
}
