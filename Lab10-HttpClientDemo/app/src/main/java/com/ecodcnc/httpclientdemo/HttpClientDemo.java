package com.ecodcnc.httpclientdemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class HttpClientDemo extends Activity {

    private static final String HOST = "http://javaplays-restjava-mobilestarterkit-template.mybluemix.net/";
    private TextView tvSum = null;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);
        setContentView(R.layout.main);
        tvSum = (TextView) findViewById(R.id.txtSum);
    }

    public void clearText(View v) {
        TextView tvSum = (TextView) v;
        tvSum.setText("");
    }

    public void useGet(View v) throws Exception {
        String response = new String();
        try {
            URL url = new URL(HOST);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            int HttpResult = conn.getResponseCode();

            // read the response
            System.out.println("Response Code: " + conn.getResponseCode());
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                response = readResponse(conn.getInputStream());
            }else {
                response = conn.getResponseMessage();
                System.out.println(conn.getResponseMessage());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            tvSum.setText(response);
        }
    }

    public void usePost(View v) {
        String response = new String();
        try {
            URL object = new URL(HOST + "api/service/login");
            HttpURLConnection con = (HttpURLConnection) object.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestMethod("POST");

            JSONObject cred = new JSONObject();
            cred.put("user_id", "admin");
            cred.put("password", "password");
            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());

            wr.write(cred.toString());
            wr.flush();

            //display what returns the POST request
            int HttpResult = con.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                response = readResponse(con.getInputStream());
                System.out.println("" + response.toString());
            } else {
                response = con.getResponseMessage();
                System.out.println(con.getResponseMessage());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            tvSum.setText(response);
        }
    }


    /**
     * processes an HttpResponse and returns a String
     *
     * @param res - an HttpResponse object
     * @return String representation of the response object
     */
    private String readResponse(InputStream res) {

        StringBuffer response = new StringBuffer();
        BufferedReader input = null;


        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(res, "utf-8"));

            String line = null;
            while ((line = br.readLine()) != null) {
                response.append(line + "\n");
            }
            br.close();
            System.out.println("" + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }
}