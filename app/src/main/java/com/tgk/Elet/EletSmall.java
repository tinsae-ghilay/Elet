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
import android.widget.RemoteViews;

import androidx.preference.PreferenceManager;
import java.util.Calendar;
import java.util.Locale;

import static android.content.Intent.ACTION_BOOT_COMPLETED;

/**
 * Implementation of App Widget functionality.
 */
public class EletSmall extends AppWidgetProvider {
    final  static String UPDATE="android.appwidget.action.APPWIDGET_UPDATE";
    final static  String BOOTED="android.intent.action.BOOT_COMPLETED";
    int FLAG;
    GeezDate geezDate;
    DateLocal dateLocal;
    //CurrentDate currentDate;
    Calendar calendar;
    RemoteViews views;

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId){
            // establish the current date
            //currentDate=new CurrentDate();// Calendar.getInstance()
        geezDate=GeezDate.now();
        dateLocal=geezDate.to();

        int today= geezDate.dayOfTheWeek();//(Calendar.getInstance().get(Calendar.DAY_OF_WEEK))

        // Construct the RemoteViews object
        views = new RemoteViews(context.getPackageName(), R.layout.elet_small);
        // Pending intent to open app if user clicks on Widget.
        Intent intent=new Intent(context,MainActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,1,intent,FLAG);
        views.setOnClickPendingIntent(R.id.small_widget,pendingIntent);
        views.setTextViewText(R.id.week_day,context.getResources().getStringArray(R.array.week_days)[today]);
        views.setTextViewText(R.id.small_geez,geezDate.dayOfMonth
                +" "+context.getResources()
                .getStringArray(R.array.monthsList)[geezDate.month-1]+", "+geezDate.year);
        views.setTextViewText(R.id.small_gregorian
                ,dateLocal.dayOfMonth
                        +" "+context.getResources().getStringArray(R.array.months)[dateLocal.month-1]+", "+dateLocal.year);
        views.setTextViewText(R.id.day_holiday,
                context.getResources().getStringArray(R.array.daily_events)[geezDate.dayOfMonth]);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        setLocale(context);
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        //Flag for Pending intents
        if (android.os.Build.VERSION.SDK_INT >= 31) {
            FLAG= PendingIntent.FLAG_MUTABLE;
        }else{
            FLAG=PendingIntent.FLAG_UPDATE_CURRENT;
        }
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        // TODO kill footprint?.
    }
    // init alarm and set it to midnight.
    private void planUpdateAtMidNight(Context context){
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, EletSmall.class);
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
        }else{
            alarmMgr.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    alarmIntent);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //Receive update intent to make it update every day around midnight
        if ((intent.getAction().equals(UPDATE))||(intent.getAction().equals(BOOTED))){
            //if Locale isn't set here. it resets to default.
            setLocale(context);
            // establish the current date
            //geezDate=GeezDate.now();
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            int[] ids = manager.getAppWidgetIds(getComponentName(context));
            for (int id : ids) {
                updateAppWidget(context, manager, id);
            }

            // set alarm again when widget has been updated
            planUpdateAtMidNight(context);
        }
        super.onReceive(context, intent);
    }

    private static ComponentName getComponentName(Context context) {
        return new ComponentName(context, EletSmall.class);
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