package com.tgk.Elet;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import androidx.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static android.content.Intent.ACTION_BOOT_COMPLETED;
import static com.tgk.Elet.HolyDaysList.setBooleans;

public class EletWidget extends AppWidgetProvider {
    final  static String UPDATE="android.appwidget.action.APPWIDGET_UPDATE";
    final static  String BOOTED="android.intent.action.BOOT_COMPLETED";
    int FLAG;
    //CurrentDate currentDate;
    GeezDate geezDate;
    DateLocal dateLocal;
    Calendar calendar;
    RemoteViews views;
    //-------------------------------

    static String[] week_latin={"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
    static String[] week_geez={"ሰንበት", "ሰኑይ", "ሰሉስ", "ረቡዕ", "ሓሙስ", "ዓርቢ", "ቀዳም"};
    static String[] latinMonths = {"January", "February", "March", "April", "May", "June", "July", "August","September", "October", "November", "December"};
    static String[] geezMonths = {"መስከረም", "ጥቅምቲ", "ሕዳር", "ታሕሳስ", "ጥሪ", "ለካቲት", "መጋቢት", "ሚያዝያ", "ግንቦት", "ሰነ", "ሓምለ", "ነሓሰ", "ጳጉሜን"};

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId){
        // establish the current date
        //currentDate=new CurrentDate();// Calendar.getInstance()
        geezDate=GeezDate.now();
        dateLocal=geezDate.to();

        int today=geezDate.dayOfTheWeek();//currentDate.getTodayInGeez();//(Calendar.getInstance().get(Calendar.DAY_OF_WEEK))
        // Construct the RemoteViews object
        boolean[] booleans=setBooleans(context);
        boolean eritrean=booleans[0];
        boolean tigraian=booleans[1];
        ArrayList<String> holidayToday=new HolidaysOfTheDay(context, geezDate.dayOfMonth, geezDate.month, geezDate.year,eritrean,tigraian)
                .getShortList();
        views = new RemoteViews(context.getPackageName(), R.layout.elet_widget);
        // Pending intent to open app if user clicks on Widget.
        Intent intent=new Intent(context,MainActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,1,intent,FLAG);
        views.setOnClickPendingIntent(R.id.widget,pendingIntent);

        //set Texts to textViews
        if (holidayToday.size()!=0){
            StringBuilder event= new StringBuilder();
            views.setViewVisibility(R.id.holiday_today, View.VISIBLE);
            for (int i=0;i< holidayToday.size();i++){
                event.append(holidayToday.get(i)).append(". ");

            }
            views.setTextViewText(R.id.holiday_today,event);
        }else {
            views.setViewVisibility(R.id.holiday_today, View.INVISIBLE);
            views.setTextViewText(R.id.holiday_today,"");
        }
        views.setTextViewText(R.id.geez_date, geezDate.dayOfMonth
                +"  "+geezMonths[geezDate.month-1]+",  "+ geezDate.year);
        //views.setTextViewText(R.id.week_day_geez,week_geez[today]);
        views.setTextViewText(R.id.day_geez,week_geez[today]);
        views.setTextViewText(R.id.day_latin,week_latin[today]);
        //views.setTextViewText(R.id.week_day_latin,week_latin[today]);
        views.setTextViewText(R.id.gregorian_date, dateLocal.dayOfMonth
                +"  "+latinMonths[dateLocal.month-1]+",  "+ dateLocal.year);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
        Log.d("WidgetId","is "+appWidgetId);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            try {
                updateAppWidget(context, appWidgetManager, appWidgetId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //Log.d("WIDGET", "Updated successfully at *****************************"+Calendar.getInstance().getTime());
    }

    @Override
    public void onEnabled(Context context) {
        setLocale(context);

        //Flag for Pending intents
        if (android.os.Build.VERSION.SDK_INT >= 31) {
            FLAG=PendingIntent.FLAG_MUTABLE;
        }else{
            FLAG=PendingIntent.FLAG_UPDATE_CURRENT;
        }
        Log.d("WIDGET "," STARTED************************");
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        // TODO kill footprint?.
    }
    private void planUpdateAtMidNight(Context context){
        setLocale(context);
        //-------------------------------
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, EletWidget.class);
        intent.setAction(UPDATE);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 2, intent, FLAG);

        // Set the alarm to start at approximately 0:00 a.m.
        if (calendar==null){
            calendar = Calendar.getInstance();
        }else{
            calendar.setTimeInMillis(System.currentTimeMillis());
        }
        calendar.add(Calendar.DAY_OF_MONTH, 1); //  set to Next day.
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);

        //***below two lines are for test only***//
        /*int interval=60*1000*10; // must be at least 10 minutes because of google's(Android's) restrictions
        long now=System.currentTimeMillis();
        calendar.setTimeInMillis(now+interval);*/
        // ***** end of test code ***** //
        //Make it repeat everyday
        if (Build.VERSION.SDK_INT>23){
            alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    alarmIntent);
            //Log.d("Alarm set","Exact and allow while idle");
        }else{
            alarmMgr.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    alarmIntent);
            //Log.d("Alarm set"," Only Exact");
        }
        //Log.d("Alarm and intent",""+alarmMgr.toString()+" "+alarmIntent.toString());
        //Log.d("WIDGET","Update planned********************************* for "+(calendar.getTime()));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        //Log.d("Intent received", "Action = "+intent.getAction());
        //Receive update intent to make it update every day around midnight
        if ((intent.getAction().equals(UPDATE))||(intent.getAction().equals(BOOTED))){
            // establish the current date
            //TODO check if date needs to be initiated
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            int[] ids = manager.getAppWidgetIds(getComponentName(context));
            for (int id : ids) {
                try {
                    updateAppWidget(context, manager, id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //Log.d("Update", "Executed at"+currentDate.getTimeNow());

            // set alarm again when widget has been updated
            planUpdateAtMidNight(context);
        }
        if (intent.getAction().equals(BOOTED)){
            Log.d("**ELET**","received "+BOOTED+" intent");
        }
    }
    private static ComponentName getComponentName(Context context) {
        return new ComponentName(context, EletWidget.class);
    }
    // get app locale from Preference
    public void setLocale(Context context) {
        String lang = PreferenceManager.getDefaultSharedPreferences(context).getString("lang","ትግርኛ");
        String loc;
        switch (lang) {
            case "English":
                loc = "en";
                break;
            case "Deutsch":
                loc = "de";
                break;
            default:
                loc = "ti";
        }
        //Locales
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        Locale locale = new Locale(loc);
        conf.setLocale(locale);
        res.updateConfiguration(conf, dm);
    }
}