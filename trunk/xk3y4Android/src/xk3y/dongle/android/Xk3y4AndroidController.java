package xk3y.dongle.android;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.acra.ErrorReporter;

import xk3y.dongle.android.dto.Iso;
import xk3y.dongle.android.dto.Xkey;
import xk3y.dongle.android.ihm.CoverFlowGamesActivity;
import xk3y.dongle.android.ihm.SettingsActivity;
import xk3y.dongle.android.utils.ConfigUtils;
import xk3y.dongle.android.utils.DialogBoxUtils;
import xk3y.dongle.android.utils.HttpServices;
import xk3y.dongle.android.utils.LoadingUtils;
import xk3y.dongle.android.utils.Xk3yParserUtils;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * Listener for the main view
 * @author maloups
 *
 */
public class Xk3y4AndroidController implements OnClickListener {
	
	public String urlToListGames = "http://" + ConfigUtils.getConfig().getIpAdress() + "/data.xml";
	
	/** the view */
	private Xk3y4AndroidActivity view;
	
	private static String defaultLoadingMessage = "Please wait...";
	private static String progressLoadingMessage = "Please wait...";
	private int error_to_display = 0;
	private ProgressDialog progressDialog;
	private static final int UPDATE_STARTED = 0;
	private static final int UPDATE_FINISHED = 1;
	private static final int UPDATE_PROGRESS = 2;
	private boolean SHOW_ERROR = false;
	private boolean SHOW_DEBUG_MSG = false;
	private String debugMsg = "";
	private String currentGameNameToDebug = "";
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case UPDATE_STARTED:
				progressDialog = ProgressDialog.show(view, "",
						"Loading. Please wait...", true, false);
				break;
			case UPDATE_PROGRESS:
				progressDialog.setMessage(progressLoadingMessage);
				break;
			case UPDATE_FINISHED:
				if (progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
				
				if (SHOW_ERROR) {
					new DialogBoxUtils(view,  error_to_display);
				} else if (SHOW_DEBUG_MSG) {
					new DialogBoxUtils(view, debugMsg);
				}
				break;

			}
			
		};
	};
	
	private void sendMessage(int what){
        Message msg = Message.obtain();
        msg.what = what;
        handler.sendMessage(msg);
    }
	
	/** Contructor */
	public Xk3y4AndroidController (Xk3y4AndroidActivity maVue) {
		this.view = maVue;
	}

	/**
	 * Evenement r�lalis� lors d'un click sur un bouton
	 */
	public void onClick(View v) {
		SHOW_ERROR = false;
		SHOW_DEBUG_MSG = false;
		if(v == view.getBtListGames()) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					sendMessage(UPDATE_STARTED);
					listGames();
					sendMessage(UPDATE_FINISHED);
				}

			}).start();
			
		} else if (v == view.getBtSettings()) {
			settings();
		}
	}


	/**
	 * Event to load games view
	 */
	private void listGames() {
		try {
			debugMsg = "List Games...";
			
			urlToListGames = "http://" + ConfigUtils.getConfig().getIpAdress()
					+ "/data.xml";
			String listGames = HttpServices.getInstance().getResponseFromUrl(
					urlToListGames);
			
			ErrorReporter.getInstance().putCustomData("\n\nTHEME", String.valueOf(ConfigUtils.getConfig().isLightTheme()));
			ErrorReporter.getInstance().putCustomData("\n\nDATA_XML", "\n" + listGames);
			
			debugMsg += "\nList Games OK";
			if (listGames == null
					|| (listGames != null && listGames.equals(""))) {
				
				debugMsg += "\nNo Xkey Connexion";
				
				SHOW_ERROR = true;
				error_to_display = R.string.not_connected;
			} else {
				// Generate xkey object from xml flow
				debugMsg += "\nConvert XML to Object...";
				Reader reader = new StringReader(listGames);
				Xkey xkey = Xk3yParserUtils.getXkey(reader);
				ConfigUtils.getConfig().setXkey(xkey);
				debugMsg += "\nConvert XML to Object OK";
				
				List<Iso> listIsos = ConfigUtils.getConfig().getListeGames();
				if (listIsos == null || (listIsos != null && listIsos.isEmpty())) {
					debugMsg += "\nNo Game detected";
					SHOW_ERROR = true;
					error_to_display = R.string.no_games;
				} else {

					// Load game info
					int cptLoad = 0;
					for (Iso game : listIsos){
						cptLoad++;
						currentGameNameToDebug = game.getTitle();
						progressLoadingMessage = defaultLoadingMessage
								+ "\nLoading game "+ cptLoad + "/" + listIsos.size()
								+ "\n"+ game.getTitle();
						sendMessage(UPDATE_PROGRESS);
						LoadingUtils.loadGameInfo(game);

					}

					// Open list games window
					currentGameNameToDebug = "All Games load...";
					debugMsg += "\nLaunch CoverFlow...";
					Intent myIntent = new Intent(view, CoverFlowGamesActivity.class);
					view.startActivity(myIntent);
				}
				
			}
			
		} catch (Exception e) {
			ErrorReporter.getInstance().putCustomData("\n\nLOADIG_GAME", "\n" + currentGameNameToDebug);
			ErrorReporter.getInstance().putCustomData("\n\nDEBUG_MESSAGE", "\n" + debugMsg);
			ErrorReporter.getInstance().handleException(e);
			//progressDialog.setMessage("boooom");
			//SHOW_DEBUG_MSG = true;
			//debugMsg += "\n" + e.getMessage();
			//error_to_display = R.string.error;
		}
		
	}

	/**
	 * Action permettant de creer un compte
	 */
	private void settings() {
		Intent myIntent = new Intent(view, SettingsActivity.class);
		view.startActivity(myIntent);
		
		/*
		PromptDialog dlg = new PromptDialog(view, R.string.title_settings,
				R.string.comment_setting, InputType.TYPE_CLASS_TEXT,
				ConfigUtils.getConfig().getIpAdress()) {
			
			@Override
			public boolean onOkClicked(String input) {
				Toast.makeText(view, "IP Save : " + input, Toast.LENGTH_SHORT).show();
				
				ConfigUtils.getConfig().setIpAdress(input);
				
				return true; // true = close dialog
			}
		};
		dlg.show();
		*/
	}
	
}
