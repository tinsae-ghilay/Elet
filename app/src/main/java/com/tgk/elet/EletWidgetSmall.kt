package com.tgk.elet

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import com.tgk.Elet.R
import com.tgk.elet.geezDate.GeezDate
import com.tgk.elet.localDate.DateLocal


/**
 * Implementation of App Widget functionality.
 */

const val UPDATE = "android.appwidget.action.APPWIDGET_UPDATE"
const val BOOTED = "android.intent.action.BOOT_COMPLETED"
var FLAG = 0
class EletWidgetSmall : AppWidgetProvider() {
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
        FLAG = if (android.os.Build.VERSION.SDK_INT >= 31) {
            PendingIntent.FLAG_MUTABLE;
        }else{
            PendingIntent.FLAG_UPDATE_CURRENT;
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
    var geezDate: GeezDate? = null
    var dateLocal: DateLocal? = null

    //CurrentDate currentDate;
    var calendar: Calendar? = null
    geezDate = GeezDate.now()
    dateLocal = DateLocal.fromJdn(geezDate.julianDay)

    val today: Int = geezDate.date
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.elet_small)

    TODO("add a function in Util, that takes a GeezDate object and returns a day of the week index");
    views.setTextViewText(R.id.week_day, context.resources.getStringArray(R.array.week_days)[6])
        //context.resources.getStringArray(R.array.week_days)[geezDate.date]);
    views.setTextViewText(R.id.small_geez,"$today  ${context.resources
        .getStringArray(R.array.monthsList)[geezDate.month-1]}, ${geezDate.year}");
    views.setTextViewText(R.id.small_gregorian
        ,"${dateLocal.date} ${context.resources.getStringArray(R.array.months)[dateLocal.month-1]} , ${dateLocal.year}");
    views.setTextViewText(R.id.day_holiday,
        context.resources.getStringArray(R.array.daily_events)[today]
    );

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}