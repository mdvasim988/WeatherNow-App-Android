package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    EditText city;
    TextView feel, felt, temp, ci;
    private final String url = "http://api.openweathermap.org/data/2.5/weather";
    private final String appid = "Enter your API id here";
    DecimalFormat df = new DecimalFormat("#");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        city = findViewById(R.id.editText);
        feel = findViewById(R.id.feel);
        felt = findViewById(R.id.felt);
        temp = findViewById(R.id.temp);
        ci = findViewById(R.id.cityw);
        populate("Guntur");
    }

    public void getUpdate(View view) {
        String cityf = city.getText().toString().trim();
        populate(cityf);
    }

    public void populate(String cityf)
    {
        String tempurl = "";
        if(cityf.equals(""))
        {
            Toast.makeText(MainActivity.this,"Please enter city name",Toast.LENGTH_SHORT).show();
        }
        else
        {
            tempurl = url + "?q=" + cityf + "&appid=" + appid;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, tempurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Log.d("test", response);
                String output = "";
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("weather");
                    JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                    String description = jsonObjectWeather.getString("description");
                    JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                    double tempr = jsonObjectMain.getDouble("temp") - 273.15;
                    double feelslike = jsonObjectMain.getDouble("feels_like") - 273.15;
                    ci.setText(jsonObject.getString("name"));
                    feel.setText("Feels Like "+df.format(feelslike)+(char) 0x00B0);
                    temp.setText(df.format(tempr)+ (char) 0x00B0+"");
                    felt.setText(description);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString().trim(),Toast.LENGTH_SHORT).show();
                Log.d("my",error.toString());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}