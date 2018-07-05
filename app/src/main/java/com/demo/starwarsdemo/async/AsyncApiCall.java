package com.demo.starwarsdemo.async;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.demo.starwarsdemo.interfaces.WsResponseListener;
import com.demo.starwarsdemo.ws.ServiceHandler;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;

import java.util.List;


public class AsyncApiCall extends AsyncTask<String, Void, String> {

    Context mContext;
    String serviceType;
    int method;
    private Activity activity;
    private static final int PERMISSION_REQUEST_CODE = 1;

    private final String LOG_TAG = "AsyncApiCall";

    Exception error = null;
    WsResponseListener wsResponseListener;
    List<NameValuePair> nameValuePair;
    boolean isloaderEnable;
    private HttpResponse response;
    ProgressDialog progress;
    String jsonObject;
    String header;

    //without token
    public AsyncApiCall(Context mContext, String serviceType, int method,
                        List<NameValuePair> nameValuePair, boolean isloaderEnable) {

        this.mContext = mContext;
        this.serviceType = serviceType;
        this.nameValuePair = nameValuePair;
        this.isloaderEnable = isloaderEnable;
        this.method = method;
        wsResponseListener = (WsResponseListener) mContext;

    }

    //with Hash
    public AsyncApiCall(Context mContext, String serviceType, int method,
                        String jsonObject, String header, boolean isloaderEnable) {

        this.mContext = mContext;
        this.serviceType = serviceType;
        this.isloaderEnable = isloaderEnable;
        this.method = method;
        this.jsonObject = jsonObject;
        this.header = header;
        wsResponseListener = (WsResponseListener) mContext;

    }

    //fragment with Hash
    public AsyncApiCall(Context mContext, Fragment fragment, String serviceType, int method,
                        String jsonObject, String header, boolean isloaderEnable) {

        this.mContext = mContext;
        this.serviceType = serviceType;
        this.jsonObject = jsonObject;
        this.isloaderEnable = isloaderEnable;
        this.method = method;
        this.header = header;
        wsResponseListener = (WsResponseListener) fragment;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (isloaderEnable) {
            progress = ProgressDialog.show(mContext, "Please Wait",
                    "Loading...", true);
        }

    }

    @Override
    protected String doInBackground(String... params) {

        try {
            if (jsonObject != null) {
                String res =  new ServiceHandler().makeServiceCall2(params[0], method, jsonObject, header);
                return res;
            }
        } catch (Exception e) {

            e.printStackTrace();
            error = e;
            Log.e(LOG_TAG, "Error: " + e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        try {
            if (isloaderEnable) {
                progress.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        wsResponseListener.onDelieverResponse(serviceType, result, error);
    }




}