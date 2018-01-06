package com.example.yash1300.smartbin;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DriverHomeActivity extends AppCompatActivity implements OnMapReadyCallback {
String choice;
    private GoogleMap mMap;
    List<BinClass> bins;
    List<LatLng> route;
    ProgressDialog progressDialog;
    String finalid, finalLoc, finalCapacity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_home);
        choice = getIntent().getExtras().getString("choice");
        progressDialog = new ProgressDialog(DriverHomeActivity.this);
        progressDialog.setMessage("Loading the map...");
        progressDialog.setCancelable(false);
        bins = new ArrayList<>();
        route = new ArrayList<>();
        progressDialog.show();

        //mMap.setMyLocationEnabled(true);


        //Request for getting all the bin locations and their colors
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://192.168.43.76:8880/api/getAllBins", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                initiateMap();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");
                    Toast.makeText(DriverHomeActivity.this, message, Toast.LENGTH_SHORT).show();
                    if (success.equals("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("bins");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            bins.add(new BinClass(jsonArray.getJSONObject(i).getString("bincap"), jsonArray.getJSONObject(i).getString("bincolor"), jsonArray.getJSONObject(i).getString("binloc"), jsonArray.getJSONObject(i).getString("_id")));
                        }

                    }
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                    Toast.makeText(DriverHomeActivity.this, "An error occured", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(DriverHomeActivity.this, "An error occured", Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(DriverHomeActivity.this).add(stringRequest);
    }

    private void initiateMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Setting Delhi as the location for the map to show when it is opened
        LatLng delhi = new LatLng(28.7041, 77.1025);
        mMap.animateCamera(CameraUpdateFactory.newLatLng(delhi));



        if (!(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            mMap.setMyLocationEnabled(true);
        }
        for (int j=0;j<bins.size();j++){
            String loc = bins.get(j).getBinloc();
            String cap = bins.get(j).getBincap();
            String color = bins.get(j).getBincolor();
            final String id = bins.get(j).getId();
            Float markerColor;

            double latitude = Double.parseDouble(loc.split(",")[0]);
            double longitude = Double.parseDouble(loc.split(",")[1]);

            //Deciding the color of the marker on the map on the basis of bin capacity left
            if (color.equals("Red")) {
                route.add((new LatLng(latitude, longitude)));
                markerColor = BitmapDescriptorFactory.HUE_RED;
            } else if (color.equals("Green")) {
                markerColor = BitmapDescriptorFactory.HUE_GREEN;
            } else if (color.equals("Yellow")) {
                route.add((new LatLng(latitude, longitude)));
                markerColor = BitmapDescriptorFactory.HUE_YELLOW;
            } else {
                continue;
            }
            if (choice.equals("driver")) {
                mMap.addMarker(
                        (new MarkerOptions())
                                .position((new LatLng(latitude, longitude)))
                                .title("Bin capacity left: " + cap)
                                .icon(BitmapDescriptorFactory.defaultMarker(markerColor)));
            } else {
                mMap.addMarker(
                        (new MarkerOptions())
                                .position((new LatLng(latitude, longitude)))
                                .title("Bin ID: " + id)
                                .icon(BitmapDescriptorFactory.defaultMarker(markerColor)).snippet("Bin capacity left: "+ cap + "\nBin Geolocation: " + loc));
            }


        }

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom((new LatLng(Double.parseDouble(bins.get(0).getBinloc().split(",")[0]), Double.parseDouble(bins.get(0).getBinloc().split(",")[1]))), 10));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                finalid = "122334";
                finalLoc = "28.59349,77.55185";
                finalCapacity = "53";
                for (int i=0;i<bins.size();i++){
                    if (marker.getTitle().equals("Bin ID: "+bins.get(i).getId())){
                        finalid = bins.get(i).getId();
                        finalLoc = bins.get(i).getBinloc();
                        finalCapacity = bins.get(i).getBincap();
                        break;
                    }
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(DriverHomeActivity.this);
                TextView id, loc, cap;
                View v = getLayoutInflater().inflate(R.layout.dialog_details, null, false);
                id = v.findViewById(R.id.binIdDialog);
                loc = v.findViewById(R.id.binLocDialog);
                cap = v.findViewById(R.id.binCapacityDialog);

                id.setText("Bin ID: "+finalid);
                loc.setText("Bin Location: "+finalLoc);
                cap.setText("Bin Capacty: "+finalCapacity);
                builder.setView(v);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                return false;
            }
        });

        //Creating the efficient path for the driver to follow and hence, cover all the bins
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.addAll(route);
        polylineOptions.width(6);
        polylineOptions.color(Color.BLUE);
        mMap.addPolyline(polylineOptions);


    }
}
