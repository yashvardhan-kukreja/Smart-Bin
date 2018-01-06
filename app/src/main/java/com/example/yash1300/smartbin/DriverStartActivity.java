package com.example.yash1300.smartbin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DriverStartActivity extends AppCompatActivity {
EditText email, password;
Button login, signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_start);

        email = findViewById(R.id.driverLogInEmail);
        password = findViewById(R.id.driverLogInPassword);
        login = findViewById(R.id.driverLogIn);
        signup = findViewById(R.id.driverSignUp);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Request for authentication of the driver
                StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.43.76:8880/api/authenticate", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");
                            Toast.makeText(DriverStartActivity.this, message, Toast.LENGTH_LONG).show();
                            if (success.equals("true")){
                                Intent i = new Intent(DriverStartActivity.this, DriverHomeActivity.class);
                                i.putExtra("choice", "driver");
                                startActivity(i);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DriverStartActivity.this, "An error occured", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(DriverStartActivity.this, "An error occured", Toast.LENGTH_LONG).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("email", email.getText().toString());
                        params.put("password", password.getText().toString());
                        return params;
                    }
                };
                Volley.newRequestQueue(DriverStartActivity.this).add(stringRequest);
                //Request end
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //If the driver wants to create a new account
                startActivity(new Intent(DriverStartActivity.this, DriverRegisterActivity.class));
            }
        });
    }
}
