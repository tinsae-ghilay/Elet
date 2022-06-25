package com.tgk.Elet;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    int d,m,y,span;
    boolean eritrean,tigraian,show_gregorian;


    RecyclerAdapter(int span,boolean eritrean,boolean tigraian,boolean show_gregorian)  {

        super();
        getDate();
        this.span=span;
        this.eritrean=eritrean;
        this.tigraian=tigraian;
        this.show_gregorian=show_gregorian;
    }

    private void getDate()  {
        GeezDate date=GeezDate.now();
        this.y = date.getYear();
        this.m = date.getMonth();
        this.d = date.getDayOfMonth();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup,int i) {

        View v = LayoutInflater.from(viewGroup.getContext())
                 .inflate(R.layout.month_item, viewGroup, false);
        return new ViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        CalendarMonth calendarMonth;
        MonthItem monthItem;
        DateItem[] dates= null;
        //Month and year of displayed  table.
        int month = 0;
        int year;
        boolean isCurrentMonth = false;
        try {
            calendarMonth = new CalendarMonth(i,span,eritrean,tigraian,show_gregorian);
            monthItem = calendarMonth.getMonthItem();
            dates= monthItem.getDates();
            month=monthItem.getMonth();
            year=monthItem.getYear();
            isCurrentMonth=(month==m&&year==y);
        } catch (Exception ignored) {

        }
        //adapter.
        if (viewHolder.adapter!=null){
            viewHolder.adapter.changeData(dates,month,isCurrentMonth);
        }else {
            viewHolder.adapter=new GridAdapter(dates,month,d, isCurrentMonth,show_gregorian);
        }
        viewHolder.grid.setAdapter(viewHolder.adapter);
        //long end=System.currentTimeMillis();
        //long result=end-start;
        //Log.d(" Time it took = "," => "+result);

    }

    @Override
    public int getItemCount() {
        return span;
    }

    @Override
    public long getItemId(int position){
        return position;
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        RecyclerView grid;
        RecyclerView.LayoutManager layoutManager;
        GridAdapter adapter;
        ViewHolder(View itemView) {
            super(itemView);
            layoutManager=new GridLayoutManager(itemView.getContext(),7){
                @Override
                public boolean canScrollHorizontally() {
                    return false;
                }

                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            grid=itemView.findViewById(R.id.grid);
            grid.setHasFixedSize(true);
            grid.setNestedScrollingEnabled(false);
            grid.setLayoutManager(layoutManager);
        }
    }
    //in case I want to enable user to determine the span of calendar.
    public void setSpan(int span) {
        this.span = span;
    }
}

