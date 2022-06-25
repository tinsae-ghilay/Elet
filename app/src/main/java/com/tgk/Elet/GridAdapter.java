package com.tgk.Elet;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.Holder> {
    boolean isCurrentMonth,show_gregorian;
    int WEEKEND=1;
    int WEEKDAY=2;
    DateItem[] dateItems;
    int month,current_date;

    GridAdapter(DateItem[] dateItems,int month,int current_date, boolean isCurrentMonth,boolean show_gregorian){
        this.isCurrentMonth=isCurrentMonth;
        this.show_gregorian=show_gregorian;
        this.dateItems =dateItems;
        this.month=month;
        this.current_date=current_date;
    }
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        View v = LayoutInflater.from(context)
                .inflate(R.layout.grid_item, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        // Array that contains all info pertaining to the date.
        DateItem day= dateItems[position];
        int date= day.getDate();
        // set textViews
        String string_geez_date=date+"";
        //--------------
        if (show_gregorian){
            holder.big.setText(new SpannedStrings(date,day.getGregorian_date()).getString());
        }else {
            holder.big.setText(string_geez_date);
        }
        // dates of this month
        if (!day.isOverFlow()){
            if (holder.dateClick==null){
                holder.dateClick = v -> ((MainActivity)holder.itemView.getContext()).showSnack(month,date);
            }
            // weekend columns of table
            boolean weekEnds=((position% 7==0)||((position+1)%7==0));
            // holidays and plans--------------
            boolean is_holy_day=day.isHoliday();
            //----------------------------
            boolean is_this_date=date==current_date;
            boolean is_today=is_this_date&&isCurrentMonth;
            //---------------------------------
            holder.big.setOnClickListener(holder.dateClick);
            //weekends,
            if (weekEnds&&!is_holy_day&&!is_today){
                holder.big.setTextColor(holder.Neon);
                //markViews(holder.big,Neon);
            }
            // holidays
            // may be do it after a while may be 1 sec..
            if (is_holy_day){
                holder.big.setTextColor(holder.red);
            }
            // date today
            if (is_today){
                holder.big.setBackgroundColor(holder.Neon);
                if (!is_holy_day){
                    holder.big.setTextColor(Color.WHITE);
                    //markViews(holder.big,Color.WHITE);
                }
            }
        }else{
            // dates in previous and next month
            markViews(holder.big,holder.super_light_gray);
        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return dateItems.length;
    }

   @Override
    public int getItemViewType(int position) {
        if ((position% 7==0)||((position+1)%7==0)){
            return WEEKEND;

        }else{
            return WEEKDAY;
        }
    }

    private void markViews(TextView b, int color){
        b.setTextColor(color);
    }
    //Update adapter data
    public void changeData(DateItem[] dateItems,int month,boolean isCurrentMonth){
        this.dateItems = dateItems;
        this.isCurrentMonth=isCurrentMonth;
        this.month=month;
        notifyDataSetChanged();
    }

    public static class Holder  extends RecyclerView.ViewHolder{
        GridViewItem big;
        View.OnClickListener dateClick;
        int Neon,super_light_gray,red;
        public Holder(@NonNull View itemView) {
            super(itemView);
            big=itemView.findViewById(R.id.big_text);
            Neon= itemView.getResources().getColor(R.color.Neon);
            super_light_gray= itemView.getResources().getColor(R.color.lightGrey);
            red= itemView.getResources().getColor(R.color.red_ish);
        }
    }

}