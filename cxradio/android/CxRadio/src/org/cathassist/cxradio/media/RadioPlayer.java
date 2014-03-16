package org.cathassist.cxradio.media;

import org.cathassist.cxradio.data.*;

import android.media.*;

public class RadioPlayer extends MediaPlayer
{
	private Channel curChannel = null;
	public void setChannel(Channel c)
	{
		curChannel = c;
	}
	public void setPlayIndex(int i)
	{
		if(curChannel!=null)
		{
			if(curChannel.items.size()>i)
			{
				setPlaySrc(curChannel.items.get(i).src);
			}
		}
	}	
	
	private void setPlaySrc(String src)
	{
		try
		{
			reset();
			setDataSource(src);
			prepare();
			start();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
