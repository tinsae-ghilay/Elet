package com.tgk.Elet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SingleItemAdapter extends BaseAdapter {
    String[] data;
    LayoutInflater inflater;
    SingleItemAdapter(String [] data, Context context){
        this.data=data;
        this.inflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        vHolder holder= new vHolder();
        if (convertView==null){
            convertView=inflater.inflate(R.layout.one_item,parent,false);
            convertView.setTag(holder);

        }
        else{
            holder=(vHolder)convertView.getTag();
        }
        holder.tv=convertView.findViewById(R.id.text);
        holder.tv.setText(data[position]);
        return convertView;
    }
    static class vHolder {
        TextView tv;
    }
}


