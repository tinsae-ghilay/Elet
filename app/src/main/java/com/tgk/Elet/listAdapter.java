package com.tgk.Elet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.ArrayList;

public class listAdapter extends BaseAdapter {
    private ArrayList<String> dates;
    private  ArrayList<String> events;
    private final LayoutInflater inflater;
    private final boolean calendar;

    listAdapter(Context context, ArrayList<String> dates, ArrayList<String> events,boolean calendar) {
        this.dates = dates;
        this.events = events;
        inflater = (LayoutInflater.from(context));
        this.calendar=calendar;
    }

    @Override
    public int getCount() {
        return dates.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        vHolder holder;
        if (view == null) {
            holder=new vHolder();
            if (calendar){
                view=inflater.inflate(R.layout.list_adapter,parent,false);
            }else{
                view=inflater.inflate(R.layout.holidays_list,parent,false);
            }
            view.setTag(holder);
        }else{
            holder = (vHolder) view.getTag();
        }
        holder.dates=view.findViewById(R.id.dates_list);
        holder.events=view.findViewById(R.id.events_list);
        holder.dates.setText(dates.get(i));
        holder.events.setText(events.get(i));
        return view;
    }
    static class vHolder{
        TextView dates;
        TextView events;
    }
    public void updateData(ArrayList<String> dates, ArrayList<String> events){
        this.dates=dates;
        this.events=events;
        this.notifyDataSetChanged();
    }
}

