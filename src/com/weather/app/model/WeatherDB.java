package com.weather.app.model;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.weather.app.db.WeatherOpenHelper;

public class WeatherDB {
	public static final int VERSION=1;
	public static final String DB_NAME="weather";
	private static WeatherDB weatherDB;
	private SQLiteDatabase db;
	
	
	
	
	private WeatherDB(Context context){
		WeatherOpenHelper dbHelper=new WeatherOpenHelper(context, DB_NAME, null, VERSION);
		db=dbHelper.getWritableDatabase();
	}
	
	
	
	
	
	public synchronized static WeatherDB getInstance(Context context){
		if(weatherDB==null){
		weatherDB=new WeatherDB(context);
		}
		return weatherDB;
		
	}
	
	
	
	
	public void saveProvince(Province province){
		if(province!=null){
		ContentValues values=new ContentValues();
		values.put("province_name",province.getProvinceName() );
		values.put("province_code", province.getProvinceCode());
		db.insert("Province", null, values);
		}
	}
	
	
	
	
	public List<Province> loadProvince(){
		List<Province> list=new ArrayList<Province>();
		Cursor cursor=db.query("Province", null, null, null, null, null, null);
		if(cursor.moveToFirst()){
			do{Province province=new Province();
			province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
			province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
			province.setId(cursor.getInt(cursor.getColumnIndex("id")));
			list.add(province);
				
			}while(cursor.moveToNext());
			if(cursor!=null){
				cursor.close();
			}
		}
		return list;

	
	}
	
	
	
	public void saveCity(City city){
		if(city!=null){
		ContentValues values=new ContentValues();
		values.put("city_name",city.getCityName() );
		values.put("city_code", city.getCityCode());
		values.put("province_id", city.getProvinceId());
		db.insert("City", null, values);
		}
	}
	
	
	
	
	public List<City> loadCity(int provinceId){
		List<City> list=new ArrayList<City>();
		Cursor cursor=db.query("City", null, null, null, null, null, null);
		if(cursor.moveToFirst()){
			do{City city=new City();
			city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
			city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
			city.setId(cursor.getInt(cursor.getColumnIndex("id")));
			city.setProvinceId(provinceId);
			list.add(city);
				
			}while(cursor.moveToNext());
			if(cursor!=null){
				cursor.close();
			}
		}
		return list;

	
	}
	
	
	
	
	public void saveCountry(Country country){
		if(country!=null){
		ContentValues values=new ContentValues();
		values.put("country_name",country.getCountryName() );
		values.put("country_code", country.getCountryCode());
		values.put("city_id", country.getCityId());
		db.insert("Country", null, values);
		}
	}
	
	
	
	
	public List<Country> loadCountry(int cityId){
		List<Country> list=new ArrayList<Country>();
		Cursor cursor=db.query("Country", null, null, null, null, null, null);
		if(cursor.moveToFirst()){
			do{Country country=new Country();
			country.setCountryName(cursor.getString(cursor.getColumnIndex("country_name")));
			country.setCountryCode(cursor.getString(cursor.getColumnIndex("country_code")));
			country.setId(cursor.getInt(cursor.getColumnIndex("id")));
			country.setCityId(cityId);
			list.add(country);
				
			}while(cursor.moveToNext());
			if(cursor!=null){
				cursor.close();
			}
		}
		return list;

	
	}
	
	
	}
	
	
	
	
	
	
	
	

