package com.weather.app.activity;

import java.util.ArrayList;
import java.util.List;


import com.weather.app.R;
import com.weather.app.model.City;
import com.weather.app.model.Country;
import com.weather.app.model.Province;
import com.weather.app.model.WeatherDB;
import com.weather.app.util.HttpUtil;
import com.weather.app.util.HttpUtil.HttpCallbackListener;
import com.weather.app.util.Utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseAreaActivity extends Activity {
	public static final int LEVEL_PROVINCE=0;
	public static final int LEVEL_CITY=1;
	public static final int LEVEL_COUNTRY=2;
	private TextView titleText;
	private ListView listView;
	private List<Province> provinceList;
	private List<City> cityList;
	private List<Country> countryList;
	private ArrayAdapter<String> adapter;
	private List<String> dataList=new ArrayList<String>();
	private int currentLevel;
	private Province selectedProvince;
	private City selectedCity;
	private WeatherDB weatherDB;
	private ProgressDialog progressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences preference=PreferenceManager.getDefaultSharedPreferences(this);
		if(preference.getBoolean("selected_city", false)){
			Intent intent=new Intent();
			intent.setClass(ChooseAreaActivity.this, WeatherActivity.class);
			startActivity(intent);
			finish();
		}
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		titleText=(TextView)findViewById(R.id.titleId);
		listView=(ListView)findViewById(R.id.listView);
		adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, dataList);
		listView.setAdapter(adapter);
		weatherDB=WeatherDB.getInstance(this);		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int index,
					long arg3) {
				if(currentLevel==LEVEL_PROVINCE){
					selectedProvince=provinceList.get(index);
					
					queryCities();
				}else if(currentLevel==LEVEL_CITY){
					selectedCity=cityList.get(index);
					queryCountries();
				}else if(currentLevel==LEVEL_COUNTRY){
					String countryCode=countryList.get(index).getCountryCode();
					Intent intent=new Intent();
					intent.setClass(ChooseAreaActivity.this, WeatherActivity.class);
					intent.putExtra("country_code",countryCode);
					startActivity(intent);
					finish();
				}
			}
			
		});
		queryProvinces();
	}
	
	

	
	
	
	private void queryProvinces(){
		provinceList=weatherDB.loadProvinces();
		if(provinceList.size()>0){
			dataList.clear();
			for(Province province:provinceList){
			dataList.add(province.getProvinceName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText("中国");
			currentLevel=LEVEL_PROVINCE;
		}else{
			queryFromServer(null,"province");
		}
		
	
	}
	
	
	
	private void queryCities(){
		
		cityList=weatherDB.loadCities(selectedProvince.getId());
		if(cityList.size() > 0){
			dataList.clear();
			for(City city:cityList){
				dataList.add(city.getCityName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedProvince.getProvinceName());
			currentLevel=LEVEL_CITY;
		}else{
			
			queryFromServer(selectedProvince.getProvinceCode(),"city");
			
		}
	}
	
	
	private void queryCountries(){
		countryList=weatherDB.loadCountries(selectedCity.getId());
		if(countryList.size()>0){
			dataList.clear();
			for(Country country:countryList){
				dataList.add(country.getCountryName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedCity.getCityName());
			currentLevel=LEVEL_COUNTRY;
		}else{
			queryFromServer(selectedCity.getCityCode(),"country");
		}
	}
	
	private void queryFromServer(final String code,final String type){
		String address;
		if(!TextUtils.isEmpty(code)){
			System.out.println("a");
			address="http://www.weather.com.cn/data/list3/city"+code+".xml";
		}else{
			System.out.println("b");
			address="http://www.weather.com.cn/data/list3/city.xml";
		}
		showProgressDialog();
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void finish(String response) {
				
				
				boolean result=false;
				if("province".equals(type)){
				 result=Utility.handleProvincesResponse(weatherDB,response);
				}else if("city".equals(type)){
					result=Utility.handleCitiesResponse(weatherDB, response,selectedProvince.getId());
				}else if("country".equals(type)){
					result=Utility.handleCountriesResponse(weatherDB, response,selectedCity.getId());
				}
				if(result){
					runOnUiThread(new Runnable(){

						@Override
						public void run() {
						
							closeProgressDialog();
					
							if("province".equals(type)){
								 queryProvinces();
								}else if("city".equals(type)){
									queryCities();
								}else if("country".equals(type)){
									queryCountries();
								}	
							
						}
						
					});
				}
				
			}
			
			@Override
			public void erro(Exception e) {
				runOnUiThread(new Runnable(){

					@Override
					public void run() {
						closeProgressDialog();
				Toast.makeText(ChooseAreaActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
					
			
				
					}
				});
		
			}
		});
	
	}
	
	public void showProgressDialog(){
		if(progressDialog==null){
		progressDialog=new ProgressDialog(this);
		progressDialog.setMessage("正在加载。。。");
		progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}
	
	public void closeProgressDialog(){
		if(progressDialog!=null){
			progressDialog.dismiss();
		}
	}




	@Override
	public void onBackPressed() {
		
	
		if(currentLevel==LEVEL_COUNTRY){
			queryCities();
		}else if(currentLevel==LEVEL_CITY){
			queryProvinces();
			
		}else{
			finish();
		}
	}
	
	
}
