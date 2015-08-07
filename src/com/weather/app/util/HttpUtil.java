package com.weather.app.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {
	
	public static void sendHttpRequest(final String address,final HttpCallbackListener listener){
		
		new Thread(new Runnable(){

			@Override
			public void run() {
					
				HttpURLConnection connection=null;
				try {
					URL url= new URL(address);
					connection=(HttpURLConnection)url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
				
					StringBuffer response=new StringBuffer();
					
					BufferedReader buffer=new BufferedReader(new InputStreamReader(connection.getInputStream()));
					String line;
					
				
					while((line=buffer.readLine())!=null){
						response.append(line);
					}
					
				if(listener!=null){
					listener.finish(response.toString());
				}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					if(listener!=null){
						listener.erro(e);
					}
				}finally{
					if(connection!=null){
						connection.disconnect();
				
					}
				}
			}
			
		}).start();
	}
	
	public interface HttpCallbackListener{
		void finish(String response);
		void erro(Exception e);
	}
}
