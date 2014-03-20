package org.cathassist.cxradio;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class RadioNotification
{
	public static void showNotification(Context context,String musicName)
	{
		//创建一个NotificationManager的引用
		NotificationManager notificationManager = (NotificationManager)
				context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		
		//定义notification的各种属性
		NotificationCompat.Builder mBuilder = 
				new NotificationCompat.Builder(context)
					.setSmallIcon(R.drawable.ic_launcher)
					.setContentTitle("晨星生命之音")
					.setContentText(musicName)
					.setAutoCancel(false);
		
		Intent intent = new Intent(context,MainActivity.class);
		PendingIntent pi = PendingIntent.getActivity(context,0,intent,Intent.FLAG_ACTIVITY_NEW_TASK);
		mBuilder.setContentIntent(pi);
		notificationManager.notify(0, mBuilder.build());
		
		/*
        notification.flags |= Notification.FLAG_ONGOING_EVENT; // 将此通知放到通知栏的"Ongoing"即"正在运行"组中   
        notification.flags |= Notification.FLAG_SHOW_LIGHTS;   
        notification.defaults = Notification.DEFAULT_LIGHTS; 
        //叠加效果常量
        //notification.defaults=Notification.DEFAULT_LIGHTS|Notification.DEFAULT_SOUND;
        notification.ledARGB = Color.BLUE;   
        CharSequence contentTitle ="蝌蚪音乐播放器"; // 通知栏标题   
        CharSequence contentText ="正在播放  "+musicName; // 通知栏内容   
        Intent notificationIntent =new Intent(context, PlayMusicActivity.class); // 点击该通知后要跳转的Activity   
        PendingIntent contentItent = PendingIntent.getActivity(context, 0, notificationIntent, 0);   
        notification.setLatestEventInfo(context, contentTitle, contentText, contentItent);   
        notificationManager.notify(0, notification);*/
    }
	public static void clearNotification(Context context)
	{
        // 启动后删除之前我们定义的通知   
        NotificationManager notificationManager = (NotificationManager)context 
                .getSystemService(Context.NOTIFICATION_SERVICE);   
        notificationManager.cancel(0);  
    }
}
