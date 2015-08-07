package com.weather.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.weather.app.model.City;
import com.weather.app.model.Country;
import com.weather.app.model.Province;
import com.weather.app.model.WeatherDB;

public class Utility {
	public synchronized static Boolean handleProvincesResponse(WeatherDB weatherDB,String response){
		if(!TextUtils.isEmpty(response)){
			String [] allProvinces=response.split(",");
			if(allProvinces!=null&&allProvinces.length>0){
				for(String p :allProvinces){
				String[] array=p.split("\\|");
				Province province=new Province();
				province.setProvinceCode(array[0]);
				province.setProvinceName(array[1]);
				weatherDB.saveProvince(province);
				}
				return true;
			}
		}
		return false;
		
	}



public static Boolean handleCitiesResponse(WeatherDB weatherDB,String response,int provinceId){
	if(!TextUtils.isEmpty(response)){
		
		String [] allCities=response.split(",");
		if(allCities!=null&&allCities.length>0){
			for(String c :allCities){
			String[] array=c.split("\\|");
			City city=new City();
			city.setCityCode(array[0]);
			city.setCityName(array[1]);
			city.setProvinceId(provinceId);
			weatherDB.saveCity(city);
			}
		
			return true;
		}
	}
	return false;
	
}


public static Boolean handleCountriesResponse(WeatherDB weatherDB,String response,int cityId){
	if(!TextUtils.isEmpty(response)){
		String [] allCountries=response.split(",");
		if(allCountries!=null&&allCountries.length>0){
			for(String c :allCountries){
			String[] array=c.split("\\|");
			Country country=new Country();
			country.setCountryCode(array[0]);
			country.setCountryName(array[1]);
			country.setCityId(cityId);
			weatherDB.saveCountry(country);
			}
			return true;
		}
	}
	return false;
	
}

public static void handleWeatherResponse(Context context,String response){
	
	try {
		JSONObject jSONObject=new JSONObject(response);
		JSONObject weatherInfo=jSONObject.getJSONObject("weatherinfo");
		String cityName=weatherInfo.getString("city");
		String weatherCode=weatherInfo.getString("cityid");
		String temp1=weatherInfo.getString("temp1");
		String temp2=weatherInfo.getString("temp2");
		String weatherDesp=weatherInfo.getString("weather");
		String publishTime=weatherInfo.getString("ptime");
		saveWeatherInfo(context,cityName,weatherCode,temp1,temp2,weatherDesp,publishTime);
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}


public static void saveWeatherInfo(Context context,String cityName,String weatherCode,String temp1,String temp2, String weatherDesp,String publishTime){
	SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(context).edit();
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日",Locale.CHINA);
	editor.putBoolean("selected_city", true);
	editor.putString("city_name", cityName);
	editor.putString("weather_code", weatherCode);
	editor.putString("temp1", temp1);
	editor.putString("temp2", temp2);
	editor.putString("weather_desp", weatherDesp);
	editor.putString("publish_time", "刚刚更新");
	editor.putString("current_date",sdf.format(new Date()));
	editor.commit();
	
	
}





}
