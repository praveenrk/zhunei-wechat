package org.cathassist.cxradio;

import android.content.Context;
import android.util.Log;
import android.view.*;
import android.widget.*;

import org.cathassist.cxradio.data.*;

public class PlayListAdapter extends BaseAdapter
{
	private LayoutInflater mInflater;// 动态布局映射
	private Channel channel = null;
	public PlayListAdapter(Context context)
	{
		mInflater = LayoutInflater.from(context);
	}
	
	public void setChannel(Channel c)
	{
		this.channel = c;
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount()
	{
		if(channel!=null)
		{
			return channel.items.size();
		}
		
		return 0;
	}
	
	@Override
	public Channel.Item getItem(int arg0)
	{
		if(channel != null)
		{
			if(arg0>-1 && arg0<channel.items.size())
			{
				return channel.items.get(arg0);
			}
		}
		return null;
	}
	
	@Override
	public long getItemId(int arg0)
	{
		return arg0;
	}
	
	@Override  
    public View getView(int position, View convertView, ViewGroup parent)
	{
		//根据布局文件实例化view
		convertView = mInflater.inflate(R.layout.playlist_item, null);
		Channel.Item item = getItem(position);
		if(item == null)
		{
			return convertView;
		}

		TextView t1 = (TextView)convertView.findViewById(R.id.textView_TrackName);
		t1.setText(item.getTitle());
		
		ProgressBar loadingBar = (ProgressBar)convertView.findViewById(R.id.progressBar_loading);
		if(item.getLoading()<0 || item.getLoading()>100)
		{
			loadingBar.setVisibility(View.INVISIBLE);
		}
		else
		{
			loadingBar.setVisibility(View.VISIBLE);
		}
		
		return convertView;
    }
}
