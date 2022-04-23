package com.tgk.Elet;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.tgk.Elet.databinding.FragmentConverterBinding;

import java.util.ArrayList;

import static com.tgk.Elet.HolyDaysList.setBooleans;

public class ConverterFragment extends Fragment {

    private FragmentConverterBinding ConverterBinding;
    // date variables to be converted- can be geez or gregorian depending on the target
    int d,m,y;
    // geez dates for toolbar title.
    int gd,gy,gm;
    // Month and weekdays
    String[] days;
    String convert_to;
    myDatePicker picker;
    TextView result,from_geez,from_gregorian;
    ListView holy_days_list;
    CurrentDate date;
    boolean eritrean,tigraian;
    AdView adView;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        ConverterBinding = FragmentConverterBinding.inflate(inflater, container, false);
        return ConverterBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        date=new CurrentDate();
        boolean[] booleans=setBooleans(requireActivity());
        eritrean=booleans[0];
        tigraian=booleans[1];
        //-------------------------------
        // Dates start with Current date in Gregorian
        d=date.getCurrentGregorianDate();
        m=date.getCurrentGregorianMonth();
        y=date.getCurrentGregorianYear();

        days=requireActivity().getResources().getStringArray(R.array.week_days);
        picker= ConverterBinding.datePicker;
        picker.setSelectorToGeez(false);

        //Listen to Date changes in wheels.
        picker.setOnChangedDateListener((month, day, year) -> {
            d=day;
            m=month;
            y=year;
        });

        holy_days_list= ConverterBinding.holidaysList;
        from_geez= ConverterBinding.fromGeez;
        from_gregorian= ConverterBinding.fromGregorian;
        from_geez.setOnClickListener(this::switchTarget);
        from_gregorian.setOnClickListener(this::switchTarget);

        // setting geez dates.
        gy=date.getCurrentGeezYear();
        gm=date.getCurrentGeezMonth();
        gd=date.getCurrentGeezDate();
        initGregorian();
        //setFrom_gregorian();
        result= ConverterBinding.result;
        Button convert= ConverterBinding.convert;
        convert.setOnClickListener(this::OnConvertClick);

        // adView
        /*
        Handler handler=new Handler();
        Runnable runnable= () -> {
            adView = ConverterBinding.adView;
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        };
        handler.postDelayed(runnable,700);*/
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ConverterBinding = null;
        adView=null;
    }
    // convert date to and fro when convert button is clicked
    private String convertDate(){
        return new ConvertDate(d,m,y,convert_to).convertDate(days);
    }
    // handling convert button click.
    public void OnConvertClick(View view) {
        result.setText(convertDate());
        ConvertDate convertDate=new ConvertDate(d,m,y,convert_to);
        ArrayList<String> holidaysOfTheDay=new HolidaysOfTheDay(requireActivity(),convertDate.geez_date,
                convertDate.geez_month,convertDate.geez_year,eritrean,tigraian).getExtendedList();
        String[] array = holidaysOfTheDay.toArray(new String[0]);
        holy_days_list.setAdapter(new SingleItemAdapter(array,requireActivity()));
    }
    // switch to and fro for conversion targets.
    public void switchTarget(View view) {
        if (from_geez.equals(view)&&!convert_to.equalsIgnoreCase("geez")) {
            setFrom_geez();

        }else if (from_gregorian.equals(view)&&!convert_to.equalsIgnoreCase("ferenji")){
            setFrom_gregorian();
        }
    }
    // set conversion from Gregorian- involves switching to gregorian date picker
    void setFrom_gregorian(){
        picker.setSelectorToGeez(false);
        initGregorian();
        picker.resetWheels();
    }
    // set conversion from geez - involves switching to geez date picker
    void setFrom_geez(){
        picker.setSelectorToGeez(true);
        intGeez();
        picker.resetWheels();
    }
    // initialise required views for gregorian to geez
    void initGregorian(){
        convert_to = "ferenji";
        from_gregorian.setBackgroundResource(R.color.Neon);
        from_geez.setBackgroundColor(Color.WHITE);
        from_gregorian.setTextColor(Color.WHITE);
        from_geez.setTextColor(Color.DKGRAY);
        //-------------------------------
        d=date.getCurrentGregorianDate();
        m=date.getCurrentGregorianMonth();
        y=date.getCurrentGregorianYear();
    }
    // initialise required views for geez to gregorian
    void intGeez(){
        from_geez.setBackgroundResource(R.color.Neon);
        from_gregorian.setBackgroundColor(Color.WHITE);
        from_geez.setTextColor(Color.WHITE);
        from_gregorian.setTextColor(Color.DKGRAY);
        convert_to = "geez";
        //--------------------------
        d=date.getCurrentGeezDate();
        m=date.getCurrentGeezMonth();
        y=date.getCurrentGeezYear();
    }
}