package com.weather.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weather.app.R;
import com.weather.app.util.HttpUtil;
import com.weather.app.util.HttpUtil.HttpCallbackListener;
import com.weather.app.util.Utility;

public class WeatherActivity extends Activity{
	private TextView cityNameText;
	private TextView publishText;
	private TextView currentDateText;
	private TextView weatherInfoText;
	private TextView temp1Text;
	private TextView temp2Text;
	private LinearLayout linearLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_info);
		cityNameText=(TextView)findViewById(R.id.city_name);
		publishText=(TextView)findViewById(R.id.publish_text);
		currentDateText=(TextView)findViewById(R.id.current_date);
		weatherInfoText=(TextView)findViewById(R.id.weather_desp);
		temp1Text=(TextView)findViewById(R.id.temp1);
		temp2Text=(TextView)findViewById(R.id.temp2);
		linearLayout=(LinearLayout)findViewById(R.id.linearLayout);
		Intent intent=getIntent();
		String countryCode=intent.getStringExtra("country_code");
		if(!TextUtils.isEmpty(countryCode)){
			linearLayout.setVisibility(View.INVISIBLE);
			cityNameText.setVisibility(View.INVISIBLE);
			publishText.setText("同步中");
			queryWeatherCode(countryCode);
		}else{
			showWeather();
		}
	}
	
	
	private void queryWeatherCode(String countryCode){
		System.out.println("dizhi  a");
		String address="http://www.weather.com.cn/data/list3/city"+countryCode+".xml";
		queryFromServer(address,"countryCode");
	}
	
	
	private void queryWeatherInfo(String weatherCode){
		System.out.println("dizhi b");
		String address="http://www.weather.com.cn/data/cityinfo/"+weatherCode+".html";
		queryFromServer(address,"weatherCode");
	}
	

	private void queryFromServer(final String address,final String type){
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void finish(String response) {
				System.out.println("1");
			if("countryCode".equals(type)){
				
				if(!TextUtils.isEmpty(response)){
					
					String []array=response.split("\\|");
					if(array!=null&&array.length==2){
					String weatherCode=array[1];
					System.out.println(weatherCode);
					queryWeatherInfo(weatherCode);
					}
				
				}
				}else if("weatherCode".equals(type)){
					System.out.println("jiexi");
				Utility.handleWeatherResponse(WeatherActivity.this, response);
				runOnUiThread(new Runnable(){

					@Override
					public void run() {
						showWeather();
				}
				
			});
				}
			}
			
			@Override
			public void erro(Exception e) {
				runOnUiThread(new Runnable(){

					@Override
					public void run() {
						publishText.setText("同步失败");
						
					}
					
				});
				
			}
		});
	}
	
	
	
			
			
			
	private void showWeather(){
		
		SharedPreferences preference=PreferenceManager.getDefaultSharedPreferences(this);
		cityNameText.setText(preference.getString("city_name", ""));
		temp1Text.setText(preference.getString("temp1", ""));
		temp2Text.setText(preference.getString("temp2", ""));
		weatherInfoText.setText(preference.getString("weather_desp", ""));
		publishText.setText("今天"+preference.getString("publish_time", "")+"发布");
		currentDateText.setText(preference.getString("current_date", ""));
		linearLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);
	}
	
	
	
}
