package com.zzksoft.light;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.ActionMode.Callback;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.app.Activity;
import org.apache.commons.net.ntp.NTPUDPClient;
import java.net.InetAddress;
import org.apache.commons.net.ntp.TimeInfo;
import java.io.IOException;
import org.apache.commons.net.ntp.TimeStamp;

public class LightView extends SurfaceView implements SurfaceHolder.Callback
{
	
	private SurfaceHolder sh;
	
	private float h = 0,s = 0.7f,v = 1;
	
	private float[] hsv = new float[]{h,s,v};
	
	
	public LightView(Context c){
		super(c);
		init();
	}
	
	public LightView(Context c,AttributeSet attrs){
		super(c,attrs);
		init();
	}
	
	public LightView(Context c,AttributeSet attrs,int style){
		super(c,attrs,style);
		init();
	}
	
	Paint paint = new Paint();
	protected void init(){
		sh = getHolder();
		sh.addCallback(this);
		
		paint.setColor(Color.WHITE);
		paint.setTextSize(100);
		paint.setStrokeWidth(20);
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder p1)
	{
		// TODO: Implement this method
		new Thread(new Runnable(){
				@Override
				public void run()
				{
					long delay = 0;
					/*
					try{
						measureSaturation(getTime());
					}catch (IOException e){}
					*/
					while (true){
						long now = System.currentTimeMillis();
						
						Canvas canvas = sh.lockCanvas();
						
						if(hsv[0]>360){
							hsv[0] = 0;
						}
						
						hsv[0]+=3f;
						
						canvas.drawColor(Color.HSVToColor(hsv));
						
						//canvas.drawText(""+millsOffset,100,100,paint);
						
						sh.unlockCanvasAndPost(canvas);
						
						try{
							delay = 25-(System.currentTimeMillis()-now);
							Thread.sleep(Math.max(0,delay));
						}catch (InterruptedException e){
							((Activity)getContext()).finish();
						}
					}
				}
			}).start();
	}
	
	float millsOffset;
	private void measureSaturation(long time){
		millsOffset = time%3000;
		hsv[0] = millsOffset*3f/25f;
	}
	
	
	private long getTime() throws IOException{
		NTPUDPClient timeClient = new NTPUDPClient();
		//String timeServerUrl = "202.120.2.101";
		String timeServerUrl = "ntp.sjtu.edu.cn";
		InetAddress timeServerAddress = InetAddress.getByName(timeServerUrl);
		TimeInfo timeInfo = timeClient.getTime(timeServerAddress);
		TimeStamp timeStamp = timeInfo.getMessage().getTransmitTimeStamp();
		return timeStamp.getTime();
	}

	@Override
	public void surfaceChanged(SurfaceHolder p1, int p2, int p3, int p4)
	{
		// TODO: Implement this method
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder p1)
	{
		// TODO: Implement this method
	}
	
}
