package com.tgk.Elet;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tgk.Elet.databinding.FragmentHolidaysBinding;

import java.util.ArrayList;
import java.util.Arrays;

import static com.tgk.Elet.HolyDaysList.setBooleans;

public class HolidaysFragment extends Fragment {

    private FragmentHolidaysBinding HolidayBinding;
    TextView daily,annual;
    ListView list;
    listAdapter adapter;
    int gy,gm;
    String gap,dateString,gms;
    String[] months;
    //booleans
    boolean eritrean,tigraian;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        HolidayBinding = FragmentHolidaysBinding.inflate(inflater, container, false);
        boolean[] booleans=setBooleans(requireActivity());
        eritrean=booleans[0];
        tigraian=booleans[1];
        return HolidayBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        daily=HolidayBinding.daily;
        annual= HolidayBinding.annual;
        annual.setOnClickListener(this::switchList);
        daily.setOnClickListener(this::switchList);
        list= HolidayBinding.holidaysList;
        CurrentDate today=new CurrentDate();
        gy=today.getCurrentGeezYear();
        gm=today.getCurrentGeezMonth();
        months=getResources().getStringArray(R.array.monthsList);
        gms=getResources().getStringArray(R.array.monthsList)[gm-1];
        gap=", ";
        dateString=gms+gap+gy;
        //------------------------------------------------------------------
        setAnnual();
        list.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        HolidayBinding = null;
    }
    // switch lists
    public void switchList(View view) {
        list.setAdapter(null);
        if (daily.equals(view)) {
            setDaily();
        }else {
            setAnnual();
        }
        list.setAdapter(adapter);
    }
    // populate annual holiday event names
    ArrayList<String> annualHolidays(){
        ArrayList<String> days=new ArrayList<>();
        for (int i=0;i<months.length;i++){
            ArrayList<String> array=new HolyDaysList(requireActivity(),i+1,gy,false,eritrean,tigraian).holiday_names;//HolyDays.updatedHolidays(this,months.get(i),y);
            if (array.size()!=0){
                days.addAll(array);
            }
        }
        return days;
    }
    // populate annual holiday dates.
    ArrayList<String> annualHolyDates(){
        ArrayList<String> days=new ArrayList<>();
        for (int i=0;i<13;i++){
            ArrayList<String> list=new HolyDaysList(requireActivity(),i+1,gy,false,eritrean,tigraian).holy_dates;
            ArrayList<String> list1=new ArrayList<>();
            if (list.size()!=0){
                for (String integer : list) {
                    if (integer.length()==1) {
                        integer=" "+integer;
                        integer=" "+integer;
                    }
                    list1.add(integer + " - " + months[i]);
                }
                days.addAll(list1);
            }
        }
        return days;
    }
    // daily Holidays(assignments) of the whole month.
    ArrayList<String> dailyEvents(){
        ArrayList<String> list=new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.daily_events)));
        list.remove(0);
        return list;
    }
    // dates for GEEZ DATE PICKER.
    ArrayList<String> oneThroughThirty(){
        ArrayList<String> dates=new ArrayList<>();
        for (int i=1;i<31;i++){
            String index;
            if (i<10){
                index=" "+i;
                index=" "+index;
            }else {
                index=""+i;
            }
            dates.add(index+" - "+gms);
        }
        return dates;
    }
    // set daily events in a list and redraw views
    void setDaily(){
        daily.setBackgroundResource(R.color.Neon);
        annual.setBackgroundColor(Color.WHITE);
        daily.setTextColor(Color.WHITE);
        annual.setTextColor(Color.DKGRAY);
        adapter=new listAdapter(getActivity(),oneThroughThirty(),dailyEvents(),false);
    }
    // set daily holidays
    void setAnnual(){
        annual.setBackgroundResource(R.color.Neon);
        annual.setTextColor(Color.WHITE);
        daily.setBackgroundColor(Color.WHITE);
        daily.setTextColor(Color.DKGRAY);
        adapter=new listAdapter(getActivity(),annualHolyDates(),annualHolidays(),false);
    }
}