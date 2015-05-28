package com.weaver.eric.orion.utils;

import android.util.Log;

public class Timer implements Runnable{

	private Thread timerThread;
	private int seconds = 0;
	private long now = 0;
	private int length = 0;
	private int index = 0;
	private boolean isKeepingTime = true;
	
	public Timer()
	{
		timerThread = new Thread(this, "Timer Thread");
		isKeepingTime = true;
		now = System.currentTimeMillis();
		timerThread.start();
		
	}
	
	public Timer(int length)
	{
		this.length = length;
		isKeepingTime = true;
		timerThread = new Thread(this, "Timer Thread");
		now = System.currentTimeMillis();
		timerThread.start();
	}

	public void Increment()
	{
		index = index + 1;
	}
	
	private void counter()
	{
		long millis = 0;
		while(index < length)
		{
			millis = System.currentTimeMillis();
		}
		isKeepingTime = false;
		millis = millis - now;
		seconds = (int)(millis / 1000);
		millis = millis % 1000;
		Log.d("TimerFinished", seconds +" seconds and " + millis + " miliseconds to complete");
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(isKeepingTime == true)
		{
			counter();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	
}
