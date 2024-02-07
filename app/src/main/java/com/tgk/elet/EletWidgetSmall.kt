package com.tgk.elet

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import com.tgk.Elet.R
import com.tgk.elet.common.Util.dayOfWeek
import com.tgk.elet.geezDate.GeezDate
import com.tgk.elet.localDate.DateLocal
import java.util.Calendar
import java.util.TimeZone


/**
 * Implementation of App Widget functionality.
 */

const val UPDATE = "android.appwidget.action.APPWIDGET_UPDATE"
const val BOOTED = "android.intent.action.BOOT_COMPLETED"
var FLAG = 0
class EletWidgetSmall : AppWidgetProvider() {
    var calendar: Calendar? = null
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
        FLAG = if (Build.VERSION.SDK_INT >= 31) {
            PendingIntent.FLAG_MUTABLE
        }else{
            PendingIntent.FLAG_UPDATE_CURRENT
        }
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        if ((intent?.action.equals(UPDATE))||(intent?.action.equals(BOOTED))){
            val manager = AppWidgetManager.getInstance(context)
            val ids = manager.getAppWidgetIds(context?.let { getComponentName(it) })
            for (id in ids) {
                updateAppWidget(context!!, manager, id)
            }
            context?.let { planUpdateAtMidNight(it) }
        }
        super.onReceive(context, intent)
    }
    private fun planUpdateAtMidNight(context: Context) {
        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, EletWidgetSmall::class.java)
        intent.setAction(UPDATE)
        val alarmIntent = PendingIntent.getBroadcast(context, 2, intent, FLAG)

        // Set the alarm to start at approximately 0:00 a.m.
        if (calendar == null) {
            calendar = Calendar.getInstance(TimeZone.getDefault())
            Log.d("- ELET -"," Calendar updated in Planer")
        }
        calendar?.add(Calendar.DAY_OF_MONTH, 1) //  set to Next day.
        calendar?.set(Calendar.HOUR_OF_DAY, 0)
        calendar?.set(Calendar.MINUTE, 0)
        calendar?.set(Calendar.SECOND, 1)

        //***below two lines are for test only***//
        /*int interval=60*1000*10; // must be at least 10 minutes because of google's(Android's) restrictions
            long now=System.currentTimeMillis();
            calendar.setTimeInMillis(now+interval);*/
        // ***** end of test code ***** //
        //Make it repeat everyday
        if (Build.VERSION.SDK_INT > 23) {
            calendar?.let {
                alarmMgr.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP, it.timeInMillis,
                    alarmIntent
                )
            }
        } else {
            calendar?.let {
                alarmMgr.setExact(
                    AlarmManager.RTC_WAKEUP, it.timeInMillis,
                    alarmIntent
                )
            }
        }
    }
}
internal fun getComponentName(context: Context): ComponentName {
    return ComponentName(context, EletWidgetSmall::class.java)
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {

    //CurrentDate currentDate;
    val c:Calendar = Calendar.getInstance(TimeZone.getDefault())
    val geezDate = GeezDate.from(c.get(Calendar.YEAR),c.get(Calendar.MONTH)+1,c.get(Calendar.DAY_OF_MONTH))
    Log.d("- ELET -"," Calendar updated in Planer")
    val dateLocal = DateLocal.fromJdn(geezDate.julianDay)

    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.elet_small)

    views.setTextViewText(R.id.week_day, context.resources.getStringArray(R.array.week_days)[geezDate.dayOfWeek()])
        //context.resources.getStringArray(R.array.week_days)[geezDate.date]);
    views.setTextViewText(R.id.small_geez,"${geezDate.date}  ${context.resources
        .getStringArray(R.array.monthsList)[geezDate.month-1]}, ${geezDate.year}")
    views.setTextViewText(R.id.small_gregorian
        ,"${dateLocal.date} ${context.resources.getStringArray(R.array.months)[dateLocal.month-1]} , ${dateLocal.year}")
    views.setTextViewText(R.id.day_holiday,
        context.resources.getStringArray(R.array.daily_events)[geezDate.date])

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}