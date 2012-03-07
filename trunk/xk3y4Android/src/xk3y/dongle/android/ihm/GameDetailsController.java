package xk3y.dongle.android.ihm;

import java.io.Reader;
import java.io.StringReader;

import xk3y.dongle.android.R;
import xk3y.dongle.android.dto.Xkey;
import xk3y.dongle.android.utils.ConfigUtils;
import xk3y.dongle.android.utils.DialogBoxUtils;
import xk3y.dongle.android.utils.HttpServices;
import xk3y.dongle.android.utils.Xk3yParserUtils;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * Listener for the main view
 * @author maloups
 *
 */
public class GameDetailsController implements OnClickListener {
	
	public String urlToListGames = "http://" + ConfigUtils.getConfig().getIpAdress() + "/data.xml";
	
	/** the view */
	private GameDetailsActivity view;
	
	
	private int detailMessage = R.string.not_connected;
	private ProgressDialog progressDialog;
	private static final int UPDATE_STARTED = 0;
	private static final int UPDATE_FINISHED = 1;
	private boolean SHOW_ERROR = false;
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case UPDATE_STARTED:
				progressDialog = ProgressDialog.show(view, "",
						"Loading. Please wait...", true, false);
				break;
			case UPDATE_FINISHED:
				if (progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
				
				if (SHOW_ERROR) {
					new DialogBoxUtils(view,  detailMessage);
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
	public GameDetailsController (GameDetailsActivity maVue) {
		this.view = maVue;
	}

	/**
	 * Evenement r�lalis� lors d'un click sur un bouton
	 */
	public void onClick(View v) {
		if(v == view.getBtLaunchGame()) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					sendMessage(UPDATE_STARTED);
					launchGame();
					sendMessage(UPDATE_FINISHED);
				}

			}).start();
			
		} else if (v == view.getBtBack()) {
			view.finish();
		} else if (v == view.getTextSummary()) {
			showSummary();
		}
		
		
	}


	private void showSummary() {
		new DialogBoxUtils(view, R.string.summary, view.getCurrentGame().getSummary());
	}

	/**
	 * Event to load games view
	 */
	private void launchGame() {
		try {

			String url = "http://" + ConfigUtils.getConfig().getIpAdress()
					+ "/launchgame.sh?" + view.getCurrentGame().getId();
			String response = HttpServices.getInstance().getResponseFromUrl(url);
			
			SHOW_ERROR = false;
			if (response == null
					|| (response != null && response.equals(""))) {
				SHOW_ERROR = true;
				detailMessage = R.string.not_connected;
			} else { 
				// Generate xkey object from xml flow
				Reader reader = new StringReader(response);
				Xkey xkey = Xk3yParserUtils.getXkey(reader);

				if (xkey.getTrayState().equals("1")) {
					SHOW_ERROR = true;
					if (xkey.getGuistate().equals("2")) {
						detailMessage = R.string.game_in_dvd_drive;
					} else {
						detailMessage = R.string.open_dvd_drive;
					}
				}
			}
			
		} catch (Exception e) {
			new DialogBoxUtils(view, R.string.error);
		}
		
	}

}
