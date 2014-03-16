package org.cathassist.cxradio;

import org.cathassist.cxradio.R;

import android.content.Context;
import android.graphics.*;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.util.AttributeSet;
import android.view.animation.*;
import android.widget.*;

public class PlayIconView extends ImageView
{
	Animation animIcon;
	long animOffset = 0;
	public PlayIconView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public PlayIconView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PlayIconView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	public void setImageResource(int resId)
	{
		super.setImageResource(resId);
		animIcon = AnimationUtils.loadAnimation(this.getContext(), R.anim.playicon);
        LinearInterpolator lin = new LinearInterpolator(); // 设置旋转速度 此处设置为匀速旋转
        animIcon.setInterpolator(lin);//将旋转速度配置给动画。
//		this.startAnimation(animIcon);
	}
	
	public void setStart()
	{
        this.startAnimation(animIcon);
//		animIcon.setStartOffset(animOffset);
	}
	
	public void setPause()
	{
		animOffset = animIcon.getStartOffset();
		this.clearAnimation();
	}
	
	
	
	public static Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
		Bitmap sbmp;
		if (bmp.getWidth() != radius || bmp.getHeight() != radius)
			sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
		else
			sbmp = bmp;
		Bitmap output = Bitmap.createBitmap(sbmp.getWidth(), sbmp.getHeight(),
				Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());

		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(Color.parseColor("#BAB399"));
		canvas.drawCircle(sbmp.getWidth() / 2 + 0.7f,
				sbmp.getHeight() / 2 + 0.7f, sbmp.getWidth() / 2 + 0.1f, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(sbmp, rect, rect, paint);

		return output;
	}

}