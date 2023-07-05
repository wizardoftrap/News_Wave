package com.shivprakash.newswave;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    TextView locationCity, textRain, currentTempreture, time, date;
    ImageView icon;
    ProgressBar load;
    Button settings;
    FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ID = 44;
    protected double latitude;
    protected double longitude;
    String cdate, city;
    ConstraintLayout dayOrnight;
    RequestQueue requestQueueN;
    RequestQueue requestQueueW;
    RecyclerView myRecyclerView;
    private static final int REQUEST_LOCATION = 1;
    ArrayList<String> title, imgUrl, link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationCity = findViewById(R.id.locationCity);
        currentTempreture = findViewById(R.id.currentTempreture);
        load=findViewById(R.id.progressBar);
        textRain = findViewById(R.id.textRain);
        icon = findViewById(R.id.icon);
        date = findViewById(R.id.date);
        dayOrnight = findViewById(R.id.constraintLayout);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        settings = findViewById(R.id.settings);
        myRecyclerView = findViewById(R.id.news);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        time = findViewById(R.id.time);
        title = new ArrayList<>();
        link = new ArrayList<>();
        imgUrl = new ArrayList<>();
        final int[] checkedItem = {-1};
        requestQueueN = Volley.newRequestQueue(MainActivity.this);
        requestQueueW = Volley.newRequestQueue(MainActivity.this);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setIcon(R.drawable.map);
                alertDialog.setTitle("Choose location");
                final int[] option = {12};
                final String[] listItems = new String[]{"Enter city manually", "Detect your city"};
                alertDialog.setSingleChoiceItems(listItems, checkedItem[0], (dialog, which) -> {
                    checkedItem[0] = which;
                    option[0] = which;
                    Arrays.fill(checkedItem, -1);
                });
                alertDialog.setPositiveButton("OK", (dialog, which) -> {
                    if (option[0] == 0) {
                        editLocation();
                    } else {
                        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            getLocation();
                        } else {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                        }
                        //gps
                    }
                    dialog.dismiss();
                });
                alertDialog.setNegativeButton("Cancel", (dialog, which) -> {
                });
                AlertDialog customAlertDialog = alertDialog.create();
                customAlertDialog.show();
                customAlertDialog.getWindow().setBackgroundDrawableResource(R.drawable.round_bg);
            }
        });
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
        getNews();
    }

    private void getData(String Rcity) {
        String myUrl = "https://weatherapi-com.p.rapidapi.com/current.json?q=" + Rcity;
        StringRequest myRequest = new StringRequest(Request.Method.GET, myUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject myJsonObject = new JSONObject(response.toString());
                            // Toast.makeText(MainActivity.this, myJsonObject.toString(), Toast.LENGTH_SHORT).show();
                            //Location
                            JSONObject location = myJsonObject.getJSONObject("location");
                            String name = location.getString("name");
                            String region = location.getString("region");
                            String country = location.getString("country");
                            String datetime = location.getString("localtime");
                            //Current Weather
                            JSONObject current = myJsonObject.getJSONObject("current");
                            int tempreture = current.getInt("temp_c");
                            int isDay = current.getInt("is_day");
                            JSONObject condition = current.getJSONObject("condition");
                            String textrain = condition.getString("text");
                            String iconurl = "https:" + condition.getString("icon");
                            //update UI
                            locationCity.setText(name + "," + region + "," + country);
                            currentTempreture.setText(tempreture + "°C");
                            textRain.setText(textrain);
                            cdate = datetime.substring(0, 10);
                            // ctime = datetime.substring(11,16);
                            date.setText(cdate);
                            //  time.setText(ctime);
                            Glide.with(MainActivity.this).load(iconurl).into(icon);
                            if (isDay == 1) {
                                locationCity.setTextColor(Color.parseColor("#ffffe0"));
                                date.setTextColor(Color.parseColor("#ffffe0"));
                                // time.setTextColor(Color.parseColor("#080808"));
                                textRain.setTextColor(Color.parseColor("#ffffe0"));
                                currentTempreture.setTextColor(Color.parseColor("#ffffe0"));
                                //textView.setTextColor(Color.parseColor("#ffffe0"));
                                //newsTitle.setTextColor(Color.parseColor("#080808"));
                                dayOrnight.setBackgroundResource(R.drawable._a2447940eabbeea9907102e4a4ed078);
                            } else {
                                locationCity.setTextColor(Color.parseColor("#ffffe0"));
                                date.setTextColor(Color.parseColor("#ffffe0"));
                                // time.setTextColor(Color.parseColor("#f5f5dc"));
                                textRain.setTextColor(Color.parseColor("#ffffe0"));
                                currentTempreture.setTextColor(Color.parseColor("#ffffe0"));
                                //textView.setTextColor(Color.parseColor("#ffffe0"));
                                //newsTitle.setTextColor(Color.parseColor("#080808"));
                                dayOrnight.setBackgroundResource(R.drawable._25f8a868d384c10060527e43f87d99b);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(MainActivity.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("X-RapidAPI-Key", "My_rapid_API_Key");
                headers.put("X-RapidAPI-Host", "weatherapi-com.p.rapidapi.com");
                return headers;
            }
        };
        requestQueueN.add(myRequest);
    }

    public void getNews() {
        String url = "https://newsdata2.p.rapidapi.com/news?country=in&category=top&language=en";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArr = response.getJSONArray("results");
                    for (int i = 0; i < jsonArr.length(); i++) {
                        JSONObject newsDetail = jsonArr.getJSONObject(i);
                        String imgUrlR = newsDetail.getString("image_url");
                        String titleR = newsDetail.getString("title");
                        String linkR = newsDetail.getString("link");
                        imgUrl.add(imgUrlR);
                        title.add(titleR);
                        link.add(linkR);
                    }
                    load.setVisibility(View.GONE);
                    CustomAdaptor myCustomAdapter = new CustomAdaptor(MainActivity.this, title, imgUrl, link);
                    myRecyclerView.setAdapter(myCustomAdapter);
                    myRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    //  Toast.makeText(MainActivity.this, title.get(9), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("fine", "Something Wrong" + error);
                Toast.makeText(MainActivity.this, "Something Wrong " + error, Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("X-RapidAPI-Key", "My_rapid_API_Key");
                headers.put("X-RapidAPI-Host", "newsdata2.p.rapidapi.com");
                return headers;
            }
        };
        requestQueueW.add(request);
    }

    public void editLocation() {
        AlertDialog.Builder builderE = new AlertDialog.Builder(MainActivity.this);
        builderE.setTitle("City Name");
        final View customLayout = getLayoutInflater().inflate(R.layout.location_edit_layout, null);
        builderE.setView(customLayout);
        builderE.setPositiveButton("OK", (dialogE, whichE) -> {
            EditText editText = customLayout.findViewById(R.id.locationEditText);
            sendDialogDataToActivity(editText.getText().toString());
        });
        AlertDialog dialogE = builderE.create();
        dialogE.show();
        dialogE.getWindow().setBackgroundDrawableResource(R.drawable.round_bg);
    }

    private void sendDialogDataToActivity(String data) {
        // Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
        city = data;
        getData(city);
    }

    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
              Location location=task.getResult();
              if (location!=null){

                  try {
                      Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                      List<Address> addressList = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                      getData(addressList.get(0).getLocality());
                      Toast.makeText(MainActivity.this, "Current Locality:"+addressList.get(0).getLocality(), Toast.LENGTH_SHORT).show();
                  } catch (IOException e) {
                      e.printStackTrace();
                  }
              }
            }
        });
    }
    public void locationDetails(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }
}
