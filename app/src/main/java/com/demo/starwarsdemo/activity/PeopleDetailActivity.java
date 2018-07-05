package com.demo.starwarsdemo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.demo.starwarsdemo.R;
import com.demo.starwarsdemo.models.PeopleModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PeopleDetailActivity extends AppCompatActivity {

    PeopleModel selectedPeople;
    TextView name_txt,height_txt,weight_txt,record_date_txt;
    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    SimpleDateFormat format2 = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_detail);

        // initialize
        name_txt = (TextView) findViewById(R.id.name_txt);
        height_txt = (TextView) findViewById(R.id.height_txt);
        weight_txt = (TextView) findViewById(R.id.weight_txt);
        record_date_txt = (TextView) findViewById(R.id.record_date_txt);

        Bundle b = getIntent().getExtras();
        if (b != null)
            selectedPeople = (PeopleModel) b.getSerializable("selectedCharacter");


        if(selectedPeople != null) {
            name_txt.setText(selectedPeople.getName());
            height_txt.setText(String.valueOf(selectedPeople.getHeight()));
            weight_txt.setText(String.valueOf(selectedPeople.getMass()));

            try {
                Date recordDate = format1.parse(selectedPeople.getCreated());
                String formatedDate = format2.format(recordDate);
                record_date_txt.setText(formatedDate);
            }catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
