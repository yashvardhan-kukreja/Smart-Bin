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

public class DriverRegisterActivity extends AppCompatActivity {
EditText name, email, truckno, aadharno, mobno, pass;
Button signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_register);

        name = findViewById(R.id.driverSignUpName);
        email = findViewById(R.id.driverSignUpEmail);
        truckno = findViewById(R.id.driverSignUpTruckNum);
        aadharno = findViewById(R.id.driverSignUpAadhar);
        mobno = findViewById(R.id.driverSignUpMobileNum);
        pass = findViewById(R.id.driverSignUpPassword);
        signup = findViewById(R.id.driverRegister);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Request for registering a new driver
                StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.43.76:8880/api/register", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success =  jsonObject.getString("success");
                            String message = jsonObject.getString("message");
                            Toast.makeText(DriverRegisterActivity.this, message, Toast.LENGTH_LONG).show();
                            if (success.equals("true")){
                                startActivity(new Intent(DriverRegisterActivity.this, DriverStartActivity.class));
                            }
                        } catch (JSONException e) {
                            Toast.makeText(DriverRegisterActivity.this, "An error occured", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(DriverRegisterActivity.this, "An error occured", Toast.LENGTH_LONG).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("name", name.getText().toString());
                        params.put("email", email.getText().toString());
                        params.put("truckno", truckno.getText().toString());
                        params.put("aadharno", aadharno.getText().toString());
                        params.put("mobno", mobno.getText().toString());
                        params.put("password", pass.getText().toString());
                        return params;
                    }
                };
                Volley.newRequestQueue(DriverRegisterActivity.this).add(stringRequest);
                //Request end


            }
        });
    }
}
