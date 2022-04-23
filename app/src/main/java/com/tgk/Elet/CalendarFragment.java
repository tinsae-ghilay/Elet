package com.tgk.Elet;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.google.android.material.snackbar.Snackbar;
import com.tgk.Elet.databinding.FragmentCalendarBinding;

import static com.tgk.Elet.HolyDaysList.setBooleans;

public class CalendarFragment extends Fragment {

    private FragmentCalendarBinding CalendarBinding;
    int POSITION,SCREEN_WIDTH;
    int span=2600;
    int MIDDLE = span/2;
    String[] monthsList;
    //--------
    int gd,gm,gy;
    // views ----------------
    RecyclerView recyclerView;
    listAdapter adapter;
    TextView calendar_title;
    Button next,back;
    ListView h_list;
    //--------------------------------------
    RecyclerView.LayoutManager layoutManager;
    RecyclerAdapter recycler_adapter;
    //---------------------------
    static String[] daily_events;
    static int first_item;
    //booleans---------------
    boolean eritrean,tigraian,show_gregorian;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        CalendarBinding = FragmentCalendarBinding.inflate(inflater, container, false);
        boolean[] booleans=setBooleans(requireActivity());
        eritrean=booleans[0];
        tigraian=booleans[1];
        show_gregorian=booleans[2];
        return CalendarBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //setBooleans();
        // Arrays from Context
        daily_events=getResources().getStringArray(R.array.daily_events);
        monthsList = getResources().getStringArray(R.array.monthsList);
        // declare the date
        declareDate();
        // Inflating Views for displaying days of the week.
        setWeekDays();
        // back and forth navigation buttons for calendar
        initCalendarNavButtons();
        //title for the calendar table. i.e month and year
        initCalendarTitle();
        //setting up RecyclerView. its LayOut manager and adapter
        initRecyclerView();
        // display holidays of this month.
        h_list=CalendarBinding.eventsList;
        //displayList(getActivity(),gm,gy);
        // snap-it like it's a(view)Pager!
        SnapHelper helper = new PagerSnapHelper();
        helper.attachToRecyclerView(recyclerView);
    }

    private void initCalendarTitle() {
        calendar_title=CalendarBinding.calendarTitle;
        calendar_title.setOnClickListener(this::onReturnClick);
        TextView event_day=CalendarBinding.today;
        String event=gd+" - "+daily_events[gd];
        event_day.setText(event);
        setCalendarTitle(gy,gm);
    }

    private void declareDate() {
        CurrentDate today=new CurrentDate();
        gd=today.getCurrentGeezDate();
        gm=today.getCurrentGeezMonth();
        gy=today.getCurrentGeezYear();
        SCREEN_WIDTH=getSCREEN_WIDTH();
    }

    private void initRecyclerView() {
        recyclerView = CalendarBinding.recyclerView;
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setItemAnimator(null);

        // layout manager
        layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);

        // adapter
        recycler_adapter = new RecyclerAdapter(span,eritrean,tigraian,show_gregorian);
        recycler_adapter.setHasStableIds(true);
        recyclerView.setAdapter(recycler_adapter);

        //Loading the middle item for ability to scroll back on calendar.
        first_item= MIDDLE-1 +gm;
        POSITION=first_item;
        recyclerView.scrollToPosition(first_item);
        // listen to scroll.
        recyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            int attached =first_item;
            int detached;
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {
                attached =recyclerView.getChildLayoutPosition(view);
                POSITION=attached;
            }
            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {
                detached = recyclerView.getChildLayoutPosition(view);
                boolean stayed= attached == detached;
                if (!stayed){
                    ScrollWatcher watcher=new ScrollWatcher(attached,span);
                    gm= watcher.getMonth();
                    gy=watcher.getGeezYear();
                    setCalendarTitle(gy,gm);
                    displayList(getActivity(),gm,gy);
                    dismissSnackBar();
                }
            }
        });
    }
    // Dismiss snackBar in Main Activity.
    private void dismissSnackBar() {
        Snackbar snackbar=((MainActivity) requireActivity()).bar;
        if (snackbar!=null&& snackbar.isShown()){
            snackbar.dismiss();
        }
    }

    // buttons next and previous
    private void initCalendarNavButtons() {
        next=CalendarBinding.next;
        back=CalendarBinding.back;
        //CalendarView calendarView=new CalendarView(requireActivity());
        next.setOnClickListener(this::onNextClick);
        back.setOnClickListener(this::onBackClick);
    }

    // navigate back
    public void onNextClick(View view){
        recyclerView.smoothScrollBy(SCREEN_WIDTH,0);
    }
    // navigate forward
    public void onBackClick(View view){
        recyclerView.smoothScrollBy(-(SCREEN_WIDTH), 0);
    }
    // navigate to start
    public void onReturnClick(View view){
        resetCalendar();
    }
    // width of screen used for scrolling on navigation buttons click.
    int getSCREEN_WIDTH(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }
    // revert to current month view
    private void resetCalendar(){
        recyclerView.scrollToPosition(first_item);
        ScrollWatcher watcher = new ScrollWatcher(first_item,span);
        displayList(requireActivity(), watcher.getMonth(), watcher.getGeezYear());
    }
    // Holidays list to be displayed under calendar table
    void displayList(Context context,int gm, int gy){
        HolyDaysList holyDaysList=new HolyDaysList(context,gm,gy,true,eritrean,tigraian);
        if (adapter==null){
            adapter=new listAdapter(context,holyDaysList.holy_dates,holyDaysList.holiday_names,true);
            h_list.setAdapter(adapter);
        }else {
            adapter.updateData(holyDaysList.holy_dates,holyDaysList.holiday_names);
        }
    }
    // set calendar title
    void setCalendarTitle(int gy, int month){
        String top_calendar_title=monthsList[month-1]+" ,"+gy;
        calendar_title.setText(top_calendar_title);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dismissSnackBar();
        recycler_adapter=null;
        adapter=null;
        layoutManager=null;
        CalendarBinding = null;
    }
    @Override
    public void onResume() {
        super.onResume();
        resetCalendar();
    }
    void setWeekDays(){
        String[] week_days=getResources().getStringArray(R.array.week_days);// name of days of the week.
        for (int i=0;i<week_days.length;i++){
            // iterate through textViews in LinearLayout and set text.
            TextView dayOfTheWeek=(TextView)CalendarBinding.weekDays.getChildAt(i);
            dayOfTheWeek.setText(week_days[i]);
        }
    }
}