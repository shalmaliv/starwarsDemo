package com.demo.starwarsdemo.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.starwarsdemo.R;
import com.demo.starwarsdemo.async.AsyncApiCall;
import com.demo.starwarsdemo.constants.AppConstant;
import com.demo.starwarsdemo.constants.AppGlobal;
import com.demo.starwarsdemo.constants.WsConstants;
import com.demo.starwarsdemo.interfaces.WsResponseListener;
import com.demo.starwarsdemo.models.PeopleResponseModel;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SplashActivity extends AppCompatActivity implements WsResponseListener{

    ProgressBar progressBar;
    private static ObjectMapper mapper = null;
    private static final Lock lock = new ReentrantLock();
    Button tryAgainBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        tryAgainBtn = (Button) findViewById(R.id.tryAgainBtn);
        runAnimation();

        tryAgainBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                tryAgainBtn.setVisibility(View.GONE);
                getStarWarsData();
            }
        });

    }

    private void runAnimation()
    {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.animate_scale);
        a.reset();
        TextView tv = (TextView) findViewById(R.id.txt);
        tv.clearAnimation();

        a.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                progressBar.setVisibility(View.VISIBLE);
                tryAgainBtn.setVisibility(View.GONE);
                getStarWarsData();
            }
        });
        tv.startAnimation(a);

    }

    private void getStarWarsData(){
        JSONObject parameters = new JSONObject();

        if (AppGlobal.isNetwork(SplashActivity.this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                new AsyncApiCall(SplashActivity.this, WsConstants.WS_GET_PEOPLE_DATA, WsConstants.GET,
                        parameters.toString(), null, false).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, WsConstants.HTTP + WsConstants.DOMAIN_URL
                        + WsConstants.WS_GET_PEOPLE_DATA);
            } else {
                new AsyncApiCall(SplashActivity.this, WsConstants.WS_GET_PEOPLE_DATA, WsConstants.GET,
                        parameters.toString(), null,false).execute(WsConstants.HTTP + WsConstants.DOMAIN_URL
                        + WsConstants.WS_GET_PEOPLE_DATA);
            }
        } else {
            progressBar.setVisibility(View.GONE);
            tryAgainBtn.setVisibility(View.VISIBLE);
            AppGlobal.showToast(SplashActivity.this, getResources().getString(R.string.network_not_available), 2);
        }

    }

    @Override
    public void onDelieverResponse(String serviceType, String data, Exception error) {
        if (error == null) {
            if (serviceType.equalsIgnoreCase(WsConstants.WS_GET_PEOPLE_DATA)) {
                try {

                    PeopleResponseModel response = getMapper().readValue(data, PeopleResponseModel.class);

                    if(response.getCount() != -1) {
                        AppConstant.peopleList = response.getResults();
                        AppConstant.totalCount = response.getCount();
                        AppConstant.next = response.getNext();
                        AppConstant.previous = response.getPrevious();

                        Intent i = new Intent(this, PeopleListActivity.class);
                        startActivity(i);
                        progressBar.setVisibility(View.INVISIBLE);
                        finish();
                    }
                    else{
                        progressBar.setVisibility(View.GONE);
                        tryAgainBtn.setVisibility(View.VISIBLE);
                        Toast.makeText(this,"Error Occured",Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else{
            progressBar.setVisibility(View.GONE);
            tryAgainBtn.setVisibility(View.VISIBLE);
            Toast.makeText(this,"Error Occured",Toast.LENGTH_SHORT).show();
        }
    }

    public static synchronized ObjectMapper getMapper() {

        if (mapper != null) {
            return mapper;
        }
        try {
            lock.lock();
            if (mapper == null) {
                mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            }
            lock.unlock();
        } catch (Exception ex) {
            if (ex != null)
                ex.printStackTrace();
        }
        return mapper;
    }
}
