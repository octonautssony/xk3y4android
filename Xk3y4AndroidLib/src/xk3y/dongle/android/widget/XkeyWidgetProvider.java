package xk3y.dongle.android.widget;


import xk3y.dongle.android.R;
import xk3y.dongle.android.exception.XkeyException;
import xk3y.dongle.android.utils.WidgetUtils;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class XkeyWidgetProvider extends AppWidgetProvider {
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.xkey_widget_layout);
		remoteViews.setOnClickPendingIntent(R.id.prevButton, buildPrevButtonPendingIntent(context));
		remoteViews.setOnClickPendingIntent(R.id.nextButton, buildNextButtonPendingIntent(context));
		remoteViews.setOnClickPendingIntent(R.id.playButton, buildPlayButtonPendingIntent(context));
		remoteViews.setOnClickPendingIntent(R.id.reloadButton, buildReloadButtonPendingIntent(context));

		try {
			WidgetUtils.initData(remoteViews);
		} catch (XkeyException e) {
			if (e.getCode() != 0){
				remoteViews.setTextViewText(R.id.NomView, context.getString(e.getCode()));
			}else{
				Log.e("Error: ",e.getMessage(), e.getCause());
			}
		}
		pushWidgetUpdate(context, remoteViews);
	}

	public static PendingIntent buildPrevButtonPendingIntent(Context context) {
		Intent intent = new Intent();
	    intent.setAction("pl.looksok.intent.action.PREV_GAME");
	    return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}
	
	public static PendingIntent buildNextButtonPendingIntent(Context context) {
		Intent intent = new Intent();
	    intent.setAction("pl.looksok.intent.action.NEXT_GAME");
	    return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}
	
	public static PendingIntent buildPlayButtonPendingIntent(Context context) {
		Intent intent = new Intent();
	    intent.setAction("pl.looksok.intent.action.PLAY_GAME");
	    return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}
	
	public static PendingIntent buildReloadButtonPendingIntent(Context context) {
		Intent intent = new Intent();
	    intent.setAction("pl.looksok.intent.action.RELOAD_GAME");
	    return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}

	public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
		
		ComponentName myWidget = new ComponentName(context, XkeyWidgetProvider.class);
	    AppWidgetManager manager = AppWidgetManager.getInstance(context);
	    manager.updateAppWidget(myWidget, remoteViews);		
	}
}
