package org.cathassist.cxradio.data;

import java.util.Date;
import java.util.*;



public class Channel {
	public static class Item
	{
		public String title;
		public String src;
		public String singer;
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
