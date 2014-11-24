package edu.asu.rockpaperscissors;

import java.util.Random;

import edu.asu.rockpaperscissors.databases.DBAdapter;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class MultiTouchGestureView extends View {

	private static final int SIZE = 60;
	private static final String TAG=MultiTouchGestureView.class.getName();
	private SparseArray<PointF> mActivePointers;
	private Paint mPaint;
	private int[] colors = { Color.BLUE, Color.GREEN, Color.MAGENTA,
			Color.BLACK, Color.CYAN, Color.GRAY, Color.RED, Color.DKGRAY,
			Color.LTGRAY, Color.YELLOW };
	public enum Choice{
		ROCK(1,"Rock"),PAPER(2,"Paper"),SCISSOR(3,"Scissor");
		int val;
		String str;
		Choice(int i, String str){
			this.val=i;
			this.str=str;
		}
		int getVal(){
			return this.val;
		}
		public String getStr(){
			return this.str;
		}
	};

	private Paint textPaint;
	long timeStart=0,timeEnd=0;
	int maxTouch=0;
	float swipeX=0,swipeY=0;
	float lastPosX=0,lastPosY=0;
	private static final int SENS_THRESHOLD=500;
	ProgressDialog progressBar;
	private int progressBarStatus = 0;
	private Handler progressBarHandler = new Handler();
	public MultiTouchGestureView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	private void initView() {
		mActivePointers = new SparseArray<PointF>();
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		// set painter color to a color you like
		mPaint.setColor(Color.BLUE);
		mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		textPaint.setTextSize(20);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		// get pointer index from the event object
		int pointerIndex = event.getActionIndex();

		// get pointer ID
		int pointerId = event.getPointerId(pointerIndex);

		// get masked (not specific to a pointer) action
		int maskedAction = MotionEventCompat.getActionMasked(event);

		switch (maskedAction) {

		case MotionEvent.ACTION_DOWN:
			timeStart=System.currentTimeMillis();
			PointF fini = new PointF();
			fini.x = event.getX();
			fini.y = event.getY();
			lastPosX=fini.x;lastPosY=fini.y;
			mActivePointers.put(pointerId, fini);
			maxTouch++;
			break;
		case MotionEvent.ACTION_POINTER_DOWN: {
			// We have a new pointer. Lets add it to the list of pointers

			PointF f = new PointF();
			f.x = event.getX(pointerIndex);
			f.y = event.getY(pointerIndex);
			mActivePointers.put(pointerId, f);
			maxTouch++;
			break;
		}
		case MotionEvent.ACTION_MOVE: { // a pointer was moved
			for (int size = event.getPointerCount(), i = 0; i < size; i++) {
				PointF point = mActivePointers.get(event.getPointerId(i));
				if (point != null) {
					point.x = event.getX(i);
					point.y = event.getY(i);
				}
			}
			swipeX+=Math.abs(event.getX()-lastPosX);swipeY+=Math.abs(event.getY()-lastPosY);
			lastPosX=event.getX();lastPosY=event.getY();
			break;
		}
		case MotionEvent.ACTION_UP:
			timeEnd=System.currentTimeMillis();
			mActivePointers.remove(pointerId);;
			long timeElapsed = timeEnd-timeStart;
			float sensitivity=(swipeX>swipeY)?swipeX:swipeY;
			if(maxTouch>3){
				Toast.makeText(getContext(), "More than 3 finger swipe not allowed!", Toast.LENGTH_SHORT).show();
			}
			else if(sensitivity > SENS_THRESHOLD && timeElapsed < 1000){
				final Choice choice = setChoice(maxTouch);
				playAndupdateDb(choice);				
				Log.d(TAG,"Finished.");				
				//launchDummyRingDialog(this);
				Intent i = null;
				i = new Intent(getContext(), ShowResultActivity.class);
				((Activity)getContext()).startActivity(i);
			}
			else if(sensitivity < SENS_THRESHOLD){
				Toast.makeText(getContext(), "Swipe was very short! Swipe again!", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(getContext(), "That swipe took a long time! Swipe again!", Toast.LENGTH_SHORT).show();
			}
			maxTouch=0;swipeX=0;swipeY=0;
			break;		
		case MotionEvent.ACTION_POINTER_UP:
		case MotionEvent.ACTION_CANCEL:
			mActivePointers.remove(pointerId);
			break;		
		}
		invalidate();
		return true;
	}

	protected int playAndupdateDb(Choice choice) {
		Choice comp = getRandChoice();
		Boolean result = winOrLose(choice,comp);		
		DBAdapter dba = new DBAdapter(getContext());
		dba.updateResult(choice,result,comp,"computer");
		return 100;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// draw all pointers
		for (int size = mActivePointers.size(), i = 0; i < size; i++) {
			PointF point = mActivePointers.valueAt(i);
			if (point != null)
				mPaint.setColor(colors[i % 9]);
			canvas.drawCircle(point.x, point.y, SIZE, mPaint);
		}
		canvas.drawText("Swipe with 1 finger for Rock", 10, 40,textPaint);
		canvas.drawText("2 fingers for Paper", 10, 90, textPaint);
		canvas.drawText("3 fingers for Scissor", 10, 140, textPaint);

	}

	private Choice setChoice(int i){
		if(i==1)
			return Choice.ROCK;
		else if(i==2)
			return Choice.PAPER;
		else if(i==3)
			return Choice.SCISSOR;
		else return null;
	}

	private Boolean winOrLose(Choice c,Choice opp){		
		if(c==Choice.ROCK){
			if(opp==Choice.PAPER)
				return false;
			if(opp==Choice.SCISSOR)
				return true;
			else return null;
		}else if(c==Choice.PAPER){
			if(opp==Choice.ROCK)
				return true;
			if(opp==Choice.SCISSOR)
				return false;
			else return null;
		}else if(c==Choice.SCISSOR){
			if(opp==Choice.PAPER)
				return true;
			if(opp==Choice.ROCK)
				return false;
			else return null;
		}
		return null;
	}

	private Choice getRandChoice() {
		Random gen = new Random();		
		Choice opp = setChoice(gen.nextInt(3)+1);
		return opp;
	}
	
	public void launchDummyRingDialog(View view) {
		final ProgressDialog ringProgressDialog = ProgressDialog.show(getContext(), "Computer Playing...", 
				"Computer Playing...", true);
		ringProgressDialog.setCancelable(true);
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
		}
		ringProgressDialog.dismiss();
	}
}
