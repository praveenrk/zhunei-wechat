package org.cathassist.daily.calendar;

import android.content.Context;
import android.util.Log;
import android.widget.ViewFlipper;

/**
 * @类描述:重写viewflipper的方法，避免横竖屏切换异常
 * @version 创建时间：2011-3-20上午11:49:43
 * @创建人: zp
 * @version 修改时间： 2011-3-20上午11:49:43
 */

public class MyViewFlipper extends ViewFlipper {

	/**
	 * @param context
	 */
	public MyViewFlipper(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		try {
			super.onDetachedFromWindow();
		} catch (Exception e) {
			Log.v("viewflipper error","viewflipper error");
		}
	}

}
