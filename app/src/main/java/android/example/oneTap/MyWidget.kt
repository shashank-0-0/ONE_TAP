package android.example.oneTap

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.example.oneTap.Presentation.widgetActivity
import android.util.Log
import android.widget.RemoteViews


/**
 * Implementation of App Widget functionality.
 * broadcast receiver for the widget
 */
class MyWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            Log.i("#@#", "onupdate(")
            val views = RemoteViews(context.packageName, R.layout.my_widget)
            val intent = Intent(context, MyWidget::class.java)
            intent.action = "com.example.MyWidget.WIDGET_CLICK"
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            views.setOnClickPendingIntent(R.id.image_icon, pendingIntent)
            // Update the app widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }


    //starting the invisible widget activity
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == "com.example.MyWidget.WIDGET_CLICK") {
            // Start the LocationService
            Log.i("#@#", "onreceive()");

            val intent = Intent(context, widgetActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

}

