package com.example.champy.wunralk;

import android.os.Handler;
import android.os.SystemClock;
import android.widget.TextView;

/**
 * Created by kasem on 13/10/2558.
 */
public class Runtime implements Runnable {
    long timeMilli = 0L;
    long timeStart = 0L;
    long finaltime = 0L;
    long timeSwap = 0L;
    Handler myHandler;
    TextView text;

    public Runtime(TextView textView){
        text = textView;
        myHandler = new Handler();
    }
    @Override
    public void run() {
        timeMilli = SystemClock.uptimeMillis() -timeStart;
        finaltime = timeSwap + timeMilli;

        int seconds = (int) (finaltime / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        int milliseconds = (int) (finaltime % 1000);
        int hours = minutes/60;
        text.setText(String.format("%02d:%02d:%02d:%03d",hours,minutes,seconds,milliseconds));
        myHandler.postDelayed(this, 0);
    }

    public  void pause(){
        timeSwap += timeMilli;
        myHandler.removeCallbacks(this);
    }

    public void start(){
        timeStart = SystemClock.uptimeMillis();
        myHandler.postDelayed(this, 0);
    }

    public String getTime(){
        int seconds = (int) (finaltime / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        int milliseconds = (int) (finaltime % 1000);
        int hours = minutes/60;
        return (String.format("%02d:%02d:%02d:%03d",hours,minutes,seconds,milliseconds));
    }
    public void reset(){
        timeMilli = 0L;
        timeStart = 0L;
        finaltime = 0L;
        timeSwap = 0L;
        text.setText(String.format("%02d:%02d:%02d:%03d",0,0,0,0));
    }

    public long getAllSecond(){
        return finaltime;
    }
}
