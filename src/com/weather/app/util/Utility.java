package com.weather.app.util;

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
}
