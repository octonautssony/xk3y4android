package xk3y.dongle.android.widget;

import xk3y.dongle.android.R;
import xk3y.dongle.android.exception.XkeyException;
import xk3y.dongle.android.utils.ConfigUtils;
import xk3y.dongle.android.utils.SettingsUtils;
import xk3y.dongle.android.utils.WidgetUtils;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class XkeyWidgetIntentReceiver extends BroadcastReceiver {


	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		if (ConfigUtils.getConfig().getIpAdress() == null){
			SettingsUtils.loadMinimalSettings(context);
		}
		
		if(intent.getAction().equals("pl.looksok.intent.action.PREV_GAME")){
			updateWidgetPrevGameListener(context);
		}else if (intent.getAction().equals("pl.looksok.intent.action.NEXT_GAME")){
			updateWidgetNextGameListener(context);
		}else if (intent.getAction().equals("pl.looksok.intent.action.PLAY_GAME")){
			updateWidgetPlayGameListener(context);
		}else if (intent.getAction().equals("pl.looksok.intent.action.RELOAD_GAME")){
			updateWidgetReloadGameListener(context);
		}
		
	}

	private void updateWidgetPrevGameListener(Context context) {
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.xkey_widget_layout);
		try {
			WidgetUtils.previousGame(remoteViews);
		} catch (XkeyException e) {
			if (e.getCode() != 0){
				remoteViews.setTextViewText(R.id.NomView, context.getString(e.getCode()));
			}else{
				Log.e("Error: ",e.getMessage(), e.getCause());
			}
		}
		remoteViews.setOnClickPendingIntent(R.id.prevButton, XkeyWidgetProvider.buildPrevButtonPendingIntent(context));
		XkeyWidgetProvider.pushWidgetUpdate(context.getApplicationContext(), remoteViews);
	}
	
	private void updateWidgetNextGameListener(Context context) {
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.xkey_widget_layout);
		try {
			WidgetUtils.nextGame(remoteViews);
		} catch (XkeyException e) {
			if (e.getCode() != 0){
				remoteViews.setTextViewText(R.id.NomView, context.getString(e.getCode()));
			}else{
				Log.e("Error: ",e.getMessage(), e.getCause());
			}
		}
		remoteViews.setOnClickPendingIntent(R.id.prevButton, XkeyWidgetProvider.buildPrevButtonPendingIntent(context));
		XkeyWidgetProvider.pushWidgetUpdate(context.getApplicationContext(), remoteViews);
	}
	
	private void updateWidgetPlayGameListener(Context context) {
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.xkey_widget_layout);
		try {
			WidgetUtils.playGame();
			remoteViews.setTextViewText(R.id.NomView, context.getString(R.string.game_in_dvd_drive));
		} catch (XkeyException e) {
			if (e.getCode() != 0){
				remoteViews.setTextViewText(R.id.NomView, context.getString(e.getCode()));
			}else{
				Log.e("Error: ",e.getMessage(), e.getCause());
			}
		}
		remoteViews.setOnClickPendingIntent(R.id.prevButton, XkeyWidgetProvider.buildPrevButtonPendingIntent(context));
		XkeyWidgetProvider.pushWidgetUpdate(context.getApplicationContext(), remoteViews);
	}
	
	private void updateWidgetReloadGameListener(Context context) {
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.xkey_widget_layout);
		remoteViews.setTextViewText(R.id.NomView, context.getString(R.string.load_widget));
		XkeyWidgetProvider.pushWidgetUpdate(context.getApplicationContext(), remoteViews);
		try {
			WidgetUtils.initData(remoteViews);
		} catch (XkeyException e) {
			if (e.getCode() != 0){
				remoteViews.setTextViewText(R.id.NomView, context.getString(e.getCode()));
			}else{
				Log.e("Error: ",e.getMessage(), e.getCause());
			}
		}
		remoteViews.setOnClickPendingIntent(R.id.reloadButton, XkeyWidgetProvider.buildReloadButtonPendingIntent(context));
		XkeyWidgetProvider.pushWidgetUpdate(context.getApplicationContext(), remoteViews);
	}

}