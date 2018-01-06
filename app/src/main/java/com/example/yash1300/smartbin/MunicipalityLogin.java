package com.example.yash1300.smartbin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MunicipalityLogin extends AppCompatActivity {
EditText email, password;
Button login;
ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_municipality_login);

        email = findViewById(R.id.munLogInEmail);
        password = findViewById(R.id.munLogInPassword);
        login = findViewById(R.id.munLogIn);
        progressDialog = new ProgressDialog(MunicipalityLogin.this);
        progressDialog.setMessage("Logging in...");
        progressDialog.setCancelable(false);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                if (email.getText().toString().equals("municipality1998@gmail.com") && password.getText().toString().equals("hello")){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Intent i = new Intent(MunicipalityLogin.this, DriverHomeActivity.class);
                        i.putExtra("choice", "mun");
                        startActivity(i);
                    }
                }, 2000);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            Toast.makeText(MunicipalityLogin.this, "Wrong password entered", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }, 2000);
                }
                /*StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.43.76/api/authenticate/municipality", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");
                            Toast.makeText(MunicipalityLogin.this, message, Toast.LENGTH_LONG).show();
                            if (success.equals("true")){
                                Intent i = new Intent(MunicipalityLogin.this, DriverHomeActivity.class);
                                i.putExtra("choice", "mun");
                                startActivity(i);
                            }
                        } catch (JSONException e) {
                            Toast.makeText(MunicipalityLogin.this, "An error occured", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MunicipalityLogin.this, "An error occured", Toast.LENGTH_LONG).show();
                        error.printStackTrace();
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
                Volley.newRequestQueue(MunicipalityLogin.this).add(stringRequest);*/
            }
        });
    }
}
