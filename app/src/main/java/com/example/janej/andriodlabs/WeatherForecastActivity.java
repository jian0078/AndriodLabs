package com.example.janej.andriodlabs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WeatherForecastActivity extends Activity {
    private ProgressBar progressBar;
    private TextView currentTemperature;
    private  TextView minTemperature;
    private TextView maxTemperature;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        currentTemperature=findViewById(R.id.currentTemperatureValue);
        minTemperature=findViewById(R.id.minTemperatureValue);
        maxTemperature=findViewById(R.id.maxTemperatureValue);
        imageView=findViewById(R.id.imageView3);

        ForecastQuery fq=new ForecastQuery();
        fq.execute();
    }

    public class  ForecastQuery extends AsyncTask<String, Integer ,String >{
        String max;
        String min;
        String current;
        Bitmap bitmap;
        String iconName;
        String ACTIVITY_NAME="weatherForecastActivity";

        @Override
        protected String doInBackground(String... strings) {
            String urlString="http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric";
            URL url = null;
            try {
                url = new URL(urlString);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                InputStream in= conn.getInputStream();

                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(in, null);
                parser.nextTag();

                while (parser.next()!=XmlPullParser.END_DOCUMENT){
                    if(parser.getEventType()!= XmlPullParser.START_TAG){
                        continue;
                    }
                    if(parser.getName().equals("temperature")){
                        current=parser.getAttributeValue(null,"value");
                        publishProgress(25);
                        android.os.SystemClock.sleep(500);

                        max=parser.getAttributeValue(null,"max");
                        publishProgress(50);
                        android.os.SystemClock.sleep(500);

                        min=parser.getAttributeValue(null,"min");
                        publishProgress(75);
                        android.os.SystemClock.sleep(500);
                    }

                    if(parser.getName().equals("weather")){
                        iconName=parser.getAttributeValue(null,"icon");
                    }
                }

                conn.disconnect();


// Get weather immage

                String immageFile=iconName+ ".png";
                if(fileExistance(immageFile)){
                    FileInputStream fis = null;
                    try {    fis = openFileInput(immageFile);   }
                    catch (FileNotFoundException e) {    e.printStackTrace();  }
                    bitmap= BitmapFactory.decodeStream(fis);
                    Log.i(ACTIVITY_NAME,"file is waether image "+ immageFile);
                }
                else{
                    //download
                    URL immageUrl= new URL("http://openweathermap.org/img/w/"+immageFile);
                    Log.i(ACTIVITY_NAME,"Searching "+ immageFile);
                    bitmap=HttpUtils.getImage(immageUrl);
                    //save to harddisk
                    FileOutputStream outputStream = openFileOutput( immageFile, Context.MODE_PRIVATE);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                    outputStream.flush();
                    outputStream.close();

                    publishProgress(100);
                }

            } catch (IOException e) {

                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

            return null;
        }


        public boolean fileExistance(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }

        @Override
        protected void onProgressUpdate(Integer...value){
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(value[0]);
        }

        @Override
        protected void onPostExecute(String result){
            maxTemperature.setText(max);
            minTemperature.setText(min);
            currentTemperature.setText(current);
            imageView.setImageBitmap(bitmap);
            progressBar.setVisibility(View.INVISIBLE);

        }
    }


    static class HttpUtils {
        public static Bitmap getImage(URL url) {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    return BitmapFactory.decodeStream(connection.getInputStream());
                } else
                    return null;
            } catch (Exception e) {
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
    }



}


