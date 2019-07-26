package dev.raghav.icsmap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import dev.raghav.icsmap.settings.Connectivity;

public class LoginActivity extends AppCompatActivity {
EditText et_email, et_pw;
Button btn_login;

String Email, Password;
     String user_id;

    SessionManager manager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        manager = new SessionManager(LoginActivity.this);

        et_email=(EditText)findViewById(R.id.user_name);
        et_pw=(EditText)findViewById(R.id.user_pw);
        btn_login=(Button)findViewById(R.id.login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
                {

                    if (validate()) {
                        Email = et_email.getText().toString();
                        Password = et_pw.getText().toString();


                        if (Connectivity.isNetworkAvailable(LoginActivity.this)) {
                            new loginExcuteTask().execute();
                        } else {
                            Toast.makeText(LoginActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "All field mondatory", Toast.LENGTH_SHORT).show();
                    }


            }
        });




    }

    private boolean validate() {
        boolean valid = false;

        Email = et_email.getText().toString();
        Password = et_pw.getText().toString();

        if (Password.isEmpty()) {
            valid = false;
            et_pw.setError("Please enter password!");
            return false;
        } else {
            valid = true;
            et_pw.setError(null);
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            valid = false;
            et_email.setError("Please enter valid email!");
            return false;
        } else {
            valid = true;
            et_email.setError(null);
        }

        return valid;
    }


    public class loginExcuteTask extends AsyncTask<String, Void,String> {

        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(LoginActivity.this);
            dialog.show();

        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://ihisaab.com/Emptracking/api/login");

                JSONObject jsonObject2 = new JSONObject();
                jsonObject2.put("email", Email);
                jsonObject2.put("password", Password);


                Log.e("postDataParams", jsonObject2.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);


                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(jsonObject2));

                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        StringBuffer Ss = sb.append(line);
                        Log.e("Ss", Ss.toString());
                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                } else {
                    return new String("false : " + responseCode);
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {
                dialog.dismiss();

                    try {
                        JSONObject obj = new JSONObject(result);
                        String res = obj.getString("responce");

                        JSONObject data= obj.getJSONObject("data");

                 user_id=data.getString("user_id");
                String name=data.getString("name");
                String lname=data.getString("last_name");
                String email=data.getString("email");
                String dob=data.getString("dob");
                        String uname=data.getString("username");
                        String pw=data.getString("password");
                        String mobile=data.getString("mobile");
                        String img=data.getString("image");
                        String status=data.getString("status");
                        String type=data.getString("type");



                        if (!res.equals("true")) {
                            Toast.makeText(LoginActivity.this, "invalid details", Toast.LENGTH_SHORT).show();
                        }


                        else{
                            manager.setLogin(true);
                            AppPreference.setUserid(LoginActivity.this,user_id);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        Toast.makeText(LoginActivity.this, "login success", Toast.LENGTH_SHORT).show();
                    }


                    }catch (JSONException e) {

                    e.printStackTrace();
                }
                }
            }


        public String getPostDataString(JSONObject params) throws Exception {

            StringBuilder result = new StringBuilder();
            boolean first = true;

            Iterator<String> itr = params.keys();

            while (itr.hasNext()) {

                String key = itr.next();
                Object value = params.get(key);

                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(key, "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(value.toString(), "UTF-8"));

            }
            return result.toString();
        }
    }





