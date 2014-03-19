package org.cathassist.cxradio.media;

import org.cathassist.cxradio.data.*;
public interface RadioEvents
{
	public void onRadioItemChanged(Channel.Item item);
	public void onRadioPrepared(int max);
	public void onRadioStoped();
	
	public void onRadioBufferedUpdate(int progress);
	public void onRadioUpdateProgress(int progress);
}
