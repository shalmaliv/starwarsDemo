package com.demo.starwarsdemo.ws;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.List;

public class ServiceHandler {

    static String response = null;
    public final static int GET = 1;
    public final static int POST = 2;
    public final static int PUT = 3;

    public ServiceHandler() {

    }

    public String makeServiceCall2(String url, int method,
                                   String params, String header_string) {
        try {
            // http client
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpParams params1 = httpClient.getParams();
            HttpConnectionParams.setConnectionTimeout(params1, 15000);
            HttpConnectionParams.setSoTimeout(params1, 15000);
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;

            // Checking http request method type
            if (method == POST) {
                HttpPost httpPost = new HttpPost(url);
                // adding post params
                if (params != null) {
                    StringEntity se = new StringEntity(params);
                    httpPost.setEntity(se);
                    //httpPost.addHeader("Authorization", Authtoken);
                    httpPost.addHeader("Content-type", "application/json");
                }

                httpResponse = httpClient.execute(httpPost);

            } else if (method == GET) {

                HttpGet httpGet = new HttpGet(url);
                //httpGet.addHeader("Authorization", Authtoken);
                httpResponse = httpClient.execute(httpGet);

            } else if (method == PUT) {
                HttpPut httput = new HttpPut(url);
                if (params != null) {
                    StringEntity se = new StringEntity(params);
                    httput.setEntity(se);
                    if (header_string != null) {
                        httput.addHeader("Header", header_string);
                    }
                    httput.addHeader("Content-type", "application/json");
                }
                httpResponse = httpClient.execute(httput);

            }
            httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);

        } catch (Exception e) {
            e.printStackTrace();
            response = "{\"count\":-1}";
        }

        return response;

    }
}
