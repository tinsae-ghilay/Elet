package com.tgk.Elet;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    int d,m,y,span;
    boolean eritrean,tigraian,show_gregorian;
    CalendarMonth[] calendarMonths;


    RecyclerAdapter(int span,boolean eritrean,boolean tigraian,boolean show_gregorian) {

        super();
        getDate();
        this.span=span;
        this.eritrean=eritrean;
        this.tigraian=tigraian;
        this.show_gregorian=show_gregorian;
    }

    private void getDate() {
        CurrentDate date=new CurrentDate();
        this.y = date.getCurrentGeezYear();
        this.m = date.getCurrentGeezMonth();
        this.d = date.getCurrentGeezDate();
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
        long start=System.currentTimeMillis();
        CalendarMonth calendarMonth=new CalendarMonth(i,span,eritrean,tigraian,show_gregorian);
        MonthItem monthItem= calendarMonth.getMonthItem();
        DateItem[] dates= monthItem.getDates();
        //Month and year of displayed  table.
        int month=monthItem.getMonth();
        int year=monthItem.getYear();
        boolean isCurrentMonth=(month==m&&year==y);
        //adapter.
        if (viewHolder.adapter!=null){
            viewHolder.adapter.changeData(dates,month,isCurrentMonth);
        }else {
            viewHolder.adapter=new GridAdapter(dates,month,d, isCurrentMonth,show_gregorian);
        }
        viewHolder.grid.setAdapter(viewHolder.adapter);
        long end=System.currentTimeMillis();
        long result=end-start;
        Log.d(" Time it took = "," => "+result);

    }

    public void setCalendarMonths(CalendarMonth[] calendarMonths) {
        this.calendarMonths = calendarMonths;
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
        //CalendarView calendarView;
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

