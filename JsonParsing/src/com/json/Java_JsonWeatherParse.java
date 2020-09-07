package com.json;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.json.JSONArray;
import org.json.JSONObject;

 
public class Java_JsonWeatherParse {

    static final String URL_OpenWeatherMap_weather =
            "https://samples.openweathermap.org/data/2.5/box/city?bbox=12,32,15,37,10&appid=b6907d289e10d714a6e88b30761fae22";

    public static void main(String[] args) {
        
        String result = "";
        
        try {
            URL url_weather = new URL(URL_OpenWeatherMap_weather);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url_weather.openConnection();

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                InputStreamReader inputStreamReader =
                    new InputStreamReader(httpURLConnection.getInputStream());
                BufferedReader bufferedReader =
                    new BufferedReader(inputStreamReader, 8192);
                String line = null;
                while((line = bufferedReader.readLine()) != null){
                    result += line;
                }
                
                bufferedReader.close();                
           
                ParseResult(result);     

            } else {
                System.out.println("Error in httpURLConnection.getResponseCode()");
            }

        }  
        catch (Exception ex) {
            Logger.getLogger(Java_JsonWeatherParse.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    static private void ParseResult(String json) throws Exception{ 
     
        JSONObject jsonObject = new JSONObject(json);             
       
        Logger cLogger = Logger.getLogger("Log");
		 
			FileHandler cFileHandler;
		
						cFileHandler = new FileHandler("City-log.log", false);
					cLogger.addHandler(cFileHandler);
		 
						SimpleFormatter formatter = new SimpleFormatter();	
					cFileHandler.setFormatter(formatter);			 
		
					List<String> list = new ArrayList<String>();
       
        JSONArray JSONArray_weather = jsonObject.getJSONArray("list");
        
        if(JSONArray_weather.length() > 0){
        	for(int i=0;i<JSONArray_weather.length();i++){
            JSONObject JSONObject_weather = JSONArray_weather.getJSONObject(i);            
          
            String result_main = JSONObject_weather.getString("name");
            list.add(result_main);
            if(result_main.startsWith("T")){            	 
            cLogger.info("\nParameter name starting with T: " + result_main+"\n");             
               }
        }
        	list.stream().filter(str->str.startsWith("T")).forEach(System.out::println);
        	
        }else{
            cLogger.info("\nweather empty ");
        }
    }
}
