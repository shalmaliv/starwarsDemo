package com.demo.starwarsdemo.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.starwarsdemo.R;
import com.demo.starwarsdemo.adapters.PeopleListAdapter;
import com.demo.starwarsdemo.async.AsyncApiCall;
import com.demo.starwarsdemo.constants.AppConstant;
import com.demo.starwarsdemo.constants.AppGlobal;
import com.demo.starwarsdemo.constants.WsConstants;
import com.demo.starwarsdemo.interfaces.WsResponseListener;
import com.demo.starwarsdemo.models.PeopleResponseModel;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PeopleListActivity extends AppCompatActivity implements WsResponseListener{

    ListView listView;
    Button prevBtn,nextBtn;
    TextView no_records_txt;
    PeopleListAdapter adapter;
    ProgressBar progressBar;
    private static ObjectMapper mapper = null;
    private static final Lock lock = new ReentrantLock();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_list);

        listView = (ListView) findViewById(R.id.listview);
        prevBtn = (Button) findViewById(R.id.prevBtn);
        nextBtn = (Button) findViewById(R.id.nextBtn);
        no_records_txt = (TextView) findViewById(R.id.no_records_txt);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        if(AppConstant.peopleList == null || AppConstant.peopleList.size() == 0){
            no_records_txt.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            prevBtn.setVisibility(View.GONE);
            nextBtn.setVisibility(View.GONE);
        }else {
            no_records_txt.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            prevBtn.setVisibility(View.VISIBLE);
            nextBtn.setVisibility(View.VISIBLE);
            adapter = new PeopleListAdapter(this, AppConstant.peopleList);
            listView.setAdapter(adapter);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(PeopleListActivity.this,PeopleDetailActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("selectedCharacter", AppConstant.peopleList.get(position));
                i.putExtras(b);
                startActivity(i);
            }
        });

        prevBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(AppConstant.previous == null){
                    Toast.makeText(PeopleListActivity.this,"You have reached start of list!",Toast.LENGTH_SHORT).show();
                }else{
                    getStarWarsData(AppConstant.previous);
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(AppConstant.next == null){
                    Toast.makeText(PeopleListActivity.this,"You have reached end of list!",Toast.LENGTH_SHORT).show();
                }else{
                    getStarWarsData(AppConstant.next);
                }
            }
        });

    }

    private void getStarWarsData(String url){
        progressBar.setVisibility(View.VISIBLE);
        JSONObject parameters = new JSONObject();

        if (AppGlobal.isNetwork(PeopleListActivity.this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                new AsyncApiCall(PeopleListActivity.this, WsConstants.WS_GET_PEOPLE_DATA, WsConstants.GET,
                        parameters.toString(), null, false).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
            } else {
                new AsyncApiCall(PeopleListActivity.this, WsConstants.WS_GET_PEOPLE_DATA, WsConstants.GET,
                        parameters.toString(), null,false).execute(url);
            }
        } else {
            AppGlobal.showToast(PeopleListActivity.this, getResources().getString(R.string.network_not_available), 2);
        }

    }

    @Override
    public void onDelieverResponse(String serviceType, String data, Exception error) {
        if (error == null) {
            if (serviceType.equalsIgnoreCase(WsConstants.WS_GET_PEOPLE_DATA)) {
                try {

                    PeopleResponseModel response = getMapper().readValue(data, PeopleResponseModel.class);
                    AppConstant.peopleList = new ArrayList<>();
                    AppConstant.peopleList.addAll(response.getResults());
                    AppConstant.totalCount = response.getCount();
                    AppConstant.next = response.getNext();
                    AppConstant.previous = response.getPrevious();

                    adapter = new PeopleListAdapter(this, AppConstant.peopleList);
                    listView.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
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
