package com.weather.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class WeatherOpenHelper extends SQLiteOpenHelper{
	public static final String CREATE_PROVINCE="create table Province(" 
			 +"id primary key autoincrement," 
			 +"province_name text," 
			 +"province_code text)";
	public static final String CREATE_CITY="create table City(" 
			 +"id primary key autoincrement," 
			 +"city_name text," 
			 +"city_code text"
			 +"province_id integer)";
	public static final String CREATE_COUNTRY="create table Country(" 
			 +"id primary key autoincrement," 
			 +"country_name text," 
			 +"country_code text"
			 +"city_id integer)";


	public WeatherOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_PROVINCE);
		db.execSQL(CREATE_CITY);
		db.execSQL(CREATE_COUNTRY);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		
		
	}

}