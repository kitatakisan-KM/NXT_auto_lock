package a.bluetooth;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;


public class Draw extends View {
	private Rect mRect = new Rect();

	private short[] mByte;
	private float[] mPoints;
	private Paint mForePaint = new Paint();
	private Paint Paint = new Paint();
	public Draw(Context context){
		this(context,null);
	}
	public Draw(Context context,AttributeSet attrs){
		this(context,attrs,0);
	}
	public Draw(Context context,AttributeSet attrs,int defStayle) {
        super(context, attrs,defStayle);
        init();
    }
	private void init(){
		mByte = null;
		mForePaint.setStrokeWidth(1f);
		mForePaint.setAntiAlias(true);
		mForePaint.setColor(Color.rgb(255, 0, 0));
		Paint.setStrokeWidth(1f);
		Paint.setAntiAlias(true);
		Paint.setColor(Color.rgb(0, 0, 0));
	}
	public void updateVisualizer(short[] bytes){
		mByte = bytes;
		

		invalidate();
	}
	@SuppressLint("DrawAllocation") @Override
	protected void onDraw(Canvas canva) {
		super.onDraw(canva);
		if(mByte == null){
			return;
		}
		if(mPoints == null || mPoints.length<mByte.length * 4){
			mPoints = new float[mByte.length * 4];
		}
		mRect.set(0, 0, getWidth(), getHeight());
		
		for(int i = 0; i< mByte.length - 1; i++){
			
			mPoints[i * 4] = mRect.width()*(i) * 2  / (mByte.length - 1);
			
			mPoints[i * 4 + 1] = mRect.height() - 10 + ((short)(-mByte[i])) * (mRect.height() / 20) / 400;
			
			mPoints[i * 4 + 2] = mRect.width() * (i + 1) * 2 / (mByte.length - 1);
			
			mPoints[i * 4 + 3] = mRect.height() - 10 + ((short)(-mByte[i + 1])) * (mRect.height() / 20) / 400;
			
		}
		canva.drawLines(mPoints, mForePaint);
		canva.drawLine(0, mRect.height() - 10 + ((short)(-800)) * (mRect.height() / 20) / 400, 1200, mRect.height() - 10 + ((short)(-800)) * (mRect.height() / 20) / 400 , Paint);

	}
	
}