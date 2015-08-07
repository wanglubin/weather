package com.weather.app.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.weather.app.util.HttpUtil;
import com.weather.app.util.HttpUtil.HttpCallbackListener;
import com.weather.app.util.Utility;

public class AutoUpdateService extends Service{

	@Override
	public IBinder onBind(Intent arg0) {
		
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		new Thread(new Runnable(){

			@Override
			public void run() {
				updateWeather();
				System.out.println("wanglubin");
				
			}
			
		}).start();
		AlarmManager manager=(AlarmManager)getSystemService(ALARM_SERVICE);
		int anHour=3000;
		long triggerAtTime=SystemClock.elapsedRealtime()+anHour;
		Intent i=new Intent(this,AutoUpdateService.class);
		PendingIntent pi=PendingIntent.getBroadcast(this, 0, i, 0);
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
		
		
		
		
		
		
		return super.onStartCommand(intent, flags, startId);
	}
	private void updateWeather(){
		SharedPreferences preference=PreferenceManager.getDefaultSharedPreferences(this);
		String weatherCode=preference.getString("weather_code", "");
		String address="http://www.weather.com.cn/data/cityinfo/"+weatherCode+".html";
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void finish(String response) {
				Utility.handleWeatherResponse(AutoUpdateService.this, response);
				
			}
			
			@Override
			public void erro(Exception e) {
				
				
			}
		});
	}


}
