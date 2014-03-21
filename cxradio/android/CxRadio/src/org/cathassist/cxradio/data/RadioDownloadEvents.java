package org.cathassist.cxradio.data;

public interface RadioDownloadEvents
{
	public void onRadioDownloadItemChanged(Channel.Item item);
	public void onRadioDownloadFinished();
}
