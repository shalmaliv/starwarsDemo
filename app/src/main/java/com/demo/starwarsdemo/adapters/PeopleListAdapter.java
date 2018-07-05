package com.demo.starwarsdemo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.demo.starwarsdemo.R;
import com.demo.starwarsdemo.models.PeopleModel;

import java.util.ArrayList;

public class PeopleListAdapter extends BaseAdapter {
    Context ctx;
    ArrayList<PeopleModel> people;

    public PeopleListAdapter(Context ctx, ArrayList<PeopleModel> people) {
        this.people = people;
        this.ctx = ctx;

    }

    @Override
    public int getCount() {
        return people.size();
    }

    @Override
    public Object getItem(int position) {
        return people.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater;
        if (convertView == null) {
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.single_people, parent, false);
        }

        TextView category_name = (TextView) convertView.findViewById(R.id.category_name);
        category_name.setText(people.get(position).getName());

        return convertView;
    }

}
